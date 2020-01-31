package ma.forix.adminpanel.folderUtils;

import ma.forix.adminpanel.FtpMaker;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

public class FolderRemover extends Thread implements Runnable {

    FTPFile[] content;

    public FolderRemover(FTPFile[] directory){
        System.out.println("j'ai set la variable:"+directory.length);

        this.content = directory;
    }

    @Override
    public void run() {
        super.run();

        for (FTPFile current : content){
            System.out.println("------------------------------------------------------------------------------------");
            try {
                System.out.println("actuellement dans: "+ FtpMaker.getClient().printWorkingDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (current.isFile()) {
                System.out.println("C'est un fichier: je vais le supprimer: "+current.getName());
                FtpMaker.mdelete(current.getName());
            } else {
                System.out.println("c'est un dossier: "+current.getName());

                //Afficher le contenu du dossier current
                for (FTPFile temp : FtpMaker.getListFiles(current.getName())){
                    System.out.println("ftpfile inside: "+temp);
                }
                if (!current.getName().equals(".") && !current.getName().equals("..")) {

                    //Verification de si il y a des dossiers ou non dans le dossier current
                    System.out.println("LONGUEUR: "+FtpMaker.getListFiles(current.getName()).length);
                    if (FtpMaker.getListFiles(current.getName()).length == 2) {
                        System.out.println("il y a rien dans ce dossier");
                        System.out.println("je vais le supprimer");
                        try {
                            FtpMaker.getClient().removeDirectory(current.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (FtpMaker.getListFiles(current.getName()).length > 2) {
                        System.out.println("Il y a un ou plusieurs dossiers dans ce dossier: " + current.getName());
                        try {
                            FtpMaker.cd(current.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            System.out.println("Current directory: " + FtpMaker.getClient().printWorkingDirectory());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FolderRemover remover = new FolderRemover(FtpMaker.getListFiles());
                        remover.start();
                        System.out.println("NOUVELLE INSTANCE LANCÉE");
                        try {
                            remover.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            FtpMaker.cd("..");
                            System.out.println("je suis revenu dans le dossier: " + FtpMaker.getClient().printWorkingDirectory());
                            System.out.println("j'ai supprimé le dossier: "+current.getName());
                            FtpMaker.getClient().removeDirectory(current.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Instance terminé");
                    }
                }
            }
        }
    }
}
