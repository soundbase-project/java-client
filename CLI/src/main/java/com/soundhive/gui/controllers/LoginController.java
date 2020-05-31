package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.soundhive.core.response.Response;
import com.soundhive.gui.authentication.LoginService;
import com.soundhive.gui.authentication.LoginWithTokenService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class LoginController extends Controller {


    @FXML
    private JFXTextField tfUsername;
    @FXML
    private JFXPasswordField tfPassword;
    @FXML
    private Button btLogin;
    @FXML
    private ProgressBar pbConnecting;
    @FXML
    private CheckBox cbStayConnected;
    @FXML
    private Pane frame;

    private LoginService loginService;
    private LoginWithTokenService loginWithTokenService;


    @FXML
    public void initialize() {
        pbConnecting.setVisible(false);
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input required");
        tfUsername.setValidators(validator);
        tfUsername.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) tfUsername.validate();
        });
        tfPassword.setValidators(validator);
        tfPassword.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) tfPassword.validate();
        });
    }


    @FXML
    public void login() {
        if (tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            getContext().getRouter().issueMessage("one field is empty.");
        } else {
            btLogin.setVisible(false);
            pbConnecting.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            pbConnecting.setVisible(true);
            loginService.start();
        }

    }

    @FXML
    public void keyPressed(final KeyEvent event) {
        System.out.println("Key pressed: " + event.getCode().getName());
    } // TODO : login on RETURN pressed

    @Override
    protected void start() {

        if (getContext().getSession().checkForToken()) {
            setLoginWithTokenService();
            loginWithTokenService.start();
        } else {
            setLoginService();
        }
    }


    //TODO: refactor to reduce reused code.
    private void setLoginWithTokenService() {
        this.loginWithTokenService = new LoginWithTokenService(getContext().getSession());
        loginWithTokenService.setOnSucceeded(e -> {
            Response<Void> status = (Response<Void>) e.getSource().getValue();
            switch (status.getStatus()) {
                case UNAUTHENTICATED:
                    getContext().getRouter().issueMessage("Could not connect using saved token.");
                    setLoginService();
                    break;
                case CONNEXION_FAILED:
                    getContext().getRouter().issueDialog("Unable to connect the server.");
                    setLoginService();
                    break;
                case SUCCESS:
                    getContext().getRouter().issueMessage(String.format("Logged in as %s", getContext().getSession().getUsername()));
                    //getSession().updateWitness(); TODO : find a new way to update this
                    getContext().getRouter().goTo("Stats", controller -> controller.setContextAndStart(getContext()));
                    break;
            }
            loginWithTokenService.reset();
        });
        this.loginWithTokenService.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            getContext().getRouter().issueDialog("Unable to connect the server.");
            loginWithTokenService.reset();
        });
    }

    private void setLoginService() {

        this.loginService = new LoginService(tfUsername.textProperty(), tfPassword.textProperty(), cbStayConnected.selectedProperty(), getContext().getSession());

        loginService.setOnSucceeded(e -> {
            Response<Void> response = (Response<Void>) e.getSource().getValue();
            switch (response.getStatus()) {
                case UNAUTHENTICATED:
                    getContext().getRouter().issueMessage("Wrong password or username.");


                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;

                case UNKNOWN_ERROR:
                    getContext().getRouter().issueMessage("Unable to connect the server.");
                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;

                case SUCCESS:
                    getContext().getRouter().issueMessage(String.format("Logged in as %s", "not implemented"));

                    //getSession().updateWitness(); //TODO : display user
                    getContext().getRouter().goTo("Stats", controller -> controller.setContextAndStart(getContext()));
                    break;
            }
            loginService.reset();
        });
        this.loginService.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            loginService.reset();
        });
    }


}
