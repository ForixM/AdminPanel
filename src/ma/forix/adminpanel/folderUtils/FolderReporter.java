package ma.forix.adminpanel.folderUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;

public class FolderReporter extends Thread implements Runnable {
    private File[] content;

    private JSONObject objInfos;
    private JSONArray gameContent;

    public FolderReporter(File[] folder){
        this.content = folder;
    }

    @Override
    public void run() {
        super.run();

        for (File current : content){
            objInfos = new JSONObject();
            if (current.isFile()) {
                //SI C'EST UN FICHIER

                objInfos.put("isFile", true);
                objInfos.put("path", "");
                objInfos.put("size", current.length());

                //FIN
            } else {
                //SI C'EST UN DOSSIER



                //FIN
                FolderExplorer explorer = new FolderExplorer(current.listFiles());
                explorer.start();
                try {
                    explorer.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
