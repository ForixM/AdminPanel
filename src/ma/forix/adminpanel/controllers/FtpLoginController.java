package ma.forix.adminpanel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ma.forix.adminpanel.FtpLogin;
import ma.forix.adminpanel.FtpMaker;
import ma.forix.adminpanel.controllers.AdminControllerFX;
import ma.forix.adminpanel.utils.Loader;
import ma.forix.adminpanel.utils.Saver;
import org.apache.commons.net.ftp.FTPClient;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
        host = hostField.getText();
        username = usernameField.getText();
        password = passwordField.getText();
        Saver.save();
        FtpLogin.getWindow().close();
    }

    @FXML
    private void initialize() {
        System.out.println("cheh t'as vu !");
        new Loader();
        if (Loader.loadHost() != null){
            hostField.setText(Loader.loadHost());
        }

        if (Loader.loadUsername() != null)
            usernameField.setText(Loader.loadUsername());

        if (Loader.loadPassword() != null) {
            passwordField.setText(new String(Base64.getDecoder().decode(Loader.loadPassword()), StandardCharsets.UTF_8));
            System.out.println(passwordField.getText());
            System.out.println(Loader.loadPassword());
        }
    }

    public static String getHost(){
        return host;
    }

    public static String getUsername(){
        return username;
    }

    public static String getPassword(){
        try {
            return Base64.getEncoder().encodeToString(password.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
