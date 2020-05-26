package com.soundhive.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.soundhive.Router;
import com.soundhive.authentication.LoginService;
import com.soundhive.authentication.LoginWithTokenService;
import com.soundhive.authentication.SessionHandler;
import com.soundhive.response.Response;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class LoginController implements IUiController {
    private Router router;
    private SessionHandler session;


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
            this.router.issueMessage("one field is empty.");
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
    public void setContext(Router router, SessionHandler session) {

        this.router = router;
        this.session = session;
        if (session.checkForToken()) {
            setLoginWithTokenService();
            loginWithTokenService.start();
        } else {
            setLoginService();
        }
    }


    //TODO: refactor to reduce reused code.
    private void setLoginWithTokenService() {
        this.loginWithTokenService = new LoginWithTokenService(session);
        loginWithTokenService.setOnSucceeded(e -> {
            Response<Void> status = (Response<Void>) e.getSource().getValue();
            switch (status.getStatus()) {
                case UNAUTHENTICATED:
                    this.router.issueMessage("Could not connect using saved token.");
                    setLoginService();
                    break;
                case ERROR:
                    this.router.issueMessage("Unable to connect the server.");
                    setLoginService();
                    break;
                case SUCCESS:
                    this.router.issueMessage(String.format("Logged in as %s", session.getUsername()));
                    session.updateWitness();
                    router.goTo("Stats", controller -> controller.setContext(router, session));
                    break;
            }
            loginWithTokenService.reset();
        });
        this.loginWithTokenService.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            loginWithTokenService.reset();
        });
    }

    private void setLoginService() {

        this.loginService = new LoginService(tfUsername.textProperty(), tfPassword.textProperty(), cbStayConnected.selectedProperty(), session);

        loginService.setOnSucceeded(e -> {
            Response<Void> response = (Response<Void>) e.getSource().getValue();
            switch (response.getStatus()) {
                case UNAUTHENTICATED:
                    this.router.issueMessage("Wrong password or username.");


                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;

                case ERROR:
                    this.router.issueMessage("Unable to connect the server.");
                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;

                case SUCCESS:
                    this.router.issueMessage(String.format("Logged in as %s", "not implemented"));

                    session.updateWitness();
                    router.goTo("Stats", controller -> controller.setContext(router, session));
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
