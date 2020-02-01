package ma.forix.adminpanel.utils;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Loader {


    public Loader(){

    }

    private static Object obj;
    private static JSONObject jObject;

    private static void loadContent(){
        try (FileReader reader = new FileReader(Saver.getSaveLocation())) {
            obj = new JSONParser().parse(reader);
            jObject = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String loadHost(){
        if (Saver.getSaveLocation().exists()) {
            loadContent();
            if (jObject.containsKey("host"))
                return (String) jObject.get("host");
            else
                return null;
        } else
            return null;
    }

    public static String loadUsername(){
        if (Saver.getSaveLocation().exists()) {
            loadContent();
            if (jObject.containsKey("username"))
                return (String) jObject.get("username");
            else
                return null;
        } else
            return null;
    }

    public static String loadPassword(){
        if (Saver.getSaveLocation().exists()) {
            loadContent();
            if (jObject.containsKey("password"))
                return (String) jObject.get("password");
            else
                return null;
        } else
            return null;
    }

    public static File getGameContentFile(){
        return new File(Saver.getFolderLocation()+"\\content.json");
    }
}
