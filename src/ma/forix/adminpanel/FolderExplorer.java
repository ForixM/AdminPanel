package ma.forix.adminpanel;

import java.io.File;

public class FolderExplorer extends Thread implements Runnable {

    File[] content;

    public FolderExplorer(File[] folder){
        this.content = folder;
    }

    @Override
    public void run() {
        super.run();

        for (File current : content){
            if (current.isFile()) {
                AdminControllerFX.addSize(current.length());
                AdminControllerFX.addFile();
            } else {
                AdminControllerFX.addFolder();
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
