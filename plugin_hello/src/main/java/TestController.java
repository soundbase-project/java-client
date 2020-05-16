import com.jfoenix.controls.JFXButton;
import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import com.soundhive.controllers.plugin.IPluginUiController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URL;

public class TestController implements IPluginUiController {
    private Router router;
    private SessionHandler session;

    @FXML
    Label label;

    @FXML
    JFXButton button;

    public TestController() {
        System.out.println("went through controller");

    }

    @FXML
    public void initialize() {
        System.out.println("this is a test plugin");
        label.setText("This is SPARTAAAAAAAAAAAAAAAAA");
        button.setOnAction(e -> {
            System.out.println("you clicked the plugin button LOL");
        });

    }
    @Override
    public void setContext(Router router, SessionHandler session) {
        this.router = router;
        this.session = session;
    }

    @Override
    public String getViewName() {
            return "TestView.fxml";
    }

    @Override
    public String getPluginName() {
        return "The funny plugin";
    }

//    public FXMLLoader getFxml() {
//        final var fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/soundhive/TestView.fxml"));
//        final Parent view = fxmlLoader.load();
//        controllerConsumer.accept(fxmlLoader.getController());
//
//        return view;
//    }
}
