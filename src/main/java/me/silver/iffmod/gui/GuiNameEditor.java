package me.silver.iffmod.gui;

import me.silver.iffmod.Iffmod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiNameEditor extends GuiScreen {

    private final ResourceLocation background = new ResourceLocation(Iffmod.MODID,"textures/gui/background.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        Minecraft.getMinecraft().renderEngine.bindTexture(background);
        drawTexturedModalRect(0, 0, 1, 1, 15, 15);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

}
