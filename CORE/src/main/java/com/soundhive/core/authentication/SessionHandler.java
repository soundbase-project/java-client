package com.soundhive.core.authentication;

import com.soundhive.core.conf.ConfHandler;
import com.soundhive.core.response.Response;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.io.*;
import java.util.Scanner;

import com.soundhive.core.response.Response.Status;

public class SessionHandler {


    private String token;
    private String name;
    private String username;
    private String email;

    private final String tokenDir;


    public SessionHandler(String tokenDir, String apiUrl) {
        this.tokenDir = tokenDir;

    }

    public Response<Void> openSession(final String username, final String password, final boolean stayConnected) {
        HttpResponse<JsonNode> res = Unirest.post("auth/login")
                .header("accept", "application/json")
                .field("username", username)
                .field("password", password)
                .asJson();

        switch (res.getStatus()) {
            case 201:
                this.token = res.getBody().getObject().getString("access_token");
                var userProfileReq = this.loadUserProfile();
                if (userProfileReq.getStatus() != Status.SUCCESS){
                    return new Response<>(Response.Status.UNKNOWN_ERROR);
                }
                if (stayConnected){
                    saveToken();
                }
                return new Response<>(Response.Status.SUCCESS);
            case 401:
                this.resetSessionValues();
                return new Response<>(Status.UNAUTHENTICATED);
            default:
                this.resetSessionValues();
                System.out.println(res.getStatus());
                return new Response<>(Status.UNKNOWN_ERROR);
        }
    }


    private void saveToken() {
        File target = new File(tokenDir);
        try {
            if (target.getParentFile().mkdirs()) {
                if (!(target.exists() || new File(tokenDir).createNewFile())){
                    System.err.println("Could not create token file.");
                }
            }
            FileWriter w = new FileWriter(tokenDir);
            w.write(this.token);
            w.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkForToken() {
        return new File(tokenDir).exists();
    }

    private String loadToken () {
        if (!new File(tokenDir).exists()) {
            return null;
        }
        StringBuilder foundToken = new StringBuilder();
        try {
            File myObj = new File(tokenDir);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                foundToken.append(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return  null;
        }
        return  foundToken.toString();
    }

    public Response<Void> loadUserProfile() {
        final String foundToken = loadToken();
        if (this.token != null && this.token.isBlank()){
            return new Response<>(Status.UNAUTHENTICATED );
        }
        HttpResponse<JsonNode> res = Unirest.get("profile")
                .header("accept", "application/json")
                .header("authorization", "Bearer " + foundToken)
                .asJson();


        switch (res.getStatus()){
            case 200:
                this.bindJSONObjectToAttributes(res.getBody().getObject());
                this.token = foundToken;
                return new Response<>(Status.SUCCESS);
            case 401:
                return new Response<>(Status.UNAUTHENTICATED);
            default:
                deleteToken();
                return new Response<>(Status.UNKNOWN_ERROR);
        }
    }

    private void deleteToken(){
        if (new File(tokenDir).delete()){
            System.out.println("Token file deleted.");
        }
    }

    private void bindJSONObjectToAttributes(JSONObject user) {
        this.username = user.getString("username");
        this.name = user.getString("name");
        this.email = user.getString("email");
    }


    private void resetSessionValues() {
        this.token = "";
        this.email = "";
        this.username = "";
        this.name = "";
    }


    public boolean isConnected() {
        return !(this.token == null  || this.token.isBlank());
    }

    public String getUsername(){
        return this.username;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
