package ma.forix.adminpanel.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ma.forix.adminpanel.AdminPanel;
import ma.forix.adminpanel.FtpLogin;
import ma.forix.adminpanel.FtpMaker;
import ma.forix.adminpanel.folderUtils.FolderExplorer;
import ma.forix.adminpanel.folderUtils.FolderReporter;
import ma.forix.adminpanel.utils.Loader;
import ma.forix.adminpanel.utils.Saver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;


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
    @FXML
    private ProgressBar chargement;

    private DirectoryChooser dirChooser;
    private FileChooser fileChooser;
    private static long size = 0;
    private static int fileAmount = 0, foldersAmount = 0;
    private File[] files;
    private static File selectedFolder, selectedFile;
    private static JSONArray gameContent;
    private static double advancement = 0;
    private static AdminControllerFX adminControllerFX;

    //Constructor
    public AdminControllerFX(){
        resetGameContent();
        adminControllerFX = this;
    }


    //other functions

    public void showFtpLogin(){
        FtpLogin.display(this);
    }

    public void activateTextInput(boolean value){
        textInputField.setDisable(!value);
    }

    public void activateDisconnectButton(boolean value){
        disconnectButton.setDisable(!value);
    }

    public double round(double val){
        return (Math.floor(val*1000.0))/1000;
    }

    public void clearInfoArea(){
        lsArea.clear();
    }

    public void infoText(String text){
        lsArea.appendText(text+"\n");
    }

    //FXML functions

    @FXML
    private void textEnter(){
        addText(textInputField.getText(), true);
        FtpMaker.exec(textInputField.getText());
        textInputField.clear();
    }

    @FXML
    private void disconnect(){
        FtpMaker.disconnect();
        activateTextInput(false);
        activateDisconnectButton(false);
        clearInfoArea();
    }

    @FXML
    private void selectGameFolder(){
        selectedFolder = dirChooser.showDialog(AdminPanel.getWindow());
        fileAmount = 0;
        foldersAmount = 0;
        size = 0;
        selectedFile = null;
        System.out.println();
        System.out.println();
        System.out.println("Folder choosed: "+selectedFolder.toString());
        System.out.println("Size: "+(selectedFolder.getTotalSpace()-selectedFolder.getFreeSpace()));
        getFolderSize(selectedFolder);
        filesLabel.setText("Fichiers: "+fileAmount+"");
        foldersLabel.setText("Dossiers: "+foldersAmount+"");
        sizeLabel.setText("Taille totale: "+round((float)size/1024/1024)+"Mo");
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

    @FXML
    private void upload(){
        if (selectedFile != null){
            FtpMaker.uploadFile(selectedFile);
        } else if (selectedFolder != null){
            FtpMaker.mkdir("GameUpdater");
            try {
                FtpMaker.cd("GameUpdater");
                FolderReporter reporter = new FolderReporter(selectedFolder.listFiles());
                reporter.start();
                reporter.join();
                System.out.println("le resultat: "+gameContent.toJSONString());
                Saver.save(gameContent);
                FtpMaker.uploadFile(Loader.getGameContentFile());
                FtpMaker.mkdir("downloads");
                FtpMaker.cd("downloads");

                Thread t = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        FtpMaker.uploadFolder(selectedFolder);
                    }
                };
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void initialize() {
        dirChooser = new DirectoryChooser();
        fileChooser = new FileChooser();
        activateDisconnectButton(false);
        System.out.println("tadaaa");
    }



    //getters and adders public

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

    public static void addGameContent(JSONObject obj){
        gameContent.add(obj);
    }

    public static void resetGameContent(){
        gameContent = new JSONArray();
    }

    public void setAdvancement(double amount){
        advancement = amount;
        System.out.println("amount = "+amount);
        Platform.runLater(() -> chargement.setProgress(amount));
    }

    public static AdminControllerFX getInstance(){
        return adminControllerFX;
    }

    public static int getFileAmount(){
        return fileAmount;
    }

    //getters private

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
}