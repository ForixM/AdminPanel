package ma.forix.adminpanel;

import javafx.application.Platform;
import ma.forix.adminpanel.controllers.AdminControllerFX;
import ma.forix.adminpanel.folderUtils.FolderRemover;
import ma.forix.adminpanel.folderUtils.FolderTransfert;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FtpMaker {

    private static String host, username, password;
    private static FTPClient client;
    private static AdminControllerFX adminControllerFX;
    private static int fileUploaded = 0;

    public FtpMaker(String host, String username, String password){
        client = new FTPClient();
        FtpMaker.host = host;
        FtpMaker.username = username;
        FtpMaker.password = password;
    }

    public static void uploadFolder(File folder){
        File[] listFiles = folder.listFiles();
        System.out.println("listfiles: "+listFiles);
        for (File current : listFiles){
            System.out.println("folder: "+current.getName());
        }
        mkdir(folder.getName());
        try {
            client.changeWorkingDirectory(folder.getName());

            FolderTransfert transfert = new FolderTransfert(listFiles);
            transfert.start();
            transfert.join();
            System.out.println("Upload du dossier fini !");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addFileUploaded(){
        fileUploaded++;
        final int fileUploadedT = fileUploaded;
        final int fileAmount = AdminControllerFX.getFileAmount();
        AdminControllerFX.getInstance().setAdvancement((double)fileUploadedT/(double)fileAmount);
    }

    public static int getFileUploaded(){
        return fileUploaded;
    }

    public static void uploadFile(File file){
        try (InputStream input = new FileInputStream(file)){
            System.out.println("Envoi du fichier en cours...");
            client.storeFile(file.getName(), input);
            System.out.println("Envoi terminé");
            reponseServeur();
            ls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean mkdir(String name){
        try {
            System.out.println("j'ai réussis à makedirectory");
            return client.makeDirectory(name);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void cd(String directory) throws IOException {
        client.changeWorkingDirectory(directory);
    }

    public static void mdelete(String fileName){
        try {
            client.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void rmdir(String pathname){
        try {
            if (client.listFiles(pathname).length == 2){
                if (client.removeDirectory(pathname)){
                    ls();
                    adminControllerFX.addText("Dossier supprimmé", false);
                } else {
                    adminControllerFX.addText("[ERREUR] La sélection doit obligatoirement être un dossier !", false);
                }
            } else if (client.listFiles(pathname).length > 2){
                adminControllerFX.addText("il y a plusieurs dossiers à supprimer", false);
                adminControllerFX.addText("longueur variable listfiles: "+client.listFiles(pathname).length, false);
                cd(pathname);
                System.out.println("je suis dans: "+client.printWorkingDirectory());
                FolderRemover remover = new FolderRemover(client.listFiles());
                remover.start();
                remover.join();
                cd("..");
                client.removeDirectory(pathname);
                ls();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static FTPClient getClient(){
        return client;
    }

    public static FTPFile[] getListFiles(String pathname){
        try {
            return client.listFiles(pathname);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FTPFile[] getListFiles(){
        try {
            return client.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setAdminControllerFX(AdminControllerFX adminC){
        adminControllerFX = adminC;
    }

    private static void reponseServeur() {
        String[] reponses = client.getReplyStrings();
        if (reponses != null && reponses.length > 0) {
            for (String reponse : reponses) {
                System.out.println("SERVEUR: " + reponse);
                adminControllerFX.addText(reponse, false);
            }
        }
    }

    public static void logIn(String host, String username, String password){
        client = new FTPClient();
        FtpMaker.host = host;
        FtpMaker.username = username;
        FtpMaker.password = password;

        try {
            client.connect(host, 21);
            reponseServeur();
            int reponse = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reponse)) {
                System.out.println("Operation échoué. Réponse Serveur: " + reponse);
                return; // false;
            }
            boolean etat = client.login(username, password);
            reponseServeur();
            if (!etat){
                System.out.println("Impossible d'accéder au serveur");
                return;// false;
            } else {
                adminControllerFX.activateTextInput(true);
                adminControllerFX.activateDisconnectButton(true);
                System.out.println("connecté");
                ls();
                return;// true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;// false;
        }
    }

    private static void ls(){
        System.out.println("je suis dans ls");
        try {
            FTPFile[] contentList = client.listFiles();
            System.out.println("contenu contentlist: "+contentList);
            System.out.println("sa longueur: "+contentList.length);
            adminControllerFX.addText("Contenu du dossier: "+client.printWorkingDirectory(), false);
            adminControllerFX.clearInfoArea();

            for (FTPFile selected : contentList) {
                System.out.println("file: " + selected.getName());
                adminControllerFX.infoText(selected.getName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exec(String command){
        System.out.println();
        System.out.println();
        String[] commande = command.split(" ");
        if (command.equals("ls")){
            ls();
        }

        if (command.equals("cd")){
            adminControllerFX.addText("Dossier distant: ",false);
        } else if (commande[0].equals("cd") && commande[1].length() > 1){
            try {
                cd(commande[1]);
                adminControllerFX.addText("OK. Le dossier actuel est maintenant: "+client.printWorkingDirectory(), false);
                ls();
            } catch (IOException e) {
                adminControllerFX.addText("Le dossier spécifié n'existe pas", false);
                e.printStackTrace();
            }
        }

        if (commande[0].equals("mkdir") && commande[1].length() > 1){
            if (mkdir(commande[1])){
                adminControllerFX.addText("Dossier créé avec succès", false);
                ls();
            } else {
                adminControllerFX.addText("Échec lors de la création du dossier", false);
            }
        }

        if (commande[0].equals("mdelete") && commande[1].length() > 1){
            try {
                if (client.deleteFile(commande[1])){
                    ls();
                    adminControllerFX.addText("Fichier supprimé", false);
                } else {
                    adminControllerFX.addText("[ERREUR] La sélection doit obligatoirement être un fichier !", false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (commande[0].equals("rmdir") && commande[1].length() > 1)
            rmdir(commande[1]);
    }

    public static void disconnect(){
        try {
            client.disconnect();
            adminControllerFX.addText("Déconnecté du serveur FTP", false);
        } catch (IOException e) {
            adminControllerFX.addText("Tentative de déconnection échoué", false);
            e.printStackTrace();
        }
    }
}
