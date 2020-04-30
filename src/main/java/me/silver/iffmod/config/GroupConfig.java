package me.silver.iffmod.config;

import me.silver.iffmod.IffGroup;
import me.silver.iffmod.config.json.JSONConfig;
import me.silver.iffmod.config.json.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GroupConfig extends JSONConfig {

    public GroupConfig(String filePath, String fileName) {
        super(filePath, fileName);
    }

    @Override
    public void loadConfig() {
        JSONArray jsonObjects = JSONHandler.readFile(filePath, fileName);

        if (jsonObjects != null) {
            for (Object object : jsonObjects) {
                JSONObject configItem = (JSONObject) object;

                for (Object key : configItem.keySet()) {
                    String s = (String) key;
                    IffGroup group = IffGroup.deserialize((JSONObject) configItem.get(s));

                    configObjects.put(s.toLowerCase(), group);
                }
            }
        }
    }

}
