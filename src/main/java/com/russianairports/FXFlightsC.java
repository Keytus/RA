package com.russianairports;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FXFlightsC
{
    String currentCustomerLogin;
    RouteInfo routeInfo;
    Statement stmtObj;
    Scene backScene;
    Stage stage;
    private FlightInfo selectedFlight = null;
    private Boolean isSelected = false;
    ArrayList<Float> mult = new ArrayList<>();

    public void setFlightsC(Statement stmtObj,String currentCustomerLogin,RouteInfo routeInfo, Scene backScene, Stage stage) throws SQLException {

        this.stmtObj = stmtObj;
        this.currentCustomerLogin = currentCustomerLogin;
        this.routeInfo = routeInfo;
        this.backScene = backScene;
        this.stage = stage;

        ResultSet resObj = stmtObj.executeQuery("SELECT ClassName,ServiceCost FROM ServiceClass");
        while (resObj.next()) {
            classCombo.getItems().add(resObj.getString("ClassName"));
            mult.add(resObj.getFloat("ServiceCost"));
        }
        classCombo.setValue(classCombo.getItems().get(0));

        String query = "SELECT flights.FlightID, flights.StartCity AS \"Start\", flights.EndCity AS \"End\", flights.Payment, flights.StartTime, flights.EndTime, flights.EconomyFree, flights.FirstClassFree, flights.LuxuryFree\n" +
                "FROM (\n" +
                "SELECT RouteID, flight.FlightID, start_city.CountryCode || '-' || start_city.CityName AS StartCity, end_city.CountryCode || '-' || end_city.CityName AS EndCity, \n" +
                "\tstart_city.FuelCost * FuelConsumption * track.Length AS Payment,\n" +
                "\tflight.DepartureTime AS StartTime, flight.ArrivalTime AS EndTime, EconomyTotal - EconomyReserved AS EconomyFree,\n" +
                "\tFirstClassTotal - FirstClassReserved AS FirstClassFree, LuxTotal - LuxReserved AS LuxuryFree\n" +
                "FROM Route r\n" +
                "INNER JOIN Track track ON (track.TrackID=r.Track1)\n" +
                "INNER JOIN Flight flight ON (flight.TrackID=track.TrackID)\n" +
                "INNER JOIN Airport start_airport ON (track.StartAirport=start_airport.AirportName)\n" +
                "INNER JOIN Airport end_airport ON (track.EndAirport=end_airport.AirportName)\n" +
                "INNER JOIN City start_city ON (start_airport.CityName=start_city.CityName AND start_airport.CountryCode=start_city.CountryCode)\n" +
                "INNER JOIN City end_city ON (end_airport.CityName=end_city.CityName AND end_airport.CountryCode=end_city.CountryCode)\n" +
                "INNER JOIN Plane plane ON (plane.PlaneID=flight.PlaneID)\n" +
                "INNER JOIN PlaneModel planeModel ON (plane.ModelName=planeModel.ModelName)\n" +
                "--WHERE flight.DepartureTime - CURRENT_TIMESTAMP > DAY(7)\n" +
                "UNION ALL \n" +
                "SELECT RouteID, flight.FlightID, start_city.CountryCode || '-' || start_city.CityName AS StartCity, end_city.CountryCode || '-' || end_city.CityName, \n" +
                "\tstart_city.FuelCost * FuelConsumption * ((LuxTotal - LuxReserved) * (SELECT ServiceCost FROM ServiceClass WHERE ClassName='Люкс') + \n" +
                "\t(EconomyTotal - EconomyReserved) * (SELECT ServiceCost FROM ServiceClass WHERE ClassName='Эконом') +\n" +
                "\t(FirstClassTotal - FirstClassReserved) * (SELECT ServiceCost FROM ServiceClass WHERE ClassName='Бизнес')),\n" +
                "\tflight.DepartureTime, flight.ArrivalTime, \n" +
                "\tEconomyTotal - EconomyReserved, FirstClassTotal - FirstClassReserved, LuxTotal - LuxReserved\n" +
                "FROM Route r\n" +
                "INNER JOIN Track track ON (track.TrackID=r.Track2)\n" +
                "INNER JOIN Flight flight ON (flight.TrackID=track.TrackID)\n" +
                "INNER JOIN Airport start_airport ON (track.StartAirport=start_airport.AirportName)\n" +
                "INNER JOIN Airport end_airport ON (track.EndAirport=end_airport.AirportName)\n" +
                "INNER JOIN City start_city ON (start_airport.CityName=start_city.CityName AND start_airport.CountryCode=start_city.CountryCode)\n" +
                "INNER JOIN City end_city ON (end_airport.CityName=end_city.CityName AND end_airport.CountryCode=end_city.CountryCode)\n" +
                "INNER JOIN Plane plane ON (plane.PlaneID=flight.PlaneID)\n" +
                "INNER JOIN PlaneModel planeModel ON (plane.ModelName=planeModel.ModelName)\n" +
                "--WHERE flight.DepartureTime - CURRENT_TIMESTAMP > DAY(7)\n" +
                "UNION ALL \n" +
                "SELECT RouteID, flight.FlightID, start_city.CountryCode || '-' || start_city.CityName, end_city.CountryCode || '-' || end_city.CityName, \n" +
                "\tstart_city.FuelCost * FuelConsumption * ((LuxTotal - LuxReserved) * (SELECT ServiceCost FROM ServiceClass WHERE ClassName='Люкс') + \n" +
                "\t(EconomyTotal - EconomyReserved) * (SELECT ServiceCost FROM ServiceClass WHERE ClassName='Эконом') +\n" +
                "\t(FirstClassTotal - FirstClassReserved) * (SELECT ServiceCost FROM ServiceClass WHERE ClassName='Бизнес')),\n" +
                "\tflight.DepartureTime, flight.ArrivalTime, \n" +
                "\tEconomyTotal - EconomyReserved, FirstClassTotal - FirstClassReserved, LuxTotal - LuxReserved\n" +
                "FROM Route r\n" +
                "INNER JOIN Track track ON (track.TrackID=r.Track3)\n" +
                "INNER JOIN Flight flight ON (flight.TrackID=track.TrackID)\n" +
                "INNER JOIN Airport start_airport ON (track.StartAirport=start_airport.AirportName)\n" +
                "INNER JOIN Airport end_airport ON (track.EndAirport=end_airport.AirportName)\n" +
                "INNER JOIN City start_city ON (start_airport.CityName=start_city.CityName AND start_airport.CountryCode=start_city.CountryCode)\n" +
                "INNER JOIN City end_city ON (end_airport.CityName=end_city.CityName AND end_airport.CountryCode=end_city.CountryCode)\n" +
                "INNER JOIN Plane plane ON (plane.PlaneID=flight.PlaneID)\n" +
                "INNER JOIN PlaneModel planeModel ON (plane.ModelName=planeModel.ModelName)\n" +
                "--WHERE flight.DepartureTime - CURRENT_TIMESTAMP > DAY(7)\n" +
                ") flights\n" +
                "WHERE flights.RouteId ="+ routeInfo.getRouteID();

        resObj = stmtObj.executeQuery(query);
        while (resObj.next()) {
            PrData.add(new FlightInfo(resObj.getInt("FlightID"),
                    resObj.getString("Start"),
                    resObj.getString("End"),
                    resObj.getInt("Payment"),
                    resObj.getString("StartTime"),
                    resObj.getString("EndTime"),
                    resObj.getInt("EconomyFree"),
                    resObj.getInt("FirstClassFree"),
                    resObj.getInt("LuxuryFree")));
        }

        // устанавливаем тип и значение которое должно хранится в колонке
        startColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, String>("Start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, String>("End"));
        costColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, Integer>("Cost"));
        economFreeColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, Integer>("EconomFree"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, String>("StartTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, String>("EndTime"));
        firstClassFreeColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, Integer>("FirstClassFree"));
        luxuryFreeColumn.setCellValueFactory(new PropertyValueFactory<FlightInfo, Integer>("LuxuryFree"));

        // заполняем таблицу данными
        Tabl1.setItems(PrData);

    }

    public ObservableList<FlightInfo> PrData = FXCollections.observableArrayList();

    @FXML
    private TableView<FlightInfo> Tabl1;

    @FXML
    private TableColumn<FlightInfo, Integer> costColumn;

    @FXML
    private TableColumn<FlightInfo, Integer> economFreeColumn;

    @FXML
    private TableColumn<FlightInfo, String> endColumn;

    @FXML
    private TableColumn<FlightInfo, String> endTimeColumn;

    @FXML
    private TableColumn<FlightInfo, Integer> firstClassFreeColumn;

    @FXML
    private TableColumn<FlightInfo, Integer> luxuryFreeColumn;

    @FXML
    private TableColumn<FlightInfo, String> startColumn;

    @FXML
    private TableColumn<FlightInfo, String> startTimeColumn;

    @FXML
    private ComboBox<String> classCombo;

    @FXML
    private TextField loginW;

    @FXML
    private Text messageText;

    @FXML
    void Add1(ActionEvent event) throws SQLException {

        if (isSelected){
            String login;

            if(loginW.getText().equals("") || loginW.getText() == null){
                login = currentCustomerLogin;
            }
            else{
                login = loginW.getText();
            }

            ResultSet resObj = stmtObj.executeQuery("SELECT Login FROM Customer");
            while (resObj.next()) {
                if(resObj.getString("Login").equals(login)){
                    Float preCost = selectedFlight.getCost().floatValue();
                    switch (classCombo.getValue()) {
                        case "Эконом" -> preCost *= mult.get(2);
                        case "Бизнес" -> preCost *= mult.get(0);
                        case "Люкс" -> preCost *= mult.get(1);
                    }
                    int resCost = Math.round(preCost);
                    FXBucketC.tickets.add(new Ticket(selectedFlight.getFlightID(),
                            classCombo.getValue(),
                            resCost,
                            selectedFlight.getStart(),
                            selectedFlight.getEnd(),
                            login,
                            selectedFlight.getStartTime()));
                    messageText.setText("Успешно");
                    break;
                }
                else {
                    messageText.setText("Неправильный логин");
                }
            }
        }
        else {
            messageText.setText("Не выбран рейс");
        }
    }

    @FXML
    void back(ActionEvent event) {
        stage.setScene(this.backScene);
        stage.show();
    }

    @FXML
    void initialize(MouseEvent event) {
        TableView.TableViewSelectionModel<FlightInfo> selectionModel = Tabl1.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<FlightInfo>(){

            @Override
            public void changed(ObservableValue<? extends FlightInfo> observableValue, FlightInfo flightInfo, FlightInfo t1) {
                if(t1 != null) {selectedFlight = t1; isSelected = true;}
                else isSelected = false;
            }
        });
    }

}
