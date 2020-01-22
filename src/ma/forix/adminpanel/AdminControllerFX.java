package ma.forix.adminpanel;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class AdminControllerFX {

    @FXML
    private TextField textInputField;
    @FXML
    private TextArea consoleArea;

    private AdminPanel adminPanel;
    private boolean isLog = false;

    public AdminControllerFX(){
        System.out.println("youpiiii");
    }

    public void setAdminPanel(AdminPanel adminPanel){
        this.adminPanel = adminPanel;
    }

    public void showFtpLogin(){
        FtpLogin.display(this);
    }

    @FXML
    private void textEnter(){
        addText(textInputField.getText(), true);
        FtpMaker.exec(textInputField.getText());
        textInputField.clear();
    }

    public void activateTextInput(boolean value){
        textInputField.setDisable(!value);
    }

    public void addText(String text, boolean userEnter){
        if (userEnter)
            consoleArea.appendText("ftp> "+text+"\n");
        else
            consoleArea.appendText(" "+text+"\n");
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        System.out.println("tadaaa");
    }
}
