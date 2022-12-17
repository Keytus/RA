package com.russianairports;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.russianairports.ExceptionLogger.LogException;

public class FXAdminC{
    Statement stmtObj;
    Scene backScene;
    Scene currentScene;
    Stage stage;
    private boolean isSelected = false;
    private RouteInfo selectedRoute;
    public void setADC(Statement stmtObj, Scene backScene,Scene currentScene, Stage stage) {
        this.stmtObj = stmtObj;
        this.backScene = backScene;
        this.currentScene = currentScene;
        this.stage = stage;
        try{
            ResultSet resObj = stmtObj.executeQuery("WITH subcte (\n" +
                    "    routeid,\n" +
                    "    last\n" +
                    ") AS (\n" +
                    "    SELECT\n" +
                    "        routeid,\n" +
                    "        CASE\n" +
                    "            WHEN last_city_3.cityname IS NOT NULL THEN\n" +
                    "                last_city_3.cityname\n" +
                    "            WHEN last_city_2.cityname IS NOT NULL THEN\n" +
                    "                last_city_2.cityname\n" +
                    "            ELSE\n" +
                    "                last_city_1.cityname\n" +
                    "        END AS last\n" +
                    "    FROM\n" +
                    "        route   r\n" +
                    "        LEFT OUTER JOIN track   last_track_3 ON ( r.track3 = last_track_3.trackid )\n" +
                    "        LEFT OUTER JOIN airport last_airport_3 ON ( last_track_3.endairport = last_airport_3.airportname )\n" +
                    "        LEFT OUTER JOIN city    last_city_3 ON ( last_airport_3.cityname = last_city_3.cityname\n" +
                    "                                              AND last_airport_3.countrycode = last_city_3.countrycode )\n" +
                    "        LEFT OUTER JOIN track   last_track_2 ON ( r.track2 = last_track_2.trackid )\n" +
                    "        LEFT OUTER JOIN airport last_airport_2 ON ( last_track_2.endairport = last_airport_2.airportname )\n" +
                    "        LEFT OUTER JOIN city    last_city_2 ON ( last_airport_2.cityname = last_city_2.cityname\n" +
                    "                                              AND last_airport_2.countrycode = last_city_2.countrycode )\n" +
                    "        LEFT OUTER JOIN track   last_track_1 ON ( r.track1 = last_track_1.trackid )\n" +
                    "        LEFT OUTER JOIN airport last_airport_1 ON ( last_track_1.endairport = last_airport_1.airportname )\n" +
                    "        LEFT OUTER JOIN city    last_city_1 ON ( last_airport_1.cityname = last_city_1.cityname\n" +
                    "                                              AND last_airport_1.countrycode = last_city_1.countrycode )\n" +
                    ")\n" +
                    "SELECT\n" +
                    "    starts.routeid,\n" +
                    "    \"START\",\n" +
                    "    last,\n" +
                    "    transits\n" +
                    "FROM\n" +
                    "         (\n" +
                    "        SELECT DISTINCT\n" +
                    "            r.routeid,\n" +
                    "            first_city.cityname AS \"START\"\n" +
                    "        FROM\n" +
                    "                 route r\n" +
                    "            INNER JOIN track   first_track ON ( r.track1 = first_track.trackid )\n" +
                    "            INNER JOIN airport first_airport ON ( first_track.startairport = first_airport.airportname )\n" +
                    "            INNER JOIN city    first_city ON ( first_airport.cityname = first_city.cityname\n" +
                    "                                            AND first_city.countrycode = first_city.countrycode )\n" +
                    "    ) starts\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT\n" +
                    "            subcte.routeid,\n" +
                    "            last,\n" +
                    "            transits\n" +
                    "        FROM\n" +
                    "                 subcte\n" +
                    "            INNER JOIN (\n" +
                    "                SELECT\n" +
                    "                    routeid,\n" +
                    "                    COUNT(*) - 1 transits\n" +
                    "                FROM\n" +
                    "                    (\n" +
                    "                        SELECT DISTINCT\n" +
                    "                            r.routeid,\n" +
                    "                            last_city.cityname,\n" +
                    "                            ROW_NUMBER()\n" +
                    "                            OVER(\n" +
                    "                                ORDER BY\n" +
                    "                                    r.routeid\n" +
                    "                            ) AS indexnum\n" +
                    "                        FROM\n" +
                    "                                 route r\n" +
                    "                            INNER JOIN track   last_track ON ( r.track3 = last_track.trackid )\n" +
                    "                            INNER JOIN airport last_airport ON ( last_track.endairport = last_airport.airportname )\n" +
                    "                            INNER JOIN city    last_city ON ( last_airport.cityname = last_city.cityname\n" +
                    "                                                           AND last_airport.countrycode = last_city.countrycode )\n" +
                    "                        UNION ALL\n" +
                    "                        SELECT DISTINCT\n" +
                    "                            r.routeid,\n" +
                    "                            last_city.cityname,\n" +
                    "                            ROW_NUMBER()\n" +
                    "                            OVER(\n" +
                    "                                ORDER BY\n" +
                    "                                    r.routeid\n" +
                    "                            ) AS indexnum\n" +
                    "                        FROM\n" +
                    "                                 route r\n" +
                    "                            INNER JOIN track   last_track ON ( r.track2 = last_track.trackid )\n" +
                    "                            INNER JOIN airport last_airport ON ( last_track.endairport = last_airport.airportname )\n" +
                    "                            INNER JOIN city    last_city ON ( last_airport.cityname = last_city.cityname\n" +
                    "                                                           AND last_airport.countrycode = last_city.countrycode )\n" +
                    "                        UNION ALL\n" +
                    "                        SELECT DISTINCT\n" +
                    "                            r.routeid,\n" +
                    "                            last_city.cityname,\n" +
                    "                            ROW_NUMBER()\n" +
                    "                            OVER(\n" +
                    "                                ORDER BY\n" +
                    "                                    r.routeid\n" +
                    "                            ) AS indexnum\n" +
                    "                        FROM\n" +
                    "                                 route r\n" +
                    "                            INNER JOIN track   last_track ON ( r.track1 = last_track.trackid )\n" +
                    "                            INNER JOIN airport last_airport ON ( last_track.endairport = last_airport.airportname )\n" +
                    "                            INNER JOIN city    last_city ON ( last_airport.cityname = last_city.cityname\n" +
                    "                                                           AND last_airport.countrycode = last_city.countrycode )\n" +
                    "                    )\n" +
                    "                GROUP BY\n" +
                    "                    routeid\n" +
                    "            ) transitssubs ON ( subcte.routeid = transitssubs.routeid )\n" +
                    "    ) ends ON ( ends.routeid = starts.routeid )\n" +
                    "ORDER BY\n" +
                    "    starts.routeid");
            while (resObj.next()) {
                PrData.add(new RouteInfo(resObj.getInt("RouteID"),resObj.getString("Start"),resObj.getString("Last"),resObj.getInt("Transits")));
            }
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
        // устанавливаем тип и значение которое должно хранится в колонке
        startColumn.setCellValueFactory(new PropertyValueFactory<RouteInfo, String>("Start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<RouteInfo, String>("End"));
        tranzColumn.setCellValueFactory(new PropertyValueFactory<RouteInfo, Integer>("tranzCount"));

        // заполняем таблицу данными
        Tabl1.setItems(PrData);
    }
    public ObservableList<RouteInfo> PrData = FXCollections.observableArrayList();

    @FXML
    private Text logT;

    @FXML
    private TableView<RouteInfo> Tabl1;

    @FXML
    private TableColumn<RouteInfo, String> startColumn;

    @FXML
    private TableColumn<RouteInfo, String> endColumn;

    @FXML
    private TableColumn<RouteInfo, Integer> tranzColumn;

    @FXML
    private Button add;

    @FXML
    private Button adminBtn;

    @FXML
    void Delete(ActionEvent event) throws SQLException {
        if (isSelected){
            stmtObj.executeUpdate("DELETE FROM Route " +
                    "WHERE Route.RouteID="+ selectedRoute.getRouteID());

            Tabl1.getItems().clear();

            ResultSet resObj = stmtObj.executeQuery("WITH subcte (\n" +
                    "    routeid,\n" +
                    "    last\n" +
                    ") AS (\n" +
                    "    SELECT\n" +
                    "        routeid,\n" +
                    "        CASE\n" +
                    "            WHEN last_city_3.cityname IS NOT NULL THEN\n" +
                    "                last_city_3.cityname\n" +
                    "            WHEN last_city_2.cityname IS NOT NULL THEN\n" +
                    "                last_city_2.cityname\n" +
                    "            ELSE\n" +
                    "                last_city_1.cityname\n" +
                    "        END AS last\n" +
                    "    FROM\n" +
                    "        route   r\n" +
                    "        LEFT OUTER JOIN track   last_track_3 ON ( r.track3 = last_track_3.trackid )\n" +
                    "        LEFT OUTER JOIN airport last_airport_3 ON ( last_track_3.endairport = last_airport_3.airportname )\n" +
                    "        LEFT OUTER JOIN city    last_city_3 ON ( last_airport_3.cityname = last_city_3.cityname\n" +
                    "                                              AND last_airport_3.countrycode = last_city_3.countrycode )\n" +
                    "        LEFT OUTER JOIN track   last_track_2 ON ( r.track2 = last_track_2.trackid )\n" +
                    "        LEFT OUTER JOIN airport last_airport_2 ON ( last_track_2.endairport = last_airport_2.airportname )\n" +
                    "        LEFT OUTER JOIN city    last_city_2 ON ( last_airport_2.cityname = last_city_2.cityname\n" +
                    "                                              AND last_airport_2.countrycode = last_city_2.countrycode )\n" +
                    "        LEFT OUTER JOIN track   last_track_1 ON ( r.track1 = last_track_1.trackid )\n" +
                    "        LEFT OUTER JOIN airport last_airport_1 ON ( last_track_1.endairport = last_airport_1.airportname )\n" +
                    "        LEFT OUTER JOIN city    last_city_1 ON ( last_airport_1.cityname = last_city_1.cityname\n" +
                    "                                              AND last_airport_1.countrycode = last_city_1.countrycode )\n" +
                    ")\n" +
                    "SELECT\n" +
                    "    starts.routeid,\n" +
                    "    \"START\",\n" +
                    "    last,\n" +
                    "    transits\n" +
                    "FROM\n" +
                    "         (\n" +
                    "        SELECT DISTINCT\n" +
                    "            r.routeid,\n" +
                    "            first_city.cityname AS \"START\"\n" +
                    "        FROM\n" +
                    "                 route r\n" +
                    "            INNER JOIN track   first_track ON ( r.track1 = first_track.trackid )\n" +
                    "            INNER JOIN airport first_airport ON ( first_track.startairport = first_airport.airportname )\n" +
                    "            INNER JOIN city    first_city ON ( first_airport.cityname = first_city.cityname\n" +
                    "                                            AND first_city.countrycode = first_city.countrycode )\n" +
                    "    ) starts\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT\n" +
                    "            subcte.routeid,\n" +
                    "            last,\n" +
                    "            transits\n" +
                    "        FROM\n" +
                    "                 subcte\n" +
                    "            INNER JOIN (\n" +
                    "                SELECT\n" +
                    "                    routeid,\n" +
                    "                    COUNT(*) - 1 transits\n" +
                    "                FROM\n" +
                    "                    (\n" +
                    "                        SELECT DISTINCT\n" +
                    "                            r.routeid,\n" +
                    "                            last_city.cityname,\n" +
                    "                            ROW_NUMBER()\n" +
                    "                            OVER(\n" +
                    "                                ORDER BY\n" +
                    "                                    r.routeid\n" +
                    "                            ) AS indexnum\n" +
                    "                        FROM\n" +
                    "                                 route r\n" +
                    "                            INNER JOIN track   last_track ON ( r.track3 = last_track.trackid )\n" +
                    "                            INNER JOIN airport last_airport ON ( last_track.endairport = last_airport.airportname )\n" +
                    "                            INNER JOIN city    last_city ON ( last_airport.cityname = last_city.cityname\n" +
                    "                                                           AND last_airport.countrycode = last_city.countrycode )\n" +
                    "                        UNION ALL\n" +
                    "                        SELECT DISTINCT\n" +
                    "                            r.routeid,\n" +
                    "                            last_city.cityname,\n" +
                    "                            ROW_NUMBER()\n" +
                    "                            OVER(\n" +
                    "                                ORDER BY\n" +
                    "                                    r.routeid\n" +
                    "                            ) AS indexnum\n" +
                    "                        FROM\n" +
                    "                                 route r\n" +
                    "                            INNER JOIN track   last_track ON ( r.track2 = last_track.trackid )\n" +
                    "                            INNER JOIN airport last_airport ON ( last_track.endairport = last_airport.airportname )\n" +
                    "                            INNER JOIN city    last_city ON ( last_airport.cityname = last_city.cityname\n" +
                    "                                                           AND last_airport.countrycode = last_city.countrycode )\n" +
                    "                        UNION ALL\n" +
                    "                        SELECT DISTINCT\n" +
                    "                            r.routeid,\n" +
                    "                            last_city.cityname,\n" +
                    "                            ROW_NUMBER()\n" +
                    "                            OVER(\n" +
                    "                                ORDER BY\n" +
                    "                                    r.routeid\n" +
                    "                            ) AS indexnum\n" +
                    "                        FROM\n" +
                    "                                 route r\n" +
                    "                            INNER JOIN track   last_track ON ( r.track1 = last_track.trackid )\n" +
                    "                            INNER JOIN airport last_airport ON ( last_track.endairport = last_airport.airportname )\n" +
                    "                            INNER JOIN city    last_city ON ( last_airport.cityname = last_city.cityname\n" +
                    "                                                           AND last_airport.countrycode = last_city.countrycode )\n" +
                    "                    )\n" +
                    "                GROUP BY\n" +
                    "                    routeid\n" +
                    "            ) transitssubs ON ( subcte.routeid = transitssubs.routeid )\n" +
                    "    ) ends ON ( ends.routeid = starts.routeid )\n" +
                    "ORDER BY\n" +
                    "    starts.routeid");
            while (resObj.next()) {
                PrData.add(new RouteInfo(resObj.getInt("RouteID"),resObj.getString("Start"),resObj.getString("Last"),resObj.getInt("Transits")));
            }

            // устанавливаем тип и значение которое должно хранится в колонке
            startColumn.setCellValueFactory(new PropertyValueFactory<RouteInfo, String>("Start"));
            endColumn.setCellValueFactory(new PropertyValueFactory<RouteInfo, String>("End"));
            tranzColumn.setCellValueFactory(new PropertyValueFactory<RouteInfo, Integer>("tranzCount"));
            // заполняем таблицу данными
            Tabl1.setItems(PrData);
        }

    }

    @FXML
    void AddRoute(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXAddRoute.fxml"));
        try {
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXAddRouteC intC = loader.getController();
            intC.setAddRoute(stmtObj,this.currentScene, stage);
        }
        catch (Exception exception) {
            LogException(exception, stmtObj);
        }
    }

    @FXML
    void AdminFunc(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MAINDOOR.class.getResource("FXAdminFunc.fxml"));
        try {
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXAdminFuncC intC = loader.getController();
            intC.setAdminFunc(stmtObj,this.currentScene, stage);
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

    @FXML
    void initialize() {
        TableView.TableViewSelectionModel<RouteInfo> selectionModel = Tabl1.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<RouteInfo>(){
            @Override
            public void changed(ObservableValue<? extends RouteInfo> observableValue, RouteInfo routeInfo, RouteInfo t1) {
                if(t1 != null) {selectedRoute = t1; isSelected = true;}
                else isSelected = false;
            }
        });

    }

}

