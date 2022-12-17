package com.russianairports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

import static com.russianairports.ExceptionLogger.LogException;
import static com.russianairports.MD5Hashing.MD5Encrypt;

public class FXPwdRestoreC extends  FXbasic {
    String сustomerLogin;
    Statement stmtObj;
    private Boolean logExist = false;

    public void setPwdRestoreC(Statement stmtObj, Scene scene, Stage stage) {
        this.stmtObj = stmtObj;
        this.scene = scene;
        this.stage = stage;
    }

    @FXML
    private TextField logW;

    @FXML
    private Text messageText;

    @FXML
    private TextField pwdNew;

    @FXML @Override
    void back(ActionEvent event) {
        stage.setScene(this.scene);
        stage.show();
    }

    @FXML
    void replacePwd(ActionEvent event) {
        try{
            ResultSet resObj = stmtObj.executeQuery("SELECT Login FROM Customer");
            while (resObj.next()) {
                if(resObj.getString("Login").equals(logW.getText())) {
                    сustomerLogin = logW.getText();
                    logExist = true;
                }
            }
            if (logExist) {
                stmtObj.executeUpdate("UPDATE Customer SET password='"+MD5Encrypt(pwdNew.getText())+ "' WHERE login = '"+сustomerLogin +"'");
                messageText.setText("Успешно");
            }
            else {
                messageText.setText("Печально");
            }
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }
}
