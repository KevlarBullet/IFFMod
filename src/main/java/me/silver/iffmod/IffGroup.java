package me.silver.iffmod;

public class IffGroup {

    private final String groupName;
    private int defaultColorIndex;

    public IffGroup(String groupName) {
        this(groupName, -1);
    }

    public IffGroup(String groupName, int defaultColorIndex) {
        this.groupName = groupName;
        this.defaultColorIndex = defaultColorIndex;
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

    @Override
    public String toString() {
        return this.groupName;
    }
}
