package me.silver.iffmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiColorButtonGrid extends GuiButton {

    private final ResourceLocation location;

    private static final int textureX = 176;
    private static final int primaryY = 0;
    private static final int hoveredY = 8;
    private static final int activeY = 16;

    private final int rowCount;
    private final int colCount;

    private short activeIndex = -1;

    public GuiColorButtonGrid(ResourceLocation location, int buttonId, int x, int y, int rowCount, int colCount) {
        super(buttonId, x, y, 8 * colCount + 2 * (colCount - 1), 8 * rowCount + 2 * (rowCount - 1), "");

        this.location = location;
        this.rowCount = rowCount;
        this.colCount = colCount;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        mc.renderEngine.bindTexture(location);

        this.hovered = isHovered(mouseX, mouseY);

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                drawTexturedModalRect(x + (j * 10), y + (i * 10), textureX, primaryY, 8, 8);
            }
        }

        if (this.hovered) {
            int mouseOffsetX = mouseX - x;
            int mouseOffsetY = mouseY - y;

            if (mouseOffsetX % 10 < 8 && mouseOffsetY % 10 < 8) {
                int buttonIndexX = mouseOffsetX / 10;
                int buttonIndexY = mouseOffsetY / 10;

                if (getColorIndex(buttonIndexX, buttonIndexY) != activeIndex) {
                    drawTexturedModalRect(x + (buttonIndexX * 10), y + (buttonIndexY * 10), textureX, hoveredY, 8, 8);
                }
            }
        }

        if (activeIndex > -1) {
            int row = activeIndex / rowCount;
            int column = activeIndex - (row * 4);

            drawTexturedModalRect(x + (column * 10), y + (row * 10), textureX, activeY, 8, 8);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            int mouseOffsetX = mouseX - x;
            int mouseOffsetY = mouseY - y;

            if (mouseOffsetX % 10 < 8 && mouseOffsetY % 10 < 8) {
                int buttonIndexX = mouseOffsetX / 10;
                int buttonIndexY = mouseOffsetY / 10;
                short index = getColorIndex(buttonIndexX, buttonIndexY);

                if (index > -1) {
                    activeIndex = index;
                }
            }
        }

        return super.mousePressed(mc, mouseX, mouseY);
    }

    private short getColorIndex(int buttonX, int buttonY) {
        return (buttonX < colCount && buttonY < rowCount) ? (short) ((rowCount * buttonY) + buttonX) : -1;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height);
    }
}
