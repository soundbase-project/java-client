package com.soundhive.core.authentication;

import com.soundhive.core.response.Response;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.JsonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.soundhive.core.response.Response.Status;
//TODO : fix session, token used before it is initialized
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
        Response<Void> res =  Response.postResponse("auth/login",
                "",
                new HashMap<>() {
                    {
                        put("username", username);
                        put("password", password);
                    }
                },
                jsonToken -> {
                    this.token = jsonToken.getObject().getString("access_token");
                });


        if (res.getStatus() == Status.SUCCESS) {
            var userProfileReq = this.loadUserProfile();
        }
        return res;

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

        return Response.queryResponse("profile",
                this.token,
                userData -> {
                    this.bindJSONObjectToAttributes(userData);
                    return null;
                });
    }

    private void deleteToken(){
        if (new File(tokenDir).delete()){
            System.out.println("Token file deleted.");
        }
    }

    private void bindJSONObjectToAttributes(JsonNode res) {
        JSONObject user = res.getObject();
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
