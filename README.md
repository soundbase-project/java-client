# Soundhive local client
Soundhive's Java client is an app that allows creators to upload their tracks to soundhive. It also allows them to see their stats in many different ways, and it locally calculates the fingerprint for the track before uploading it.
Furthermore, this client is compatible with the use of plugins. This can be done by importing jar files that are developped for this project

# Plugins
So far, plugins that can be installed consist on an interface along with its associated logic. The purpose is to extend functionnalities of the soundhive client.

## How to develop a plugin 
To create a plugin for this software, you have to extend this plugin's controller from the abstract `Controller` class, and implement the right methods along with it.

### Requirements
```
Maven
Java version 11
Javafx 9
```

### How ?
Clone this project in a repository, and add a module to it.
Then, In this module, create a controller named PluginController.java that extends the project's abstract Controller class.
Add a view in the ressources called View.FXML. For the plugin to work, ensure that you do not set any controller onto the view.
Add the JAR packaged plugin to SoundHive client's plugin directory to test it.
