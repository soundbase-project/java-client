module com.soundbase {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.soundhive to javafx.fxml;
    exports com.soundhive;
}