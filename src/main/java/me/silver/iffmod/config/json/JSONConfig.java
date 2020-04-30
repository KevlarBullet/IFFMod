package me.silver.iffmod.config.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collection;
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

    public void saveConfig() {
        JSONArray array = new JSONArray();

        for (Map.Entry<String, JSONSerializable> entry : configObjects.entrySet()) {
            JSONObject object = new JSONObject();
            object.put(entry.getKey(), entry.getValue().serialize());

            array.add(object);
        }

        JSONHandler.writeFile(filePath, fileName, array);
    }

    public JSONSerializable get(String name) {
        return configObjects.get(name.toLowerCase());
    }

    public Collection<JSONSerializable> getAll() {
        return configObjects.values();
    }

    public void add(String name, JSONSerializable object) {
        configObjects.put(name.toLowerCase(), object);
    }

    public void remove(JSONSerializable jsonSerializable) {
        remove(jsonSerializable.toString());
    }

    public void remove(String name) {
        configObjects.remove(name.toLowerCase());
    }
}
