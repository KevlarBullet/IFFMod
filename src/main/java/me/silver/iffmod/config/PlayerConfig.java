package me.silver.iffmod.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.silver.iffmod.IffPlayer;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerConfig {

    private final String filePath;
    private final String fileName;

    private final Gson gson = new Gson();
    private Map<String, IffPlayer> players = new HashMap<>();

    public PlayerConfig(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void loadConfig() {
        FileReader reader = JSONHandler.readFile(filePath, fileName);

        if (reader != null) {
            Type type = new TypeToken<HashMap<String, IffPlayer>>() {}.getType();
            players = gson.fromJson(reader, type);
        }
    }

    public void saveConfig() {
        JSONHandler.writeFile(filePath, fileName, gson.toJson(players));
    }

    public IffPlayer get(String username) {
        return players.get(username.toLowerCase());
    }

    public Collection<IffPlayer> getAll() {
        return players.values();
    }

    public void add(String name, IffPlayer player) {
        if (!players.containsKey(name.toLowerCase())) {
            players.put(name.toLowerCase(), player);
        }
    }

    public void remove(IffPlayer player) {
        String name = player.playerName.toLowerCase();
        players.remove(name);
    }
}
