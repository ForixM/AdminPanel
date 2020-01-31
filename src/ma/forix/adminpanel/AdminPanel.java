package ma.forix.adminpanel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.forix.adminpanel.controllers.AdminControllerFX;
import ma.forix.adminpanel.utils.Saver;

public class AdminPanel extends Application {

    private static Stage window;
    private static Parent root;
    private static Scene scene;
    public TextField textInputField;

    private AdminControllerFX adminControllerFX;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Admin Panel");
        window.setResizable(false);
        System.out.println("testinputfield: "+textInputField);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AdminPanel.class.getResource("fxml/AdminPanel.fxml"));
        root = loader.load();

        adminControllerFX = loader.getController();

        scene = new Scene(root, 600-10, 400-10);

        window.setScene(scene);
        window.show();
        new Saver();
    }

    public static Stage getWindow(){
        return window;
    }
}
