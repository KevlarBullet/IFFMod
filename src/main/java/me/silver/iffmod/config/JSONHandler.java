package me.silver.iffmod.config;

import me.silver.iffmod.IffMod;

import java.io.*;

public class JSONHandler {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static FileReader readFile(String filePath, String fileName) {
        File file = new File(filePath + fileName);

        try {
            if (!file.exists()) {
                IffMod.LOGGER.warn("File '" + fileName + "' does not exist. Creating new one...");
                file.getParentFile().mkdirs();
                return null;
            }

            return new FileReader(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Result of mkdirs() call is unneeded
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeFile(String filePath, String fileName, String jsonObject) {
        File file = new File(filePath + fileName);

        try {
            if (!file.exists()) file.getParentFile().mkdirs();

            FileWriter writer = new FileWriter(file);

            writer.write(jsonObject);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
