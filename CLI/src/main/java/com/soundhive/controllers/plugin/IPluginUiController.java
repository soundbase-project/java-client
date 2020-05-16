package com.soundhive.controllers.plugin;

import com.soundhive.Router;
import com.soundhive.authentication.SessionHandler;
import com.soundhive.controllers.IUiController;

import java.net.URL;

public interface IPluginUiController extends IUiController {
    void setContext(Router router, SessionHandler session);

    String getViewName();

    String getPluginName();

}
