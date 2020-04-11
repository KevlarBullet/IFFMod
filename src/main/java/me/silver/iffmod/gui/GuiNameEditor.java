package me.silver.iffmod.gui;

import me.silver.iffmod.Iffmod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiNameEditor extends GuiScreen {

    private final ResourceLocation background = new ResourceLocation(Iffmod.MODID,"textures/background.png");

    private int GUI_CENTER_X;
    private int GUI_CENTER_Y;

    private final int SMALL_BUTTON_SIZE = 8;
    private final int MEDIUM_BUTTON_SIZE = 12;
    private final int LARGE_BUTTON_SIZE = 18;
    private final int SMALL_BOX_WIDTH = 116;
    private final int LARGE_BOX_WIDTH = 138;
    private final int SCROLL_BOX_ITEM_HEIGHT = 16;
    private final int SCROLL_BOX_HEIGHT = 64;

    private GuiTextField textBoxPlayerSearch;
    private GuiTextField textBoxPlayerGroup;
    private GuiTextField textBoxGroupEditor;

    private GuiButtonList<String> guiButtonList;
    private GuiColorButtonGrid colorGrid;

    private final GuiTextField[] textBoxes = {textBoxPlayerSearch, textBoxPlayerGroup, textBoxGroupEditor};

    // The currently active text box that the player clicked on
    private GuiTextField activeTextBox;

    private final int CLOSE_BUTTON = 0;
    private GuiButton buttonCloseGui;

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(buttonCloseGui = new GuiButton(CLOSE_BUTTON, GUI_CENTER_X, GUI_CENTER_Y, 12, 12, ""));

        GUI_CENTER_X = width / 2 - 88;
        GUI_CENTER_Y = height / 2 - 111;

        textBoxPlayerSearch = new GuiTextField(-1, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 18, LARGE_BOX_WIDTH, LARGE_BUTTON_SIZE);
        textBoxPlayerGroup = new GuiTextField(-2, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 132, LARGE_BOX_WIDTH, LARGE_BUTTON_SIZE);
        textBoxGroupEditor = new GuiTextField(-3, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 172, SMALL_BOX_WIDTH, LARGE_BUTTON_SIZE);

        guiButtonList = new GuiScrollBox<>(background, 72, GUI_CENTER_X - 120, GUI_CENTER_Y, 6);
        colorGrid = new GuiColorButtonGrid(background, -69, GUI_CENTER_X + 184, GUI_CENTER_Y, 4, 4);

        guiButtonList.addItem("This");
        guiButtonList.addItem("Is");
        guiButtonList.addItem("A");
        guiButtonList.addItem("Test");
        guiButtonList.addItem("Thingy");
        guiButtonList.addItem("Please");
        guiButtonList.addItem("Ignore");
        guiButtonList.addItem("1");
        guiButtonList.addItem("2");
        guiButtonList.addItem("3");
        guiButtonList.addItem("4");
        guiButtonList.addItem("5");

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        Minecraft.getMinecraft().renderEngine.bindTexture(background);
        drawTexturedModalRect(GUI_CENTER_X, GUI_CENTER_Y, 0, 0, 176, 222);

        mc.fontRenderer.drawString("Search for a Player:", GUI_CENTER_X + 8, GUI_CENTER_Y + 7, 8421504);
        mc.fontRenderer.drawString("Player Editor:", GUI_CENTER_X + 8, GUI_CENTER_Y + 41, 8421504);
        mc.fontRenderer.drawString("Color:", GUI_CENTER_X + 130, GUI_CENTER_Y + 41, 8421504);
        mc.fontRenderer.drawString("Player Group:", GUI_CENTER_X + 8, GUI_CENTER_Y + 121, 8421504);
        mc.fontRenderer.drawString("Group Editor:", GUI_CENTER_X + 8, GUI_CENTER_Y + 161, 8421504);
        mc.fontRenderer.drawString("Group Color:", GUI_CENTER_X + 8, GUI_CENTER_Y + 195, 8421504);
//        drawString(mc.standardGalacticFontRenderer, "Search for a Player:", GUI_CENTER_X + 8, GUI_CENTER_Y + 5, 8421504);

        textBoxPlayerSearch.drawTextBox();
        textBoxPlayerGroup.drawTextBox();
        textBoxGroupEditor.drawTextBox();

        guiButtonList.drawButton(mc, mouseX, mouseY, 0);
        colorGrid.drawButton(mc, mouseX, mouseY, 0);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case CLOSE_BUTTON:
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
        }

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (activeTextBox != null) {
            activeTextBox.textboxKeyTyped(typedChar, keyCode);

            // Key code for enter?
            if (keyCode == 28) {
                activeTextBox.setFocused(false);

                switch (activeTextBox.getId()) {
                    case -1:

                        break;
                    case -2:

                        break;

                    case -3:

                        break;
                }

                activeTextBox = null;
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Activate text box if clicked (I'm sure there's a better way to do this, but I can't think of it)
        if (textBoxPlayerSearch.mouseClicked(mouseX, mouseY, mouseButton)) {
            activeTextBox = textBoxPlayerSearch;
        } else if (textBoxPlayerGroup.mouseClicked(mouseX, mouseY, mouseButton)) {
            activeTextBox = textBoxPlayerGroup;
        } else if (textBoxGroupEditor.mouseClicked(mouseX, mouseY, mouseButton)) {
            activeTextBox = textBoxGroupEditor;
        } else {
            activeTextBox = null;
        }

        guiButtonList.mousePressed(mc, mouseX, mouseY);
        colorGrid.mousePressed(mc, mouseX, mouseY);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        guiButtonList.mouseReleased(mouseX, mouseY);

        super.mouseReleased(mouseX, mouseY, state);
    }
}
