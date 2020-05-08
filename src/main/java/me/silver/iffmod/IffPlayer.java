package me.silver.iffmod;

public class IffPlayer {

    public final String playerName;
    private String displayName;
    private String group;
    private int colorIndex;

    public IffPlayer(String playerName) {
        this(playerName, playerName, "", -1);
    }

    public IffPlayer(String playerName, String displayName, String groupName, int colorIndex) {
        this.playerName = playerName;
        this.displayName = displayName;
        this.group = groupName;
        this.colorIndex = colorIndex;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
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
