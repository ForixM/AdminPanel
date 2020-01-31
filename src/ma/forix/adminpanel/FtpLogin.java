package ma.forix.adminpanel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ma.forix.adminpanel.controllers.AdminControllerFX;
import ma.forix.adminpanel.controllers.FtpLoginController;

import java.io.IOException;

public class FtpLogin {

    private static Stage window;
    private static FtpLoginController loginController;

    public static void display(AdminControllerFX adminControllerFX){
        window = new Stage();
        window.setResizable(false);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FtpLogin.class.getResource("fxml/FTPLogin.fxml"));
            Parent root = loader.load();
            loginController = loader.getController();
            loginController.setAdminPanel(adminControllerFX);

            Scene scene = new Scene(root, 500, 80);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getWindow(){
        return window;
    }
}
