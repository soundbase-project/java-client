package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.soundhive.core.response.Response;
import com.soundhive.gui.authentication.LoginService;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.security.Provider;

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


    private LoginService loginService;


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
            getContext().getRouter().issueMessage("One field is empty.");

        } else {
            pbConnecting.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            btLogin.setVisible(false);
            pbConnecting.setVisible(true);

            loginService.start();
        }
    }

    @Override
    protected void start() {
        setLoginService(true);
        loginService.start();

    }




    private void setLoginService(final boolean autoLogin) {

        this.loginService = new LoginService(tfUsername.textProperty(), tfPassword.textProperty(), cbStayConnected.selectedProperty(), getContext().getSession(), autoLogin);

        loginService.setOnSucceeded(e -> {
            var response = (Response<?>) e.getSource().getValue();
            switch (response.getStatus()) {
                case SUCCESS:
                    getContext().getRouter().issueMessage(String.format("Logged in as %s", getContext().getSession().getUsername()));
                    getContext().getSession().setUserInfos();
                    getContext().getRouter().goTo("Stats", controller -> controller.setContextAndStart(getContext()));
                    break;

                case CONNECTION_FAILED:
                    getContext().getRouter().issueDialog("Server unreachable. Please check your internet connection.");
                    break;

                case UNAUTHENTICATED:
                    if (autoLogin) {
                        getContext().getRouter().issueMessage("Could not login Automatically.");
                    } else {
                        getContext().getRouter().issueMessage("Wrong password or username.");
                    }
                    getContext().getSession().destroySession();
                    break;

                case INTERNAL_ERROR:
                    getContext().getRouter().issueDialog("An error occurred.");
                    getContext().log(response.getMessage());
                    if (response.getException() != null) {
                        getContext().logException((response.getException()));
                    }
                    break;
            }

            pbConnecting.setVisible(false);
            btLogin.setVisible(true);
            loginService.reset();
            setLoginService(false);

            getContext().log("login request : " + response.getMessage());
            System.out.println(response.getStatus());
        });
        this.loginService.setOnFailed(e -> {
            pbConnecting.setVisible(false);
            btLogin.setVisible(true);
            loginService.reset();
            setLoginService(false);

            getContext().logException(e.getSource().getException());
            getContext().getRouter().issueDialog("An error occurred in the login process.");
        });


    }

}
