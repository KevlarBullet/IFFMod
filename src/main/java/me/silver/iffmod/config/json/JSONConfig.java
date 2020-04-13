package me.silver.iffmod.config.json;

import me.silver.iffmod.IffPlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class JSONConfig {

    protected final String filePath;
    protected final String fileName;

    protected final Map<String, JSONSerializable> configObjects = new HashMap<>();

    public JSONConfig(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public abstract void loadConfig();

    public JSONSerializable get(String name) {
        return configObjects.get(name);
    }

    public void add(String name, JSONSerializable object) {
        configObjects.put(name, object);
    }
}
