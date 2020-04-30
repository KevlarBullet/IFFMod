package me.silver.iffmod.util;

import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public enum GuiColor {
    BLACK((short) 0, "000000"),
    DARK_BLUE((short) 1, "0000AA"),
    DARK_GREEN((short) 2, "00AA00"),
    DARK_AQUA((short) 3, "00AAAA"),
    DARK_RED((short) 4, "AA0000"),
    DARK_PURPLE((short) 5, "AA00AA"),
    GOLD((short) 6, "FFAA00"),
    GRAY((short) 7, "AAAAAA"),
    DARK_GRAY((short) 8, "555555"),
    BLUE((short) 9, "5555FF"),
    GREEN((short) 10, "55FF55"),
    AQUA((short) 11, "55FFFF"),
    RED((short) 12, "FF5555"),
    LIGHT_PURPLE((short) 13, "FF55FF"),
    YELLOW((short) 14, "FFFF55"),
    WHITE((short) 15, "FFFFFF");


    private final short colorIndex;
    private final String rgbHex;

    GuiColor(short colorIndex, String rgbHex) {
        this.colorIndex = colorIndex;
        this.rgbHex = rgbHex;
    }

    public static GuiColor getByColorIndex(short colorIndex) {
        for (GuiColor color : values()) {
            if (color.colorIndex == colorIndex) return color;
        }

        return BLACK;
    }

    public int getRgbColor() {
        return Integer.parseInt(rgbHex, 16) - 16777216;
    }

    public short getColorIndex() {
        return colorIndex;
    }


    // This won't return null because the supplied color index will always be 0-15
    @SuppressWarnings("ConstantConditions")
    @Override
    public String toString() {
        return TextFormatting.fromColorIndex(colorIndex).toString();
    }
}
