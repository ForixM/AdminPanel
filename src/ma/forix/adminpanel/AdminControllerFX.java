package ma.forix.adminpanel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;


public class AdminControllerFX {

    @FXML
    private TextField textInputField;
    @FXML
    private TextArea consoleArea;
    @FXML
    private TextArea lsArea;
    @FXML
    private Button disconnectButton;

    private DirectoryChooser fileChooser;

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

    public void activateDisconnectButton(boolean value){
        disconnectButton.setDisable(!value);
    }

    @FXML
    private void disconnect(){
        FtpMaker.disconnect();
        activateTextInput(false);
        activateDisconnectButton(false);
    }

    private static File selectedFile;

    @FXML
    private void selectGameFolder(){
        selectedFile = fileChooser.showDialog(AdminPanel.getWindow());
        System.out.println("File choosed: "+selectedFile.toString());
        System.out.println("Size: "+(selectedFile.getTotalSpace()-selectedFile.getFreeSpace()));
        getFolderSize(selectedFile);
    }

    private int size = 0;
    private File[] files;
    private File[] toDo;

    private int getFolderSize(File folder){
        files = folder.listFiles();

        while (files.length != 0){
            ArrayList<File> filesTEMP = new ArrayList<>();
            for (File current : files){
                System.out.println("current file: "+current);
                if (current.isFile())
                    size+=current.length();
                else{
                    filesTEMP.add(current);
                }
            }
            toDo = filesTEMP.toArray(new File[filesTEMP.size()]);
            filesTEMP =
            for (File temp : toDo){
                files = temp.listFiles()
            }

            for (File temp : files){
                System.out.println("[CONTENU] "+temp.toString());
            }
            System.out.println("size: "+size);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("FINI !");


        return size;
    }

    public void addText(String text, boolean userEnter){
        if (userEnter) {
            consoleArea.appendText("ftp> " + text + "\n");
            System.out.println("ftp> " + text);
        }else {
            consoleArea.appendText(" " + text + "\n");
            System.out.println(" " + text);
        }
    }

    public void clearInfoArea(){
        lsArea.clear();
    }

    public void infoText(String text){
        lsArea.appendText(text+"\n");
    }

    @FXML
    private void initialize() {
        fileChooser = new DirectoryChooser();
        activateDisconnectButton(false);
        System.out.println("tadaaa");
    }
}
