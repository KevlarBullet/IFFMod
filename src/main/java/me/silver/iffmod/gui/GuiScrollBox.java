package me.silver.iffmod.gui;

import me.silver.iffmod.Iffmod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.util.ResourceLocation;

public class GuiScrollBox extends Gui implements GuiPageButtonList.GuiResponder {

    private GuiButton scrollBarPlayerEditor;
    private GuiButton scrollBarPlayerGroup;
    private GuiButton scrollBarGroupEditor;

    public GuiScrollBox() {

    }

    @Override
    public void setEntryValue(int id, boolean value) {

    }

    @Override
    public void setEntryValue(int id, float value) {

    }

    @Override
    public void setEntryValue(int id, String value) {

    }

    private static class GuiScrollBar extends GuiSlider {

        private static ResourceLocation scrollBarTexture = new ResourceLocation(Iffmod.MODID, "textures/background.png");


        public GuiScrollBar(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String nameIn, float minIn, float maxIn, float defaultValue, FormatHelper formatter) {
            super(guiResponder, idIn, x, y, nameIn, minIn, maxIn, defaultValue, formatter);
        }
    }
}
