[![time tracker](https://wakatime.com/badge/github/soundhive/java-client.svg)](https://wakatime.com/badge/github/soundhive/java-client)
# SoundHive local client
SoundHive's Java client is an app that allows creators to upload their tracks to 
soundHive. It also allows them to see their stats in many ways.
Furthermore, the GUI client is compatible with the use of plugins. This can be 
done by importing jar files that are developed specifically for this project.

It is available in both a CLI and a GUI version, both depending on the CORE package.

# CLI
The command line interface version does not allow use of plugins. 
However, it is cross-platform, compared to the GUI client.

To learn how to use the CLI, launch it with the option `--help` or `-h`.

# GUI
Soundhive's GUI is powered by javaFX.

It is ready for the use of plugins. 

[Read more about it here](https://github.com/soundhive/java-client/tree/master/GUI)

# CORE
`CLI` and `GUI` both rely on the `CORE` package.
The `CORE` package contains a generic API communication system, wrapped over the Unirest Library, using `Response<T>` as a return object.

`CORE` also manages the user's session along with the associated data for it, and the Conf file's parsing and handling logic.

 

