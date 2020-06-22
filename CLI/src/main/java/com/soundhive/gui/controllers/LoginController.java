package com.soundhive.gui.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.soundhive.core.response.Response;
import com.soundhive.gui.authentication.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

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
            btLogin.setVisible(false);
            pbConnecting.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            pbConnecting.setVisible(true);
            loginService.start();
        }
    }

    @Override
    protected void start() {
        setLoginService(true);
        loginService.start();

        this.btLogin.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                loginService.start();
            }

        });
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
                case UNAUTHENTICATED:
                    if (autoLogin) {
                        getContext().getRouter().issueMessage("Could not login Automatically.");
                    } else {
                        getContext().getRouter().issueMessage("Wrong password or username.");
                    }
                    getContext().getSession().destroySession();
                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;
                case INTERNAL_ERROR:
                    getContext().getRouter().issueDialog("An error occurred.");
                    getContext().log(response.getMessage());
                    if (response.getException() != null) {
                        getContext().logException((response.getException()));
                    }
                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;
            }
            getContext().log("login request : " + response.getMessage());
            loginService.reset();

            //next login service will be called by user so all messages need to be activated
            if (autoLogin) {
                setLoginService(false);
            }
        });
        this.loginService.setOnFailed(e -> {
            getContext().logException(e.getSource().getException());
            getContext().getRouter().issueDialog("An error occurred.");
            loginService.reset();
            if (autoLogin) {
                setLoginService(false);
            }
        });


    }

}
