package me.silver.iffmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

// Honestly, it's just a button
public class GuiButtonImageLayered extends GuiButton {

    private final ResourceLocation location;

    private final int backgroundX;
    private final int backgroundY;
    private final int foregroundX;
    private final int foregroundY;

    private boolean isActive = false;
    public boolean shouldDraw = true;

    public GuiButtonImageLayered(ResourceLocation location, int buttonId, int x, int y, int widthIn, int heightIn, int backgroundX, int backgroundY, int foregroundX, int foregroundY) {
        super(buttonId, x, y, widthIn, heightIn, "");

        this.location = location;
        this.backgroundX = backgroundX;
        this.backgroundY = backgroundY;
        this.foregroundX = foregroundX;
        this.foregroundY = foregroundY;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!shouldDraw) return;

        mc.renderEngine.bindTexture(location);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.hovered = isHovered(mouseX, mouseY);

        if (backgroundX > -1 && backgroundY > -1) {
            if (!this.enabled) {
                drawTexturedModalRect(x, y, backgroundX + width * 2, backgroundY, width, height);
            } else {
                if (this.isActive) {
                    drawTexturedModalRect(x, y, backgroundX + width, backgroundY, width, height);
                } else if (this.hovered) {
                    drawTexturedModalRect(x, y, backgroundX + width * 3, backgroundY, width, height);
                } else {
                    drawTexturedModalRect(x, y, backgroundX, backgroundY, width, height);
                }

                drawTexturedModalRect(x, y, foregroundX, foregroundY, width, height);
            }
        } else {
            if (this.enabled) {
                drawTexturedModalRect(x, y, foregroundX, foregroundY, width, height);
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY) && this.enabled) {
            this.isActive = true;

            return true;
        }

        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.isActive = false;

        super.mouseReleased(mouseX, mouseY);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height);
    }
}
