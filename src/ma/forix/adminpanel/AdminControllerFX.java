package ma.forix.adminpanel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class AdminControllerFX {

    @FXML
    private TextField textInputField;
    @FXML
    private TextArea consoleArea;
    @FXML
    private TextArea lsArea;

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

    public void infoText(String text){
        lsArea.appendText(text+"\n");
    }

    @FXML
    private void initialize() {
        System.out.println("tadaaa");
    }
}
