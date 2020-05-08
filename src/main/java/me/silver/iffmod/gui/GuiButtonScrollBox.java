package me.silver.iffmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonScrollBox<E> extends GuiButtonList<E> {

    private final List<E> fullItemList = new ArrayList<>();

    private int sbValue = 0;
    private int sbMouseOffset = 0;
    private ScrollButton sbActiveButton = null;
    private ScrollButton sbHoveredButton = null;

    private final ScrollButton buttonUpArrow;
    private final ScrollButton buttonDownArrow;
    private final ScrollButton scrollBar;

    public GuiButtonScrollBox(GuiNameEditor parent, int buttonId, int x, int y, int buttonCount) {
        super(parent, buttonId, x, y, buttonCount);
        // Add 6 to the width to accommodate for the scrollbar
        this.setWidth(width + 6);

        this.buttonUpArrow = new ScrollButton(x + width - 6, y, 6, 6, 177, 185, 193, 201, 57);
        this.buttonDownArrow = new ScrollButton(x + width - 6, y + height - 6, 6, 6, 177, 185, 193, 201, 65);
        this.scrollBar = new ScrollButton(x + width - 6, y + 6, 6, 24, 177, 185, 193, 201, 73);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.enabled) return;
        mouseDragged(mc, mouseX, mouseY);

        setDisplayedItems(sbValue);
        super.drawButton(mc, mouseX, mouseY, partialTicks);

        resetColors();
        drawRect(this.x + width - 6, this.y, this.x + this.width, this.y + this.height, -13421773);
        drawRect(this.x + width - 5, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, -13092808);
        resetColors();

        ScrollButton hoveredButton = getScrollbarHoverState(mouseX, mouseY);

        if (hoveredButton != null && hoveredButton != sbActiveButton) {
            if (sbHoveredButton != null && hoveredButton != sbHoveredButton) {
                if (sbHoveredButton.getState() == 2) {
                    sbHoveredButton.setState(0);
                }

                hoveredButton.setState(2);
                sbHoveredButton = hoveredButton;
            } else if (sbHoveredButton == null) {
                hoveredButton.setState(2);
                sbHoveredButton = hoveredButton;
            }
        } else {
            if (sbHoveredButton != null && sbHoveredButton != sbActiveButton) sbHoveredButton.setState(0);
            sbHoveredButton = null;
        }

        if (fullItemList.size() <= buttonCount) {
            buttonUpArrow.setState(3);
            scrollBar.setState(3);
            buttonDownArrow.setState(3);
        } else {
            if (sbValue == 0) {
                buttonUpArrow.setState(3);
            } else {
                if (buttonUpArrow.getState() == 3) buttonUpArrow.setState(0);
            }

            if (sbValue == fullItemList.size() - buttonCount) {
                buttonDownArrow.setState(3);
            } else {
                if (buttonDownArrow.getState() == 3) buttonDownArrow.setState(0);
            }

            if (scrollBar.getState() == 3) scrollBar.setState(0);
        }

        buttonUpArrow.draw();
        scrollBar.draw();
        buttonDownArrow.draw();
    }

    // 0 = up button, 1 = down button, 2 = movable bar, 3 = background, -1 = not hovered
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (!this.enabled) return false;

        if (sbActiveButton != null) {
            sbActiveButton.setState(0);
            sbActiveButton = null;
        }

        ScrollButton hoveredButton = getScrollbarHoverState(mouseX, mouseY);
        this.sbActiveButton = hoveredButton;

        if (hoveredButton != null) {
            this.activeItem = -1;
            hoveredButton.setState(1);

            if (hoveredButton == scrollBar) {
                sbMouseOffset = mouseY - scrollBar.y;
            } else {
                int hiddenItemCount = fullItemList.size() - buttonCount;

                if (hiddenItemCount > 0) {
                    if (hoveredButton == buttonUpArrow) {
                        if (sbValue > 0) {
                            sbValue--;
                            updateScrollBarPosition();
                        }
                    } else if (hoveredButton == buttonDownArrow) {
                        if (sbValue < hiddenItemCount) {
                            sbValue++;
                            updateScrollBarPosition();
                        }
                    }
                }

            }

            return true;
        } else {
            return super.mousePressed(mc, mouseX, mouseY);
        }
    }

    private void updateScrollBarPosition() {
        int hiddenItemCount = fullItemList.size() - buttonCount;
        int sbMinY = y + 6;
        int sbMaxY = y + height - 6 - scrollBar.height;
        int sbPositionCount = sbMaxY - sbMinY;
        float increment = (float) sbPositionCount / hiddenItemCount;

        scrollBar.setPositionY(y + 6 + (int) (sbValue * increment));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (sbActiveButton != null) {
            sbActiveButton.setState(0);
            sbActiveButton = null;
        }

        sbMouseOffset = 0;

        super.mouseReleased(mouseX, mouseY);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.sbActiveButton == scrollBar) {
            int sbMinY = y + 6;
            int sbMaxY = y + height - 6 - scrollBar.height;
            int hiddenItemCount = fullItemList.size() - buttonCount;

            scrollBar.setPositionY(Math.max(sbMinY, Math.min(sbMaxY, mouseY - sbMouseOffset)));

            if (hiddenItemCount > 0) {
                int sbPositionCount = sbMaxY - sbMinY;
                float increment = (float) hiddenItemCount / sbPositionCount;

                sbValue = (int) ((scrollBar.y - sbMinY) * increment);
            } else {
                sbValue = 0;
            }
        }

        super.mouseDragged(mc, mouseX, mouseY);
    }

    @Override
    protected int getHoveredIndex(int mouseX, int mouseY) {
        if (mouseX < (x + width - 6)) {
            return (mouseY - this.y) >> 4;
        }

        return -1;
    }

    public boolean isScrollBarHovered(int mouseX, int mouseY) {
        return (isHovered(mouseX, mouseY) && (mouseX >= this.x + this.width - 6));
    }

    public ScrollButton getScrollbarHoverState(int mouseX, int mouseY) {
        if (isScrollBarHovered(mouseX, mouseY)) {
            if (buttonUpArrow.getState() != 3 && mouseY < buttonUpArrow.y + buttonDownArrow.height) {
                return buttonUpArrow;
            } else if (buttonDownArrow.getState() != 3 && mouseY >= buttonDownArrow.y) {
                return buttonDownArrow;
            } else if (scrollBar.getState() != 3 && mouseY >= scrollBar.y && mouseY < scrollBar.y + scrollBar.height) {
                return scrollBar;
            }
        }

        return null;
    }

    @Override
    public void addItem(E item) {
        fullItemList.add(item);
        updateScrollBarPosition();
    }

    @Override
    protected E removeItem(int itemIndex) {
        if (sbValue == fullItemList.size() - buttonCount && sbValue > 0) sbValue--;

        E item = super.removeItem(itemIndex);
        fullItemList.remove(item);

        return item;
    }

    public void setDisplayedItems(int startIndex) {
        this.itemList.clear();
        this.sbValue = startIndex;

        for (int i = startIndex; i < Math.min(fullItemList.size(), startIndex + buttonCount); i++) {
            itemList.add(fullItemList.get(i));
        }
    }

    public void setDisplayedItem(E item) {
        for (int i = 0; i < fullItemList.size(); i++) {
            if (fullItemList.get(i) == item) {
                if (i - sbValue < 0) {
                    setDisplayedItems(i);
                    activeItem = 0;
                } else if (i - sbValue > 3) {
                    int newDisplayIndex = Math.min(i, fullItemList.size() - 4);
                    setDisplayedItems(newDisplayIndex);
                    activeItem = i - newDisplayIndex;
                } else {
                    activeItem = i - sbValue;
                }

                updateScrollBarPosition();
                break;
            }
        }
    }

    private class ScrollButton {

        // 0 = primary, 1 = active, 2 = hovered, 3 = disabled
        private int state;

        int x;
        int y;
        int width;
        int height;

        private final int primaryX;
        private final int activeX;
        private final int hoveredX;
        private final int disabledX;
        private final int textureY;

        ResourceLocation location = GuiButtonScrollBox.this.location;

        public ScrollButton(int x, int y, int width, int height, int primaryX, int activeX, int hoveredX, int disabledX, int textureY) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.primaryX = primaryX;
            this.activeX = activeX;
            this.hoveredX = hoveredX;
            this.disabledX = disabledX;
            this.textureY = textureY;
        }

        public void draw() {
            Minecraft.getMinecraft().renderEngine.bindTexture(location);

            switch(state) {
                case 0:
                    if (primaryX != -1) {
                        drawTexturedModalRect(x, y, primaryX, textureY, width, height);
                    }
                    break;
                case 1:
                    if (activeX != -1) {
                        drawTexturedModalRect(x, y, activeX, textureY, width, height);
                    }
                    break;
                case 2:
                    if (hoveredX != -1) {
                        drawTexturedModalRect(x, y, hoveredX, textureY, width, height);
                    }
                    break;
                case 3:
                    if (disabledX != -1) {
                        drawTexturedModalRect(x, y, disabledX, textureY, width, height);
                    }
                    break;
            }
        }

        public void setState(int state) {
            if (state >= 0 && state <= 3) {
                this.state = state;
            } else {
                this.state = 3;
            }
        }

        public int getState() {
            return state;
        }

        public void setPositionX(int x) {
            this.x = x;
        }

        public void setPositionY(int y) {
            this.y = y;
        }
    }
}
