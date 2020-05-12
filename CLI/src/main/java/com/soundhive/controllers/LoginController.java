package com.soundhive.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.soundhive.Router;
import com.soundhive.authentication.LoginService;
import com.soundhive.authentication.LoginWithTokenService;
import com.soundhive.authentication.SessionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class LoginController implements InterfaceController {
    private Router router;
    private SessionHandler session;


    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
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

    }


    @FXML
    public void login() {
        if (tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            System.out.println("one field is empty.");
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
    }

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

    private void setLoginWithTokenService() {
        this.loginWithTokenService = new LoginWithTokenService(session);
        loginWithTokenService.setOnSucceeded(e -> {
            SessionHandler.LoginStatus status = (SessionHandler.LoginStatus) e.getSource().getValue();
            switch (status) {
                case UNAUTHORIZED:

                    JFXSnackbar bar = new JFXSnackbar(frame);
                    bar.enqueue(new JFXSnackbar.SnackbarEvent(new Label("Token didnt work")));

                    setLoginService();
                    break;
                case CONNECTION_ERROR:
                    System.out.println("Unknown error.");
                    setLoginService();
                    break;
                case SUCCESS:
                    System.out.println("Login controller passed.");
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
            SessionHandler.LoginStatus status = (SessionHandler.LoginStatus) e.getSource().getValue();
            switch (status) {
                case UNAUTHORIZED:

                    System.out.println("wrong password.");

                    JFXSnackbar bar = new JFXSnackbar(frame);
                    bar.enqueue(new JFXSnackbar.SnackbarEvent(new Label("Wrong password or username.")));

                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;

                case CONNECTION_ERROR:
                    System.out.println("Unknown error");
                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;

                case SUCCESS:
                    System.out.println("Connected");

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
