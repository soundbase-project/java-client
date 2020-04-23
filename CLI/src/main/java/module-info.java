module com.soundbase {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.soundbase to javafx.fxml;
    exports com.soundbase;
}