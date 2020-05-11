package com.soundhive.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.soundhive.Router;
import com.soundhive.authentication.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import kong.unirest.json.JSONObject;

public class LoginController {
    private Router router;

    @FXML private TextField tfUsername;
    @FXML private TextField tfPassword;
    @FXML private Button btLogin;
    @FXML private ProgressBar pbConnecting;

    private LoginService loginService;



    @FXML
    public void initialize() {
        pbConnecting.setVisible(false);

        this.loginService = new LoginService(tfUsername.textProperty(), tfPassword.textProperty());
        loginService.setOnSucceeded(e -> {

            JSONObject user = (JSONObject) e.getSource().getValue();

            loginService.reset();

            if (user.has("access_token")) {
                router.<StatsController>goTo("Stats", controller -> controller.setRouter(router));
//                JFXSnackbar bar = new JFXSnackbar();
//                bar.enqueue(new JFXSnackbar.SnackbarEvent("Notification Msg"))
                System.out.println("Connected");
                System.out.println("Could not connect.");
            }
            else if (user.has("statusCode")) {
                System.out.println(user.getInt("statusCode") + " " + user.getString("message"));
                pbConnecting.setVisible(false);
                btLogin.setVisible(true);

            }
            else {
                System.out.println(user.toString());
            }

        });
        this.loginService.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            loginService.reset();
        });
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    @FXML
    public void login() {
        btLogin.setVisible(false);
        pbConnecting.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        pbConnecting.setVisible(true);
        loginService.start();
    }

    @FXML
    public void keyPressed(final KeyEvent event) {
        System.out.println("Key pressed: " + event.getCode().getName());
    }

}
