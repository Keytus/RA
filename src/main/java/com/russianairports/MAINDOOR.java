package com.russianairports;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;


public class MAINDOOR extends Application {

    static  void main (String [] args){
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        Statement stmtObj = null;
        try {
            String dbURL = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "coop_prod";
            String password = "pwd4coop_prod";
            Connection connObj;
            connObj = DriverManager.getConnection(dbURL, user, password);
            stmtObj = connObj.createStatement();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MAINDOOR.class.getResource("FXint.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXintC intC = loader.getController();
            intC.setIntC(stmtObj, scene, stage,null);
        }
        catch (Exception exception){
            LogException(exception, stmtObj);
        }
    }
}
