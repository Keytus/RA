package com.russianairports;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;

public class FXAdminFuncC {
    Statement stmtObj;
    Scene backScene;
    Stage stage;
    ObservableList<String> formatList = FXCollections.observableArrayList("xml", "json", "csv");
    ObservableList<String> restoreList = FXCollections.observableArrayList("Дата", "Последнее", "n последних");
    public void setAdminFunc(Statement stmtObj, Scene backScene, Stage stage) {
        this.stmtObj = stmtObj;
        this.backScene = backScene;
        this.stage = stage;

        datePicker.setVisible(false);
        restoreCount.setVisible(false);

        formatBox.setValue(formatList.get(0));
        formatBox.setItems(formatList);
        restoreFormat.setValue(restoreList.get(0));
        restoreFormat.setItems(restoreList);

        restoreCount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    restoreCount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private ComboBox<String> formatBox;
    @FXML
    private ComboBox<String> restoreFormat;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField restoreCount;
    @FXML
    private Text messageText;
    @FXML
    void ChangeFormat(ActionEvent event) {

    }
    @FXML
    void ChangeRestore(ActionEvent event) {
        switch (restoreFormat.getValue()){
            case "Дата":
                datePicker.setVisible(true);
                restoreCount.setVisible(false);
                break;
            case "Последнее":
                datePicker.setVisible(false);
                restoreCount.setVisible(false);
                break;
            case "n последних":
                datePicker.setVisible(false);
                restoreCount.setVisible(true);
                break;
            default:
                break;
        }
    }
    @FXML
    void Export(ActionEvent event) throws SQLException {
        String query = "";
        switch (formatBox.getValue()) {
            case "xml":
                query = "DECLARE\n" +
                        "       fhandle UTL_FILE.FILE_TYPE;\n" +
                        "       MYCLOB CLOB;\n" +
                        "   BEGIN\n" +
                        "       SELECT\n" +
                        "         DBMS_XMLGEN.GETXML('\n" +
                        "           select * from PlaneModel\n" +
                        "        ')\n" +
                        "      INTO MYCLOB\n" +
                        "      FROM DUAL;\n" +
                        "\n" +
                        "      fhandle := UTL_FILE.FOPEN('TEMP_DIR','PlaneModels.xml','w',32767);\n" +
                        "      UTL_FILE.PUT(fhandle,MYCLOB);\n" +
                        "      UTL_FILE.FCLOSE(fhandle);\n" +
                        "END;";
                break;
            case "json":
                query = "\n" +
                        "DECLARE\n" +
                        "    ret      json_list;\n" +
                        "    fhandle  utl_file.file_type;\n" +
                        "BEGIN\n" +
                        "    fhandle := utl_file.fopen('TEMP_DIR',\n" +
                        "                             'PlaneModels.json',\n" +
                        "                             'w');\n" +
                        "    ret := json_dyn.executelist('select * from PlaneModel');\n" +
                        "    utl_file.put(fhandle, json_printer.pretty_print_list(ret));\n" +
                        "    utl_file.fclose(fhandle);\n" +
                        "END;\n";
                break;
            case "csv":
                query = "DECLARE\n" +
                        "    fhandle  utl_file.file_type;\n" +
                        "    CURSOR plane_models IS \n" +
                        "    select *\n" +
                        "    from PlaneModel;\n" +
                        "BEGIN\n" +
                        "    fhandle := utl_file.fopen('TEMP_DIR',\n" +
                        "                             'PlaneModels.csv',\n" +
                        "                             'w');\n" +
                        "    FOR plane_model in plane_models\n" +
                        "    LOOP\n" +
                        "        utl_file.put(fhandle, '\"'||plane_model.MODELNAME||'\",'\n" +
                        "    ||''||plane_model.ECONOMYTOTAL||','\n" +
                        "    ||''||plane_model.FIRSTCLASSTOTAL||','\n" +
                        "    ||''||plane_model.LUXTOTAL||','\n" +
                        "    ||''||plane_model.FUELCONSUMPTION||'' || chr(13));\n" +
                        "    END LOOP;\n" +
                        "    utl_file.fclose(fhandle);\n" +
                        "END;";
                break;
            default:
               return;
        }
        try {
            stmtObj.execute(query);
            messageText.setText("Успешный экспорт в файл PlaneModels."+ formatBox.getValue());
        }
        catch (Exception exception) {
            messageText.setText("Провальный экспорт");
            LogException(exception, stmtObj);
        }
    }
    @FXML
    void Restore(ActionEvent event) throws SQLException {
        String query = "DECLARE\n" +
                "    test_time     TIMESTAMP;\n" +
                "BEGIN\n";
        switch (restoreFormat.getValue()){
            case "Дата":
                if (datePicker.getValue() == null){
                    messageText.setText("Не выбрана дата");
                    return;
                }
                query += "    test_time := to_timestamp('"+ datePicker.getValue().toString() +"', 'YYYY-MM-DD');\n"+
                        "restore_package.restore(test_time);\n";
                break;
            case "Последнее":
                query += "restore_package.restore;\n";
                break;
            case "n последних":
                if (restoreCount.getText().equals("")){
                    messageText.setText("Неправильное количество изменений");
                    return;
                }
                query += "restore_package.restore("+restoreCount.getText()+");\n";
                break;
            default:
                break;
        }
        query += "END;";
        try {
            stmtObj.execute(query);
            messageText.setText("Успешный откат");
        }
        catch (Exception exception) {
            messageText.setText("Провальный откат");
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void Back(ActionEvent event) {
        stage.setScene(this.backScene);
        stage.show();
    }
}
