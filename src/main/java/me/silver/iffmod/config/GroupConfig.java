package me.silver.iffmod.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.silver.iffmod.IffGroup;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GroupConfig {

    private final String filePath;
    private final String fileName;

    private final Gson gson = new Gson();
    private Map<String, IffGroup> groups = new HashMap<>();

    public GroupConfig(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void loadConfig() {
        FileReader reader = JSONHandler.readFile(filePath, fileName);

        if (reader != null) {
            Type type = new TypeToken<HashMap<String, IffGroup>>() {}.getType();
            groups = gson.fromJson(reader, type);
        }
    }

    public void saveConfig() {
        JSONHandler.writeFile(filePath, fileName, gson.toJson(groups));
    }

    public IffGroup get(String username) {
        return groups.get(username.toLowerCase());
    }

    public Collection<IffGroup> getAll() {
        return groups.values();
    }

    public void add(String name, IffGroup group) {
        if (!groups.containsKey(name.toLowerCase())) {
            groups.put(name.toLowerCase(), group);
        }
    }

    public void remove(IffGroup group) {
        String name = group.getGroupName();
        groups.remove(name);
    }

}
