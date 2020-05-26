# SoundHive local client
SoundHive's Java client is an app that allows creators to upload their tracks to 
soundHive. It also allows them to see their stats in many ways, and it 
locally calculates the fingerprint for the track before uploading it.
Furthermore, this client is compatible with the use of plugins. This can be 
done by importing jar files that are developed for this project.

# Plugins
So far, plugins that can be installed consist on an interface along with its associated logic. The purpose is to extend functionnalities of the soundhive client.

## How to develop a plugin 
To create a plugin for this software, you have to implement the `IUiController` Interface to your controller, and implement the right methods along with it.

### Requirements
```
Maven
Java version 11
OpenJFX 12
```

### How ?
Clone this project in a repository, and add a module to it.
Set this project's CLI package as a dependence for your project.
Then, In this module, create a controller named PluginController.java that implements the project's interface IPluginUiController.
Add a view in the resources called ``View.FXML``. For the plugin to work, ensure that you do not set any controller for the view.
Add the JAR packaged plugin to SoundHive client's plugin directory, and run SoundHive client to test it.
