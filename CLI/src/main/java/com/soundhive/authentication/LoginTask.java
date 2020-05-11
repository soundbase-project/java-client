package com.soundhive.authentication;

import javafx.concurrent.Task;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;


public class LoginTask extends Task<JSONObject> {

    private final String username;
    private final String password;

    public LoginTask(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    protected JSONObject call() {
        HttpResponse<JsonNode> res = Unirest.post("auth/login")
                .header("accept", "application/json")
                .field("username", username)
                .field("password", password)
                .asJson();

        return res.getBody().getObject();
    }
}
