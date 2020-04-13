package me.silver.iffmod.config;

import me.silver.iffmod.IffPlayer;
import me.silver.iffmod.config.json.JSONConfig;
import me.silver.iffmod.config.json.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlayerConfig extends JSONConfig {

    public PlayerConfig(String filePath, String fileName) {
        super(filePath, fileName);
    }

    // Temporary solution until I can figure out reflective deserialization/object casting
    @Override
    public void loadConfig() {
        JSONArray jsonObjects = JSONHandler.readFile(filePath, fileName);

        if (jsonObjects != null) {
            for (Object object : jsonObjects) {
                JSONObject configItem = (JSONObject) object;

                for (Object key : configItem.keySet()) {
                    String s = (String) key;
                    IffPlayer player = IffPlayer.deserialize((JSONObject) configItem.get(s));

                    configObjects.put(s, player);
                }
            }
        }
    }

}
