package com.russianairports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;
import static com.russianairports.MD5Hashing.MD5Encrypt;

public class FXregC extends  FXbasic{

    Statement stmtObj;

    public void setRegC(Statement stmtObj, Scene scene, Stage stage) {

        this.stmtObj = stmtObj;
        this.scene = scene;
        this.stage = stage;
    }
    String name3;
    private Boolean Reg1 = false, Reg2 = false, Reg3 = false;
    @FXML
    private Text log1;

    @FXML
    private Text passw1;

    @FXML
    private Text passw2;

    @FXML
    private Text RegFinal;

    @FXML
    private TextField logW;

    @FXML
    private PasswordField PasW1;

    @FXML
    private PasswordField PasW2;

    @FXML
    private TextField emailW;

    @FXML
    private TextField fullNameW;

    @FXML
    private TextField pasportW;

    @FXML
    void loginScan(KeyEvent event) throws SQLException {
        name1 = logW.getText();
        Boolean existedLogin = false;

        ResultSet resObj = stmtObj.executeQuery("SELECT Login FROM Customer");
        while (resObj.next()) {
            if(resObj.getString("Login").equals(name1))
            {
                existedLogin = true;
            }
        }

        if (name1.equals("Admin")) {log1.setText("Большой брат следит за тобой и не допустит этого"); Reg1 = false;}
        else if (name1.length() < 8) {log1.setText("Логин слишком короткий"); Reg1 = false;}
        else if (name1.length() > 12) {log1.setText("Логин слишком длинный"); Reg1 = false;}
        else if (existedLogin) {log1.setText("Логин занят"); Reg1 = false;}
        else {log1.setText("Вы можете исспользовать этот логин"); Reg1 = true;}
    }

    @FXML
    void passw1Scan(KeyEvent event) {
        name3 = PasW1.getText();
        if (name3.length() < 8) {passw1.setText("Пароль слишком короткий"); Reg2 = false;}
        else if (name3.length() > 12) {passw1.setText("Пароль слишком длинный"); Reg2 = false;}
        else {passw1.setText("Вы можете использовать этот пароль"); Reg2 = true;}
    }

    @FXML
    void passw2Scan(KeyEvent event) {
        name2 = PasW2.getText();
        if (!name2.equals(name3)) {passw2.setText("Пароли не совпадают"); Reg3 = false;}
        else {passw2.setText("Пароли совпали"); Reg3 = true;}
    }

    public void registerMain(ActionEvent actionEvent){
        if (Reg1 && Reg2 && Reg3)
        {
            String emailString = "NULL";
            if (!emailW.getText().equals(""))
            {
                emailString = "'"+emailW.getText()+"'";
            }
            try {
                String query = "INSERT INTO Customer VALUES ('"+
                        logW.getText() + "'," +
                        emailString + "," +
                        pasportW.getText() + ",'" +
                        fullNameW.getText() + "','" +
                        MD5Encrypt(PasW1.getText()) + "')";
                stmtObj.executeUpdate(query);
            }
            catch (Exception exception) {
                LogException(exception, stmtObj);
            }

            RegFinal.setText("Регистрация успешна! Войдите в свой аккаунт через главное меню.");
        }
        else RegFinal.setText("Регистрация отклонена");
    }


    @FXML @Override
    void back(ActionEvent event) {
        stage.setScene(this.scene);
        stage.show();
    }
}