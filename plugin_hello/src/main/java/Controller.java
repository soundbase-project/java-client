import com.jfoenix.controls.JFXButton;
import com.soundhive.gui.plugin.PluginController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class Controller extends PluginController {
    //Thanks to the annotations, the following fields are instantiated automatically before the call of initialize()
    @FXML
    Label label;

    @FXML
    JFXButton button;

    //This method is called when the host app calls "FXMLloader.load();"
    @FXML
    private void initialize() {
        this.label.setText("Press the below button.");
        //getContext() == null;
    }

    //this is called just after context has been added.
    @Override
    protected void start() {
        button.setOnAction(e -> {
            getContext().getRouter().issueDialog("You clicked the button!");
        });
    }

    @Override
    public String getViewName() {
        return "View.fxml";
    }

    @Override
    public String getName() {
        return "Hello world Plugin";
    }

}
