package me.silver.iffmod.config.json;

import me.silver.iffmod.Iffmod;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JSONHandler {

    private static final JSONParser parser = new JSONParser();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static JSONArray readFile(String filePath, String fileName) {
        File file = new File(filePath + fileName);
        JSONArray array = null;

        try {
            if (!file.exists()) {
                Iffmod.LOGGER.warn("File '" + fileName + "' does not exist. Creating new one...");
                file.getParentFile().mkdirs();
            }

            FileReader reader = new FileReader(file);
            array = (JSONArray) parser.parse(reader);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    // Result of mkdirs() call is unneeded
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeFile(String filePath, String fileName, JSONArray array) {
        File file = new File(filePath + fileName);

        try {
            if (!file.exists()) file.getParentFile().mkdirs();

            FileWriter writer = new FileWriter(file);

            writer.write(array.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
