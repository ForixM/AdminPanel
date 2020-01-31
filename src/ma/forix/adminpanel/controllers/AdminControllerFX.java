package ma.forix.adminpanel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ma.forix.adminpanel.AdminPanel;
import ma.forix.adminpanel.FtpLogin;
import ma.forix.adminpanel.FtpMaker;
import ma.forix.adminpanel.folderUtils.FolderExplorer;

import java.io.File;


public class AdminControllerFX {

    @FXML
    private TextField textInputField;
    @FXML
    private TextArea consoleArea;
    @FXML
    private TextArea lsArea;
    @FXML
    private Button disconnectButton;
    @FXML
    private Label foldersLabel;
    @FXML
    private Label filesLabel;
    @FXML
    private Label sizeLabel;

    private DirectoryChooser dirChooser;
    private FileChooser fileChooser;

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
        clearInfoArea();
    }

    private static File selectedFolder;
    private static File selectedFile;

    @FXML
    private void selectGameFolder(){
        selectedFolder = dirChooser.showDialog(AdminPanel.getWindow());
        selectedFile = null;
        System.out.println();
        System.out.println();
        System.out.println("Folder choosed: "+selectedFolder.toString());
        System.out.println("Size: "+(selectedFolder.getTotalSpace()-selectedFolder.getFreeSpace()));
        getFolderSize(selectedFolder);
        filesLabel.setText("Fichiers: "+fileAmount+"");
        foldersLabel.setText("Dossiers: "+foldersAmount+"");
        sizeLabel.setText("Taille totale: "+(float)size/1024/1024+"Mo");
    }

    @FXML
    private void selectFile(){
        selectedFile = fileChooser.showOpenDialog(AdminPanel.getWindow());
        selectedFolder = null;
        System.out.println();
        System.out.println();
        System.out.println("File choosed: "+selectedFile.toString());
        System.out.println("Size: "+selectedFile.length()/1024+"Ko");

        filesLabel.setText("Fichiers: 1");
        foldersLabel.setText("Dossiers: CEST UN FICHIER");
        sizeLabel.setText("Taille totale: "+round((double)selectedFile.length()/1024/1024)+"Mo");
    }

    public double round(double val){
        return (Math.floor(val*1000.0))/1000;
    }

    private static long size = 0;
    private static int fileAmount = 0;
    private static int foldersAmount = 0;
    private File[] files;
    private File[] toDo;

    private long getFolderSize(File folder){
        files = folder.listFiles();
        FolderExplorer explorer = new FolderExplorer(files);
        long startMillis = System.currentTimeMillis();
        explorer.start();
        try {
            explorer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endMillis = System.currentTimeMillis();
        System.out.println("Temps: "+(double)(endMillis-startMillis)/1000+" sec");
        System.out.println(size/1024+"Ko");
        System.out.println(size/1024/1024+"Mo");
        System.out.println(size/1024/1024/1024+"Go");
        System.out.println();
        System.out.println("Folders: "+foldersAmount);
        System.out.println("Files: "+fileAmount);

        System.out.println("FINI !");


        return size;
    }

    public static void addSize(long amount){
        size+=amount;
    }

    public static void addFile(){
        fileAmount++;
    }

    public static void addFolder(){
        foldersAmount++;
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
    private void upload(){
        if (selectedFile != null){
            FtpMaker.uploadFile(selectedFile);
        } else if (selectedFolder != null){
            FtpMaker.uploadFolder(selectedFolder);
        }
    }

    @FXML
    private void initialize() {
        dirChooser = new DirectoryChooser();
        fileChooser = new FileChooser();
        activateDisconnectButton(false);
        System.out.println("tadaaa");
    }
}
