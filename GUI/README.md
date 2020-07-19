# SoundHive's GUI client
This is powered by javaFX combined with the [JFoenix](https://github.com/jfoenixadmin/JFoenix) components.

The requests are made using [Unirest](https://kong.github.io/unirest-java/). Read more about it at the root of this 
project.

## How to run ? 
### Requirements
```
Maven
Java version 11
```

Make sur you have Maven installed, along with at least JDK version 11.

Keep in mind that Maven will create shaded jars for the app to be able to run independently, which also implies that the 
executables only work on the operating system they were compiled in.

## Plugins
So far, plugins that can be installed consist on an interface along with its associated logic. The purpose is to extend 
functionalities of the soundhive client.
To add a plugin at run-time, go to the settings, and click the "add" button, and select the associated jar for your 
plugin. If the plugin is valid, it will be usable right away.
You can delete plugins on runtime as well.

### How to develop a plugin 
To create a plugin for this software, you have to inherit fom the `PluginController` in your controller, along with the 
methods that go with it.
To access the database in general , you can use the CORE package as a dependence and its functions.

For all kind of actions, you can use the inherited method `getContext()` that gives you a context containing several 
tools including the session, access to the configuration file, to the other plugins, user notification methods such as dialogs and snackbar messages, logging, and other objects.

The lifecycle of a view using jfx consists in the following order : `Controller()` -> `initialize()` -> `start()`

- `Controller()` constructor will be called by a third party library, out of our control. It may be better not to declare any.
- `initialize()` will occur after instantiations of the components by FXML using annotations
- `start()` is issued from the specific inheritance system of SoundHive. Before this lifecycle event, `getContext()` will return null. If you intend to use the context, you can do it from the `start()` call on.

For the injection system to find your plugin, please call the controller class `Controller`.

- `public abstract String getName();` in this Function, you are supposed to return the name that the users will see on the UI for your plugin.

- `public abstract String getViewName();` in this function, you are supposed to return the name of the resource that is the main FXML file for your plugin.

### Simple plugin example

- Controller.java :
```java
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
```

-  View.fxml :
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import com.jfoenix.controls.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<AnchorPane prefHeight="600.0" prefWidth="1080.0" xmlns="http://javafx.com/javafx/10.0.2-internal" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Label fx:id="label" layoutX="433.0" layoutY="225.0" text="Placeholder." textFill="WHITE" />
      <JFXButton fx:id="button" layoutX="512.0" layoutY="300.0" mnemonicParsing="false" text="Placeholder." textFill="WHITE" />
   </children>
</AnchorPane>

```




