package com.russianairports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FXEmployeeC
{
    Statement stmtObj;
    Stage stage;
    Scene scene;
    ObservableList<String> flightIDList = FXCollections.observableArrayList();
    public void setEmployeeC(Statement stmtObj,Scene scene, Stage stage) throws SQLException {
        this.stmtObj = stmtObj;
        this.scene = scene;
        this.stage = stage;

        ResultSet resObj = stmtObj.executeQuery("SELECT FlightID FROM Flight");
        while (resObj.next()) {
            flightIDList.add(Integer.toString(resObj.getInt("FlightID")));
        }
        flightCombo.setItems(flightIDList);
    }
    @FXML
    private ComboBox<String> flightCombo;

    @FXML
    private TextArea textArea;

    @FXML
    void ChangeCombo(ActionEvent event) throws SQLException {
        textArea.clear();

        ResultSet resObj = stmtObj.executeQuery("SELECT (EconomyReserved+FirstClassReserved+LuxReserved) TotalReserved,(EconomyTotal+FirstClassTotal+LuxTotal) Space,EconomyReserved,FirstClassReserved,LuxReserved,EconomyTotal,FirstClassTotal,LuxTotal, " +
                "((EconomyReserved*(SELECT ServiceCost FROM ServiceClass WHERE ClassName = 'Эконом')+FirstClassReserved*(SELECT ServiceCost FROM ServiceClass WHERE ClassName = 'Бизнес')+LuxReserved*(SELECT ServiceCost FROM ServiceClass WHERE ClassName = 'Люкс'))*(Length*FuelCost*FuelConsumption)) FlightIncome, " +
                "(EconomyReserved*(SELECT ServiceCost FROM ServiceClass WHERE ClassName = 'Эконом')*(Length*FuelCost*FuelConsumption)) EconomyIncome, " +
                "(FirstClassReserved*(SELECT ServiceCost FROM ServiceClass WHERE ClassName = 'Бизнес')*(Length*FuelCost*FuelConsumption)) FirstClassIncome, " +
                "(LuxReserved*(SELECT ServiceCost FROM ServiceClass WHERE ClassName = 'Люкс')*(Length*FuelCost*FuelConsumption)) LuxIncome " +
                "FROM Flight " +
                "JOIN Plane ON (Flight.PlaneID = Plane.PlaneID) " +
                "JOIN PlaneModel ON (Plane.ModelName = PlaneModel.ModelName) " +
                "JOIN Track ON (Flight.TrackID = Track.TrackID) " +
                "JOIN Airport ON (Track.StartAirport = Airport.AirportName) " +
                "JOIN City ON (Airport.CityName = City.CityName) " +
                "WHERE FlightID =" + flightCombo.getValue());

        String info = "";

        info += "ID :" +flightCombo.getValue() +"\n";
        resObj.next();
        info += "Total reserved:" + resObj.getInt("TotalReserved") +"\n";
        info += "Space :" + resObj.getInt("Space") +"\n";
        info += "EconomyReserved :" + resObj.getInt("EconomyReserved") +"\n";
        info += "FirstClassReserved :" + resObj.getInt("FirstClassReserved") +"\n";
        info += "LuxReserved :" + resObj.getInt("LuxReserved") +"\n";
        info += "EconomyTotal :" + resObj.getInt("EconomyTotal") +"\n";
        info += "FirstClassTotal :" + resObj.getInt("FirstClassTotal") +"\n";
        info += "LuxTotal :" + resObj.getInt("LuxTotal") +"\n";
        info += "FlightIncome :" + resObj.getInt("FlightIncome") +"\n";
        info += "EconomyIncome :" + resObj.getInt("EconomyIncome") +"\n";
        info += "FirstClassIncome :" + resObj.getInt("FirstClassIncome") +"\n";
        info += "LuxIncome :" + resObj.getInt("LuxIncome") +"\n";

        textArea.setText(info);
    }

    @FXML
    void back(ActionEvent event) {
        stage.setScene(this.scene);
        stage.show();
    }
}
