package ma.forix.adminpanel.utils;

import ma.forix.adminpanel.controllers.FtpLoginController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Saver {

    private static File folderLocation;
    private static final String fileName = "\\loginInfos.json";

    public Saver(){
        folderLocation = new File(System.getProperty("user.home")+"\\AppData\\Roaming\\GameUpdater Admin Panel");
        if (!folderLocation.exists()){
            folderLocation.mkdir();
        }
    }

    public static void save(){
        JSONObject object = new JSONObject();
        object.put("host", FtpLoginController.getHost());
        object.put("username", FtpLoginController.getUsername());
        object.put("password", FtpLoginController.getPassword());

        try (FileWriter writer = new FileWriter(folderLocation+fileName)){
            writer.write(object.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getSaveLocation(){
        return new File(folderLocation+fileName);
    }
}
