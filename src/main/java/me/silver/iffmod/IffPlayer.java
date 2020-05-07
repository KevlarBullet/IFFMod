package me.silver.iffmod;

import me.silver.iffmod.config.json.JSONSerializable;
import org.json.simple.JSONObject;

public class IffPlayer implements JSONSerializable {

    public final String playerName;
    private String displayName;
    private IffGroup group;
    private int colorIndex;

    public IffPlayer(String playerName) {
        this(playerName, playerName, "", -1);
    }

    public IffPlayer(String playerName, String displayName, String groupName, int colorIndex) {
        this.playerName = playerName;
        this.displayName = displayName;
        this.group = (IffGroup) Iffmod.getInstance().groupConfig.get(groupName);
        this.colorIndex = colorIndex;
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unchecked"})
    @Override
    public JSONObject serialize() {
        JSONObject playerObject = new JSONObject();

        playerObject.put("player_name", playerName);
        playerObject.put("display_name", displayName);
        playerObject.put("group_name", (group != null ? group.getGroupName() : ""));
        playerObject.put("color_index", colorIndex);

        return playerObject;
    }

    public static IffPlayer deserialize(JSONObject object) {
        String playerName = (String) object.get("player_name");
        String displayName = (String) object.get("display_name");
        String groupName = (String) object.get("group_name");
        int colorIndex = (int) ((long) object.get("color_index"));

        return new IffPlayer(playerName, displayName, groupName, colorIndex);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public IffGroup getGroup() {
        return group;
    }

    public void setGroup(IffGroup group) {
        this.group = group;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int index) {
        if (index >= -1 && index <= 15) {
            this.colorIndex = index;
        }
    }

    @Override
    public String toString() {
        return this.playerName;
    }

}
