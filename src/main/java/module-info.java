module com.example.expracticointerfaces {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires lombok;

    opens com.example.expracticointerfaces to javafx.fxml;
    exports com.example.expracticointerfaces;
}