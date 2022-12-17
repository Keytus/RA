module com.russianairports {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.russianairports to javafx.fxml;
    exports com.russianairports;
}