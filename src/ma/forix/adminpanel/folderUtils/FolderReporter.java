package ma.forix.adminpanel.folderUtils;

import ma.forix.adminpanel.controllers.AdminControllerFX;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;

public class FolderReporter extends Thread implements Runnable {
    private File[] content;

    private JSONObject objInfos;
    private static JSONArray gameContent;

    private String gamePath;

    public FolderReporter(File[] folder, String gamePath){
        this.gamePath = gamePath;
        this.content = folder;
    }

    public FolderReporter(File[] folder){
        this.content = folder;
        gamePath = "";
    }

    @Override
    public void run() {
        super.run();

        for (File current : content){

            System.out.println("-----------------------------------------------------------");
            System.out.println("current: "+current.getName());

            objInfos = new JSONObject();
            gameContent = new JSONArray();
            if (current.isFile()) {
                //SI C'EST UN FICHIER

                objInfos.put("isFile", true);
                objInfos.put("path", gamePath);
                objInfos.put("filename", current.getName());
                objInfos.put("size", current.length());
                AdminControllerFX.addGameContent(objInfos);

                //FIN
            } else {
                //SI C'EST UN DOSSIER

                objInfos.put("isFile", false);
                objInfos.put("path", gamePath+current.getName());
                objInfos.put("size", 0);
                AdminControllerFX.addGameContent(objInfos);

                //FIN
                if (current.listFiles().length > 0) {
                    System.out.println("il y a des trucs dans ce dossier");
                    for (File temp : current.listFiles()) {
                        System.out.println("liste: " + temp);
                    }
                    System.out.println("le chemin d'accès: "+gamePath+"\\"+current.getName());
                    FolderReporter reporter = new FolderReporter(current.listFiles(), gamePath+current.getName()+"\\");
                    reporter.start();
                    try {
                        reporter.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    System.out.println("il y a rien dans ce dossier");
            }
        }
    }
}
