package com.soundhive.authentication;

import com.soundhive.Globals;
import javafx.beans.property.StringProperty;
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
                //bindJSONObjectToAttributes(res.getBody().getObject());
                this.token = res.getBody().getObject().getString("access_token");
                if (stayConnected){
                    saveToken();
                }
                return LoginStatus.SUCCESS;
            case 401:
                this.resetSessionValues();
                return LoginStatus.UNAUTHORIZED;
            default:
                this.resetSessionValues();
                System.out.println(res.getStatus());
                return LoginStatus.CONNECTION_ERROR;
        }
    }

    public void updateWitness(){
        this.lbSession.setValue(this.username);
    }

    private void saveToken() {
        File target = new File(Globals.TOKEN_DIR);
        try {
            if (target.getParentFile().mkdirs()) {
                if (!(target.exists() || new File(Globals.TOKEN_DIR).createNewFile())){
                    System.err.println("Could not create token file.");
                }
            }
            FileWriter w = new FileWriter(Globals.TOKEN_DIR);
            w.write(this.token);
            w.close();

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
        StringBuilder foundToken = new StringBuilder();
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

    public LoginStatus openSessionWithToken() {
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
                //this.username = res.getBody().getObject().getString("username");
                this.token = foundToken;
                return LoginStatus.SUCCESS;
            case 401:
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


}
