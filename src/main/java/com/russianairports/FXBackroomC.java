package com.russianairports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;

public class FXBackroomC {
    Statement stmtObj;
    Stage stage;
    Scene backScene;
    Scene currentScene;
    public void setBackroomC(Statement stmtObj,Scene backScene,Scene currentScene, Stage stage) {
        this.stmtObj = stmtObj;
        this.backScene = backScene;
        this.stage = stage;
        this.currentScene = currentScene;
    }
    @FXML
    void AdminStart(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXAdmin.fxml"));
        try{
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXAdminC intC = loader.getController();
            intC.setADC(stmtObj,this.currentScene,scene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void EmployeeStart(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXEmployee.fxml"));
        try{
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXEmployeeC intC = loader.getController();
            intC.setEmployeeC(stmtObj,this.currentScene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void back(ActionEvent event) {
        stage.setScene(this.backScene);
        stage.show();
    }
}
