package com.russianairports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;

public class FXUserMC extends  FXbasic{
    String currentCustomerLogin;
    Statement stmtObj;
    Scene userMScene;
    ObservableList<String> languagesList = FXCollections.observableArrayList("RU", "ENG");
    public void setUserMC(Statement stmtObj,String currentCustomerLogin, Scene scene, Scene userMScene, Stage stage) {
        this.stmtObj = stmtObj;
        this.currentCustomerLogin = currentCustomerLogin;
        this.scene = scene;
        this.userMScene = userMScene;
        this.stage = stage;
        languagesBox.setValue(languagesList.get(0));
        languagesBox.setItems(languagesList);
        try {
            ResultSet resObj = stmtObj.executeQuery("SELECT " + languagesBox.getValue() + " FROM Customer " +
                    "JOIN Languages ON (Customer.Description = Languages.ID) " +
                    "WHERE Login='" + currentCustomerLogin + "'");
            resObj.next();
            status.setText(resObj.getString(languagesBox.getValue()));
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    private ComboBox<String> languagesBox;

    @FXML
    private Text status;

    @FXML
    void Basket(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXBucket.fxml"));
        try {
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXBucketC intC = loader.getController();
            intC.setBuckC(stmtObj,currentCustomerLogin, this.userMScene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void MakeOrder(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXOrder.fxml"));
        try {
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXOrderC intC = loader.getController();
            intC.setOrdC(stmtObj,currentCustomerLogin,this.userMScene,scene,stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void changeLanguage(ActionEvent event) {
        try {
            ResultSet resObj = stmtObj.executeQuery("SELECT " + languagesBox.getValue() + " FROM Customer " +
                    "JOIN Languages ON (Customer.Description = Languages.ID) " +
                    "WHERE Login='" + currentCustomerLogin + "'");
            resObj.next();
            status.setText(resObj.getString(languagesBox.getValue()));
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML @Override
    void back(ActionEvent event) {
        stage.setScene(this.scene);
        stage.show();
    }

}

