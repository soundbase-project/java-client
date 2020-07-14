package com.soundhive.gui.controllers;

import com.soundhive.gui.Context;

public abstract class Controller {
    private Context context;

    public void setContextAndStart(Context context){
        this.context = context;
        start();
    }

    protected Context getContext(){
        return this.context;
    }

    protected abstract void start();


}
