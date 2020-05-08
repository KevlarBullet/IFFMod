package me.silver.iffmod.gui;

import me.silver.iffmod.util.GuiColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

public class GuiButtonColorGrid extends GuiButton {

    private final ResourceLocation location;

    private static final int textureX = 176;
    private static final int primaryY = 0;
    private static final int hoveredY = 8;
    private static final int activeY = 16;

    private final int rowCount;
    private final int colCount;

    private short activeIndex = -1;

    public GuiButtonColorGrid(ResourceLocation location, int buttonId, int x, int y, int rowCount, int colCount) {
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
                int posX = x + (j * 10);
                int posY = y + (i * 10);

                drawTexturedModalRect(posX, posY, textureX, primaryY, 8, 8);
                drawRect(posX + 2, posY + 2, posX + 6, posY + 6, GuiColor.getByColorIndex(getColorIndex(j, i)).getRgbColor());

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
            int column = activeIndex % colCount;
            int row = (activeIndex - column) / colCount;

            drawTexturedModalRect(x + (column * 10), y + (row * 10), textureX, activeY, 8, 8);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
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

                    return true;
                }
            }
        }

        return false;
    }

    private short getColorIndex(int buttonX, int buttonY) {
        return (buttonX < colCount && buttonY < rowCount) ? (short) ((colCount * buttonY) + buttonX) : -1;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height);
    }

    public GuiColor getActiveColor() {
        return (activeIndex == -1) ? null : GuiColor.getByColorIndex(activeIndex);
    }

    public void setActiveColor(int colorIndex) {
        if (colorIndex >= 0 && colorIndex <= 15) {
            this.activeIndex = (short) colorIndex;
        } else {
            this.activeIndex = -1;
        }
    }
}
