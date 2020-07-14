package com.soundhive.gui;

import javafx.scene.image.Image;

public class ImageFetchingHandler {
    private final String baseURL;

    public ImageFetchingHandler(final String baseURL){
        this.baseURL = baseURL;
    }


    public Image getImage(String url) throws IllegalArgumentException {
        System.out.println(this.baseURL + url);
        return new Image(this.baseURL + url, false);
    }
}
