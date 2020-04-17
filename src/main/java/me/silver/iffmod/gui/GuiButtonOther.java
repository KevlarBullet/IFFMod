package me.silver.iffmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonOther extends GuiButton {

    private final ResourceLocation location;
    private final int textureX;
    private final int textureY;

    public GuiButtonOther(ResourceLocation location, int buttonId, int x, int y, int width, int height, int textureX, int textureY) {
        super(buttonId, x, y, width, height, "");

        this.location = location;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!enabled) return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(location);
        this.hovered = isHovered(mouseX, mouseY);

        if (!hovered) {
            drawTexturedModalRect(x, y, textureX, textureY, width, height);
        } else {
            drawTexturedModalRect(x, y, textureX, textureY + 8, width, height);
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height);
    }
}
