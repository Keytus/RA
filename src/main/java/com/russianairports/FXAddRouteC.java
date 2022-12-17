package com.russianairports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.russianairports.ExceptionLogger.LogException;

public class FXAddRouteC
{
    Statement stmtObj;
    Stage stage;
    Scene backScene;
    ArrayList<Tracking> trackings = new ArrayList<>();
    public void setAddRoute(Statement stmtObj, Scene backScene, Stage stage) {
        this.stmtObj = stmtObj;
        this.backScene = backScene;
        this.stage = stage;

        trackings.add(new Tracking("",true));
        try{
            ResultSet resObj = stmtObj.executeQuery("SELECT StartAirport, EndAirport FROM Track");
            while (resObj.next()) {
                trackings.add(new Tracking(resObj.getString("StartAirport") + "->" + resObj.getString("EndAirport"), true));
            }
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }

        trackings.forEach((tracking -> {
            track1.getItems().add(tracking.getTrack());
            track2.getItems().add(tracking.getTrack());
            track3.getItems().add(tracking.getTrack());
        }));

        track1.setValue("");
        track2.setValue("");
        track3.setValue("");

    }
    @FXML
    private Button add;

    @FXML
    private ComboBox<String> track1 = new ComboBox<String>();

    @FXML
    private ComboBox<String> track2 = new ComboBox<String>();

    @FXML
    private ComboBox<String> track3 = new ComboBox<String>();

    @FXML
    private Button refresh;

    @FXML
    private Text notification;

    @FXML
    void Refresh(ActionEvent event) {
        track1.getItems().clear();
        track2.getItems().clear();
        track3.getItems().clear();

        trackings.forEach((tracking -> {
            track1.getItems().add(tracking.getTrack());
            track2.getItems().add(tracking.getTrack());
            track3.getItems().add(tracking.getTrack());
        }));

        track1.setValue("");
        track2.setValue("");
        track3.setValue("");
    }


    @FXML
    void Add(ActionEvent event) {
        String query = "INSERT INTO Route(TRACK1,TRACK2,TRACK3) VALUES (";

        if (!track1.getValue().equals(""))
        {
            if(track2.getValue().equals("") && !track3.getValue().equals(""))
            {
                notification.setText("Не выбран 2 путь,когда выбран 3");
                return;
            }
            String[] tokens1 = track1.getValue().split("->");
            query += "(SELECT TrackID FROM Track WHERE StartAirport='"+ tokens1[0] +
                    "' AND EndAirport='"+ tokens1[1] + "'),";
            if(track2.getValue().equals(""))
            {
                query += "NULL,NULL)";
                try{
                    stmtObj.executeUpdate(query);
                }
                catch (Exception exception) {
                    LogException(exception, stmtObj);
                }
                return;
            }
            String[] tokens2 = track2.getValue().split("->");
            if(!tokens1[1].equals(tokens2[0]))
            {
                notification.setText("Начало второго не совпадает с концом первого");
                return;
            }
            query += "(SELECT TrackID FROM Track WHERE StartAirport='"+ tokens2[0] +
                    "' AND EndAirport='"+ tokens2[1] + "'),";
            if(track3.getValue().equals(""))
            {
                query += "NULL)";
                try{
                    stmtObj.executeUpdate(query);
                }
                catch (Exception exception) {
                    LogException(exception, stmtObj);
                }
                return;
            }
            String[] tokens3 = track3.getValue().split("->");
            if(!tokens2[1].equals(tokens3[0]))
            {
                notification.setText("Начало третьего не совпадает с концом второго");
                return;
            }
            query += "(SELECT TrackID FROM Track WHERE StartAirport='"+ tokens3[0] +
                    "' AND EndAirport='"+ tokens3[1] + "'))";

            try{
                stmtObj.executeUpdate(query);
            }
            catch (Exception exception) {
                LogException(exception, stmtObj);
            }
            notification.setText("Успех,бойцы");
        }
        else notification.setText("Не выбран 1 путь");

    }

    @FXML
    void back(ActionEvent event) {
        stage.setScene(this.backScene);
        stage.show();
    }

    @FXML
    void changeCombo1(ActionEvent event) {

        if (track1.getValue() == null)
        {
            track1.setValue("");
        }
        String selectedTrack = track1.getValue();

        if (!selectedTrack.equals(""))
        {
            track2.getItems().remove(selectedTrack);
            track3.getItems().remove(selectedTrack);
        }

    }

    @FXML
    void changeCombo2(ActionEvent event) {
        if (track2.getValue() == null)
        {
            track2.setValue("");
        }
        String selectedTrack = track2.getValue();
        if (!selectedTrack.equals(""))
        {
            track1.getItems().remove(selectedTrack);
            track3.getItems().remove(selectedTrack);
        }

    }

    @FXML
    void changeCombo3(ActionEvent event) {
        if (track3.getValue() == null)
        {
            track3.setValue("");
        }
        String selectedTrack = track3.getValue();
        if (!selectedTrack.equals(""))
        {
            track1.getItems().remove(selectedTrack);
            track2.getItems().remove(selectedTrack);
        }
    }
}
