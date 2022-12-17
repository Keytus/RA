package com.russianairports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;
import static com.russianairports.MD5Hashing.MD5Encrypt;

public class FXloginC extends  FXbasic{
    String currentCustomerLogin;
    Scene currentScene;
    Statement stmtObj;
    public void setLoginC(Statement stmtObj, Scene backScene,Scene currentScene, Stage stage) {
        this.stmtObj = stmtObj;
        this.scene = backScene;
        this.currentScene = currentScene;
        this.stage = stage;
    }

    private Boolean logState = false;

    @FXML
    private TextField logW;


    @FXML
    private PasswordField PasW1;

    @FXML
    private Text RegFinal;

    @FXML @Override
    void back(ActionEvent event) {
        stage.setScene(this.scene);
        stage.show();
    }

    @FXML
    void loginMain(ActionEvent event) {
        try {
            ResultSet resObj = stmtObj.executeQuery("SELECT Login, Password FROM Customer");
            while (resObj.next()) {
                if(resObj.getString("Login").equals(logW.getText()) && resObj.getString("Password").equals(MD5Encrypt(PasW1.getText())))
                {
                    currentCustomerLogin = logW.getText();
                    logState = true;
                }
            }
            if (logState) {
                RegFinal.setText("Вход успешен, перенаправление.");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MAINDOOR.class.getResource("FXUserM.fxml"));
                AnchorPane root = loader.load();
                Scene scene1 = new Scene(root);
                stage.setScene(scene1);
                stage.show();
                FXUserMC intC = loader.getController();
                intC.setUserMC(stmtObj,currentCustomerLogin, this.scene, scene1, stage);
            }
            else RegFinal.setText("Введен неверный логин или пароль");
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void resetPwd(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXPwdRestore.fxml"));
        try {
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXPwdRestoreC intC = loader.getController();
            intC.setPwdRestoreC(stmtObj, currentScene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }
}
