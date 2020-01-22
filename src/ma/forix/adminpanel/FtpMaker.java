package ma.forix.adminpanel;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

public class FtpMaker {

    private static String host, username, password;
    private static FTPClient client;
    private static AdminControllerFX adminControllerFX;

    public FtpMaker(String host, String username, String password){
        client = new FTPClient();
        FtpMaker.host = host;
        FtpMaker.username = username;
        FtpMaker.password = password;
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
                System.out.println("connecté");
                return;// true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;// false;
        }
    }

    public static void exec(String command){
        System.out.println();
        System.out.println();
        String[] commande = command.split(" ");
        if (command.equals("ls")){
            System.out.println("user executed 'ls' command");
            try {
                FTPFile[] test123 = client.listFiles();
                adminControllerFX.addText("Contenu du dossier: "+client.printWorkingDirectory(), false);

                for (FTPFile temp : test123) {
                    System.out.println("file: " + temp.getName());
                    adminControllerFX.infoText(temp.getName().toString());
                    //adminControllerFX.addText(temp.getName().toString(), false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (command.equals("cd")){
            adminControllerFX.addText("Dossier distant: ",false);
        } else if (commande[0].equals("cd") && commande[1].length() > 1){
            System.out.println("changement de dossier vers: "+commande[1]);
            try {
                client.changeWorkingDirectory(commande[1]);
                System.out.println("OK. Le dossier actuel est maintenant: "+client.printWorkingDirectory());
                adminControllerFX.addText("OK. Le dossier actuel est maintenant: "+client.printWorkingDirectory(), false);
            } catch (IOException e) {
                adminControllerFX.addText("Le dossier spécifié n'existe pas", false);
                e.printStackTrace();
            }
        }
    }
}
