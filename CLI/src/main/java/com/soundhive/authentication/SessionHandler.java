package com.soundhive.authentication;

import com.soundhive.Globals;
import com.soundhive.controllers.StatsController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.io.*;
import java.util.Scanner;

public class SessionHandler {
    public enum LoginStatus{
        UNAUTHORIZED, CONNECTION_ERROR, SUCCESS
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

    public LoginStatus openSession(final String username,final String password, final boolean stayConnected) {
        HttpResponse<JsonNode> res = Unirest.post("auth/login")
                .header("accept", "application/json")
                .field("username", username)
                .field("password", password)
                .asJson();

        switch (res.getStatus()) {
            case 201:
                bindJSONObjectToAttributes(res.getBody().getObject());
                if (stayConnected){
                    saveToken();
                }
                return LoginStatus.SUCCESS;
            case 401:
                return LoginStatus.UNAUTHORIZED;
            default:
                System.out.println(res.getStatus());
                return LoginStatus.CONNECTION_ERROR;
        }
    }

    public void updateWitness(){
        this.lbSession.setValue(this.username);
    }

    private void saveToken() {
        try {
            if (new File(Globals.TOKEN_DIR).getParentFile().mkdirs()) {
                if (new File(Globals.TOKEN_DIR).createNewFile()){
                    System.out.println("Token file was created.");
                }
            }
            FileWriter w = new FileWriter(Globals.TOKEN_DIR);
            w.write(this.token);
            w.close();
            System.out.println("Token Written into file");

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public boolean checkForToken() {
        return new File(Globals.TOKEN_DIR).exists();
    }

    private String loadToken () {
        if (!new File(Globals.TOKEN_DIR).exists()) {
            return null;
        }
        StringBuilder foundToken = new StringBuilder("");
        try {
            File myObj = new File(Globals.TOKEN_DIR);
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

    public LoginStatus loginWithToken () {
        final String foundToken = loadToken();
        if (foundToken == null|| foundToken.isBlank()){
            return LoginStatus.UNAUTHORIZED;
        }
        HttpResponse<JsonNode> res = Unirest.get("profile")
                .header("accept", "application/json")
                .header("authorization", "Bearer " + foundToken)
                .asJson();


        switch (res.getStatus()){
            case 200:
                this.username = res.getBody().getObject().getString("username");
                System.out.println("Connected with Token");
                return LoginStatus.SUCCESS;
            case 401:
                System.out.println("Invalid Token");
                deleteToken();
                return LoginStatus.UNAUTHORIZED;
            default:
                System.out.println(res.getStatusText());
                deleteToken();
                return LoginStatus.CONNECTION_ERROR;
        }
    }

    private void deleteToken(){
        if (new File(Globals.TOKEN_DIR).delete()){
            System.out.println("Token file deleted.");
        }
    }

    private void bindJSONObjectToAttributes(JSONObject user) {
        this.username = user.getString("username");
        this.name = user.getString("name");
        this.token = user.getString("access_token");
        this.email = user.getString("email");
        this.isActive = user.getBoolean("isActive");
    }


    private void resetValues() {
        this.token = "";
        this.email = "";
        this.username = "";
        this.name = "";
    }

}
