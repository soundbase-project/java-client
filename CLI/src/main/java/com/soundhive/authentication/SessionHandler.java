package com.soundhive.authentication;

import com.soundhive.controllers.StatsController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class SessionHandler {
    public enum LoginStatus{
        WRONG_PASSWORD, CONNECTION_ERROR, SUCCESS
    }

    private String token;
    private String name;
    private String username;
    private String email;
    private boolean isActive;

    private final StringProperty lbSession;

    public SessionHandler(StringProperty lbSession) {
        this.lbSession = lbSession;
    }

    public LoginStatus openSession(String username, String password) {
        HttpResponse<JsonNode> res = Unirest.post("auth/login")
                .header("accept", "application/json")
                .field("username", username)
                .field("password", password)
                .asJson();
        JSONObject user = res.getBody().getObject();

        switch (res.getStatus()) {
            case 201:
                this.username = user.getString("username");
                this.name = user.getString("name");
                this.token = user.getString("access_token");
                this.email = user.getString("email");
                this.isActive = user.getBoolean("isActive");

                return LoginStatus.SUCCESS;
            case 401:
                return LoginStatus.WRONG_PASSWORD;
            default:
                System.out.println(res.getStatus());
                System.out.println("gne sa march pa");
                return LoginStatus.CONNECTION_ERROR;
        }
    }

    public void updateWitness(){
        this.lbSession.setValue(this.username);
    }



}
