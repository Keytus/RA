package com.russianairports;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class FXBucketC extends FXbasic{
    static ArrayList <Ticket> tickets = new ArrayList<>();
    String currentCustomerLogin;
    Statement stmtObj;
    Ticket selectTicket;
    private Boolean Log1 = false, isSelected = false;
    public void setBuckC(Statement stmtObj,String currentCustomerLogin, Scene scene, Stage stage) {
        this.stmtObj = stmtObj;
        this.currentCustomerLogin = currentCustomerLogin;
        this.scene = scene;
        this.stage = stage;

        SetTable();
    }

    public ObservableList<Ticket> PrData = FXCollections.observableArrayList();

    @FXML
    private TableView<Ticket> Tabl1;

    @FXML
    private TableColumn<Ticket, String> classColumn;

    @FXML
    private TableColumn<Ticket, Integer> costColumn;

    @FXML
    private TableColumn<Ticket, String> endColumn;

    @FXML
    private Text logT;

    @FXML
    private TableColumn<Ticket, String> loginColumn;

    @FXML
    private TableColumn<Ticket, String> startColumn;

    @FXML
    private TableColumn<Ticket, String> startTimeColumn;

    @FXML
    void Add1(ActionEvent event) throws SQLException {
        if (isSelected) {

            //((SELECT ClassName FROM ServiceClass WHERE ClassName='Эконом'),(SELECT Login FROM Customer WHERE Login='Зефир'),(SELECT FlightID FROM Flight WHERE FlightID=1))
            String query = "INSERT INTO Ticket(SEATCLASS, LOGIN, FLIGHTID) VALUES ('"+
                    selectTicket.getServiceClass()+ "','" +
                    selectTicket.getLogin() + "'," +
                    selectTicket.getFlightID() + ")";

            stmtObj.executeUpdate(query);

            query = "UPDATE Plane ";
            switch (selectTicket.getServiceClass())
            {
                case "Эконом":
                    query += "SET EconomyReserved = EconomyReserved + 1 ";
                    break;
                case "Бизнес":
                    query += "SET FirstClassReserved = FirstClassReserved + 1 ";
                    break;
                case "Люкс":
                    query += "SET LuxReserved = LuxReserved + 1 ";
                    break;
            }
            query +="WHERE PLaneID =(SELECT PlaneID\n" +
                    "                    FROM Flight\n" +
                    "                    WHERE FlightID =" + selectTicket.getFlightID() + ")";

            stmtObj.executeUpdate(query);

            tickets.remove(selectTicket);

            Tabl1.getItems().clear();

            SetTable();
        }
        else logT.setText("выберите товар");
    }

    @FXML
    void Add2(ActionEvent event) {
        if (isSelected) {

            tickets.remove(selectTicket);
            logT.setText("Заказ успешно отменен!");

            Tabl1.getItems().clear();

            SetTable();
        }
        else logT.setText("выберите товар");
    }

    @FXML
    void back(ActionEvent event) {
        stage.setScene(this.scene);
        stage.show();
    }

    @FXML
    void initialize() {
        TableView.TableViewSelectionModel<Ticket> selectionModel = Tabl1.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Ticket>(){
            @Override
            public void changed(ObservableValue<? extends Ticket> observableValue, Ticket ticket, Ticket t1) {
                if(t1 != null) {selectTicket = t1; isSelected = true;}
                else isSelected = false;
            }
    });
    }

    void SetTable()
    {
        PrData.removeAll();
        tickets.forEach(ticket -> PrData.add(ticket));

        // устанавливаем тип и значение которое должно хранится в колонке
        classColumn.setCellValueFactory(new PropertyValueFactory<Ticket, String>("ServiceClass"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("Cost"));
        endColumn.setCellValueFactory(new PropertyValueFactory<Ticket, String>("End"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<Ticket, String>("Login"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Ticket, String>("Start"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Ticket, String>("StartTime"));

        // заполняем таблицу данными
        Tabl1.setItems(PrData);
    }

}

