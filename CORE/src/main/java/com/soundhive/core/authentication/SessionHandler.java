package com.soundhive.core.authentication;

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

    private final UserProfileConsumer profileLoader;

    public SessionHandler(final String tokenDir,final  UserProfileConsumer profileLoader) {
        this.tokenDir = tokenDir;
        this.profileLoader = profileLoader;
        this.token = loadToken();

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
                    return new Response<>(Response.Status.UNKNOWN_ERROR, res.getStatusText());
                }
                if (stayConnected){
                    saveToken();
                }
                return new Response<>(Response.Status.SUCCESS, res.getStatusText());
            case 401:
                this.resetSessionValues();
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());
            default:
                this.resetSessionValues();
                System.out.println(res.getStatus());
                return new Response<>(Status.UNKNOWN_ERROR, res.getStatusText());
        }
    }


    public boolean checkForToken() {
        return new File(tokenDir).exists();
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
        if (this.token == null || this.token.isBlank()){
            return new Response<>(Status.UNAUTHENTICATED , "No usable token to request profile.");
        }
        HttpResponse<JsonNode> res;
        try {
            res = Unirest.get("profile")
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + this.token)
                    .asJson();
        }
        catch (Exception e) {
            return new Response<>(Status.CONNECTION_FAILED, e.getMessage());
        }



        switch (res.getStatus()){
            case 200:
                this.bindJSONObjectToAttributes(res.getBody().getObject());
                return new Response<>(Status.SUCCESS, res.getStatusText());
            case 401:
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());
            default:
                deleteToken();
                return new Response<>(Status.UNKNOWN_ERROR, res.getStatusText());
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

    public void setUserInfos(){
        this.profileLoader.accept(this.username);
    }
}
