package me.silver.iffmod;

import me.silver.iffmod.config.GroupConfig;
import me.silver.iffmod.config.json.JSONSerializable;
import org.json.simple.JSONObject;

public class PlayerGroup implements JSONSerializable {

    private final String groupName;
    private int defaultColorIndex;

    public PlayerGroup(String groupName) {
        this(groupName, -1);
    }

    public PlayerGroup(String groupName, int defaultColorIndex) {
        this.groupName = groupName;
        this.defaultColorIndex = defaultColorIndex;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject serialize() {
        JSONObject groupObject = new JSONObject();

        groupObject.put("group_name", groupName);
        groupObject.put("color_index", defaultColorIndex);

        return groupObject;
    }

    public static PlayerGroup deserialize(JSONObject object) {
        String groupName = (String) object.get("group_name");
        int colorIndex = (int) ((long) object.get("color_index"));

        return new PlayerGroup(groupName, colorIndex);
    }

    public String getGroupName() {
        return groupName;
    }

    public int getDefaultColorIndex() {
        return defaultColorIndex;
    }

    public void setColorIndex(int index) {
        if (index >= 0 && index <= 15) {
            this.defaultColorIndex = index;
        }
    }
}
