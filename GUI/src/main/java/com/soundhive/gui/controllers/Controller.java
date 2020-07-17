package com.soundhive.gui.controllers;

import com.soundhive.core.response.Response;
import com.soundhive.gui.Context;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;

import java.util.function.Consumer;

public abstract class Controller {
    private Context context;

    public void setContextAndStart(Context context) {
        this.context = context;
        start();
    }

    protected Context getContext() {
        return this.context;
    }

    protected abstract void start();

//    protected void handleResponse(WorkerStateEvent event, Consumer<Response<?>> onSuccess) {
//        var response = (Response<?>) event.getSource().getValue();
//        switch (response.getStatus()) {
//            case SUCCESS:
//                onSuccess.accept(response);
//                break;
//
//            case CONNECTION_FAILED:
//                getContext().getRouter().issueDialog("Server unreachable. Please check your internet connection.");
//                break;
//
//            case UNAUTHENTICATED:
//                if (autoLogin) {
//                    getContext().getRouter().issueMessage("Could not login Automatically.");
//                } else {
//                    getContext().getRouter().issueMessage("Wrong password or username.");
//                }
//                getContext().getSession().destroySession();
//                break;
//
//            case INTERNAL_ERROR:
//                getContext().getRouter().issueDialog("An error occurred.");
//                getContext().log(response.getMessage());
//                if (response.getException() != null) {
//                    getContext().logException((response.getException()));
//                }
//                break;
//        }
//    }


}
