package ma.forix.adminpanel.folderUtils;

import ma.forix.adminpanel.FtpMaker;

import java.io.File;
import java.io.IOException;

public class FolderTransfert extends Thread implements Runnable{

    File[] content;

    public FolderTransfert(File[] folder){
        this.content = folder;
    }

    @Override
    public void run() {
        super.run();

        for (File current : content){
            if (current.isFile()) {
                FtpMaker.uploadFile(current);
            } else {
                FtpMaker.mkdir(current.getName());
                try {
                    FtpMaker.cd(current.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FolderTransfert transfert = new FolderTransfert(current.listFiles());
                transfert.start();
                try {
                    transfert.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    FtpMaker.cd("..");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}