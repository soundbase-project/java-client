module com.soundbase {
    requires javafx.controls;
    requires javafx.fxml;
    requires unirest.java;
    requires com.jfoenix;

    opens com.soundhive to javafx.fxml;
    exports com.soundhive;
}