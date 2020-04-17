package me.silver.iffmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonList<E> extends GuiButton {

    protected final List<E> itemList;

    private final GuiNameEditor parent;
    protected final ResourceLocation location;
    protected final int buttonCount;

    private static final int ACTIVE_TEXTURE_X = 0;
    private static final int ACTIVE_TEXTURE_Y = 222;
    private static final int HOVERED_TEXTURE_X = 0;
    private static final int HOVERED_TEXTURE_Y = 238;
    private static final int LIST_ITEM_WIDTH = 110;
    private static final int LIST_ITEM_HEIGHT = 16;
    private static final int REMOVE_BUTTON_X = 176;
    private static final int REMOVE_BUTTON_INACTIVE_Y = 40;
    private static final int REMOVE_BUTTON_ACTIVE_Y = 48;
    public static final int REMOVE_BUTTON_OFFSET_X = 101;
    public static final int REMOVE_BUTTON_OFFSET_Y = 4;

    protected int activeItem = -1;

    // Maybe not the best practice to have the ResourceLocation passed in through the constructor if it should always be the same, but whatever
    public GuiButtonList(GuiNameEditor parent, int buttonId, int x, int y, int buttonCount) {
        super(buttonId, x, y, 110, buttonCount << 4, "");

        this.parent = parent;
        this.location = parent.background;
        this.buttonCount = buttonCount;
        this.itemList = new ArrayList<>(buttonCount);
    }

    // TODO: Figure out why texture isn't drawing correctly
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.enabled) return;
        mc.renderEngine.bindTexture(location);

        // Have to reset colors after every draw action
        resetColors();
        drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
        resetColors();

        this.hovered = isHovered(mouseX, mouseY);
        int hoveredIndex = -1;

        if (this.hovered) {
            hoveredIndex = this.getHoveredIndex(mouseX, mouseY);

            if (hoveredIndex > -1 && hoveredIndex < itemList.size() && hoveredIndex != activeItem) {
                drawTexturedModalRect(this.x, this.y + (hoveredIndex << 4), HOVERED_TEXTURE_X, HOVERED_TEXTURE_Y,
                        LIST_ITEM_WIDTH, LIST_ITEM_HEIGHT);
            }

        }

        if (activeItem > -1) {
            drawTexturedModalRect(this.x, this.y + (activeItem << 4), ACTIVE_TEXTURE_X, ACTIVE_TEXTURE_Y,
                    LIST_ITEM_WIDTH, LIST_ITEM_HEIGHT);
        }

        // Draw button text
        for (int i = 0; i < itemList.size(); i++) {
            drawString(mc.fontRenderer, itemList.get(i).toString(), x + 4, y + (i * 16) + 4, 14737632);
        }

        // Draw item remove buttons
        mc.renderEngine.bindTexture(location);
        for (int i = 0; i < itemList.size(); i++) {
            if (i == hoveredIndex && isRemoveButtonHovered(hoveredIndex, mouseX, mouseY)) {
                drawTexturedModalRect(x + REMOVE_BUTTON_OFFSET_X, y + (i << 4)
                        + REMOVE_BUTTON_OFFSET_Y, REMOVE_BUTTON_X, REMOVE_BUTTON_ACTIVE_Y, 8, 8);
            } else {
                drawTexturedModalRect(x + REMOVE_BUTTON_OFFSET_X, y + (i << 4)
                        + REMOVE_BUTTON_OFFSET_Y, REMOVE_BUTTON_X, REMOVE_BUTTON_INACTIVE_Y, 8, 8);
            }
        }

    }

    // Resets the GLSM color so textures will be displayed properly
    protected void resetColors() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void addItem(E item) {
        if (itemList.size() >= buttonCount) return;

        itemList.add(item);
    }

    public E setItem(int index, E item) {
        if (index > buttonCount) throw new IndexOutOfBoundsException();
        return itemList.set(index, item);
    }

    public E getActiveItem() {
        if (activeItem > -1) {
            return itemList.get(activeItem);
        }

        return null;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            int itemIndex = getHoveredIndex(mouseX, mouseY);

            if (isRemoveButtonHovered(itemIndex, mouseX, mouseY)) {
                removeItem(itemIndex);
                activeItem = -1;
            } else if (itemIndex < itemList.size()) {
                activeItem = itemIndex;
            } else {
                activeItem = -1;
            }

            return true;
        }

        return false;
    }

    protected E removeItem(int itemIndex) {
        parent.handleItemRemoved(itemList.get(itemIndex));
        return itemList.remove(itemIndex);
    }

    // Check for mouse hover is probably redundant
    protected int getHoveredIndex(int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            return (mouseY - this.y) >> 4;
        }

        return -1;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height);
    }

    public boolean isRemoveButtonHovered(int itemIndex, int mouseX, int mouseY) {
        if (itemIndex == -1 || itemIndex >= itemList.size()) return false;

        int buttonX = x + REMOVE_BUTTON_OFFSET_X;
        int buttonY = y + REMOVE_BUTTON_OFFSET_Y + (itemIndex << 4);

        return (mouseX >= buttonX && mouseX < buttonX + 8 && mouseY >= buttonY && mouseY < buttonY + 8);
    }
}
