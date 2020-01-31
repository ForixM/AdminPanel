package ma.forix.adminpanel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ma.forix.adminpanel.FtpLogin;
import ma.forix.adminpanel.FtpMaker;
import ma.forix.adminpanel.controllers.AdminControllerFX;
import org.apache.commons.net.ftp.FTPClient;

public class FtpLoginController {

    private static AdminControllerFX adminControllerFX;
    private static FTPClient client;

    private static String host, username, password;

    @FXML
    private TextField hostField, usernameField;
    @FXML
    private PasswordField passwordField;


    public FtpLoginController(){
        System.out.println("c'est ici !");
    }

    public void setAdminPanel(AdminControllerFX adminControllerFX){
        this.adminControllerFX = adminControllerFX;
    }

    @FXML
    private void logIn(){
        FtpMaker.setAdminControllerFX(adminControllerFX);
        FtpMaker.logIn(hostField.getText(), usernameField.getText(), passwordField.getText());
        FtpLogin.getWindow().close();
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        System.out.println("cheh t'as vu !");
    }
}
