package com.soundhive.controllers;

import com.soundhive.Router;
import com.soundhive.authentication.LoginService;
import com.soundhive.authentication.SessionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class LoginController  implements InterfaceController{
    private Router router;
    private SessionHandler session;

    @FXML private TextField tfUsername;
    @FXML private TextField tfPassword;
    @FXML private Button btLogin;
    @FXML private ProgressBar pbConnecting;

    private LoginService loginService;



    @FXML
    public void initialize() {
        pbConnecting.setVisible(false);

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

    @Override
    public void setContext(Router router, SessionHandler session) {

        this.router = router;
        this.session = session;
        setLoginService();
    }

    private void setLoginService() {
        this.loginService = new LoginService(tfUsername.textProperty(), tfPassword.textProperty(), session);

        loginService.setOnSucceeded(e -> {
            SessionHandler.LoginStatus status = (SessionHandler.LoginStatus) e.getSource().getValue();
            switch (status) {
                case WRONG_PASSWORD:
                    System.out.println("wrong password.");
                    pbConnecting.setVisible(false);
                    btLogin.setVisible(true);
                    break;
                case CONNECTION_ERROR:
                    System.out.println("Connexion problem.");
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
