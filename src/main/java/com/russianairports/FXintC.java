package com.russianairports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;

public class FXintC {
    String currentCustomerLogin;
    Statement stmtObj;
    Scene scene;
    Stage stage;

    public void setIntC(Statement stmtObj,Scene scene, Stage stage, String currentCustomerLogin) {
        this.stmtObj = stmtObj;
        this.scene = scene;
        this.stage = stage;
        this.currentCustomerLogin = currentCustomerLogin;
    }
    @FXML
    void admin(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXBackroom.fxml"));
        try{
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXBackroomC intC = loader.getController();
            intC.setBackroomC(stmtObj, this.scene,scene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void exit(ActionEvent event) {
        stage.close();
    }

    @FXML
    void register(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXreg.fxml"));
        try{
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXregC intC = loader.getController();
            intC.setRegC(stmtObj, this.scene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void vhod(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXlogin.fxml"));
        try{
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXloginC intC = loader.getController();
            intC.setLoginC(stmtObj, this.scene, scene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }
}
