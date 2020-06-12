import com.jfoenix.controls.JFXButton;
import com.soundhive.gui.plugin.PluginController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class Controller extends PluginController {
    @FXML
    Label label;

    @FXML
    JFXButton button;

    public Controller() {
        System.out.println("went through controller");

    }

    @FXML
    private void initialize() {
        System.out.println("this is a test plugin");
        label.setText("This is SPARTAAAAAAAAAAAAAAAAA");
        button.setOnAction(e -> {
            System.out.println("you clicked the plugin button LOL");
        });
    }

    @Override
    protected void start() {

    }

    @Override
    public String getViewName() {
        return "TestView.fxml";
    }

    @Override
    public String getName() {
        return "second run time plugin";
    }

//    public FXMLLoader getFxml() {
//        final var fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/soundhive/TestView.fxml"));
//        final Parent view = fxmlLoader.load();
//        controllerConsumer.accept(fxmlLoader.getController());
//
//        return view;
//    }
}
