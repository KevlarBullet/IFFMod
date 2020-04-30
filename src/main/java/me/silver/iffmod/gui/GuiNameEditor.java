package me.silver.iffmod.gui;

import me.silver.iffmod.IffGroup;
import me.silver.iffmod.IffPlayer;
import me.silver.iffmod.Iffmod;
import me.silver.iffmod.config.json.JSONConfig;
import me.silver.iffmod.config.json.JSONSerializable;
import me.silver.iffmod.util.GuiColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// A healthy mix of good and lazy programming practices
public class GuiNameEditor extends GuiScreen {

    private final JSONConfig playerConfig = Iffmod.getInstance().playerConfig;
    private final JSONConfig groupConfig = Iffmod.getInstance().groupConfig;

    public final ResourceLocation background = new ResourceLocation(Iffmod.MODID,"textures/background.png");

    private int GUI_CENTER_X;
    private int GUI_CENTER_Y;

    private final List<GuiTextField> textBoxes = new ArrayList<>();
    private GuiTextField activeTextBox;

    // Screw Liskov's Substitution Principle.
    // I don't want to constantly have to cast GuiButton to other subtypes.
    private GuiTextField textBoxPlayerSearch; // 138 x 18 (8, 18) textFieldID: -1
    private GuiTextField textBoxPlayerGroup; // 116 x 18 (8, 132) textFieldID: -2
    private GuiTextField textBoxGroupEditor; // 116 x 18 (8, 172) textFieldID: -3

    private GuiButtonImageLayered closeButton; // 12 x 12 (157, 4) buttonID: 0
    private GuiButtonImageLayered playerSearchConfirm; // 18 x 18 (151, 18) buttonID: 1
    private GuiButtonImageLayered playerListAccept; // 12 x 12 (133, 95) buttonID: 2
    private GuiButtonImageLayered playerListCancel; // 12 x 12 (153, 95) buttonID: 3
    private GuiButtonImageLayered groupEditorCancel; // 12 x 12 (153, 175) buttonID: 6

//    private GuiButtonImageLayered groupEditorA; // 12 x 12 (133, 175)
    private GuiButtonImageLayered groupEditorAdd; // buttonID: 4
    private GuiButtonImageLayered groupEditorAccept; // buttonID: 5

    private GuiButtonScrollBox<IffPlayer> playerList; // 4 (8, 25) buttonID: 100
    private GuiButtonScrollBox<IffGroup> playerGroupList; // 4 (8, 144) buttonID: 101
    private GuiButtonScrollBox<IffGroup> groupList; // 4 (8, 183) buttonID: 102

    private GuiButtonColorGrid playerColor; // 4 x 4 (130, 52) buttonID: 1000
    private GuiButtonColorGrid groupColor; // 16 x 1 (9, 205) buttonID: 1001

    private GuiButtonOther playerSearchClear; // 7 x 7 (135, 23) buttonID: 10000
    private GuiButtonOther playerGroupDropdown; // 7 x 7 (113, 137) buttonID: 10001
    private GuiButtonOther groupEditorDropdown; // 7 x 7 (113, 177) buttonID: 10002

    @Override
    public void initGui() {
        buttonList.clear();

        GUI_CENTER_X = width / 2 - 88;
        GUI_CENTER_Y = height / 2 - 111;

        textBoxes.add(textBoxPlayerSearch = new GuiTextFieldButTheIsEnabledBooleanIsAccessibleBecauseItsDumbThatItIsnt(-1, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 18, 138, 18));
        textBoxes.add(textBoxPlayerGroup = new GuiTextFieldButTheIsEnabledBooleanIsAccessibleBecauseItsDumbThatItIsnt(-2, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 132, 116, 18));
        textBoxes.add(textBoxGroupEditor = new GuiTextFieldButTheIsEnabledBooleanIsAccessibleBecauseItsDumbThatItIsnt(-3, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 172, 116, 18));
        textBoxGroupEditor.setText("Test I guess");
        textBoxGroupEditor.setEnabled(false);

        buttonList.add(closeButton = new GuiButtonImageLayered(background, 0, GUI_CENTER_X + 157, GUI_CENTER_Y + 4, 12, 12, 184, 18, 244, 18));
        buttonList.add(playerSearchConfirm = new GuiButtonImageLayered(background, 1, GUI_CENTER_X + 151, GUI_CENTER_Y + 18, 18, 18, 184, 0, 238, 30));
        buttonList.add(playerListAccept = new GuiButtonImageLayered(background, 2, GUI_CENTER_X + 133, GUI_CENTER_Y + 95, 12, 12, 184, 18, 232, 18));
        buttonList.add(playerListCancel = new GuiButtonImageLayered(background, 3, GUI_CENTER_X + 153, GUI_CENTER_Y + 95, 12, 12, 184, 18, 244, 18));
        buttonList.add(groupEditorCancel = new GuiButtonImageLayered(background, 6, GUI_CENTER_X + 153, GUI_CENTER_Y + 175, 12, 12, 184, 18, 244, 18));

        // These two are handled slightly differently than the others (Only one drawn at a time)
        groupEditorAdd = addButton(new GuiButtonImageLayered(background, 4, GUI_CENTER_X + 133, GUI_CENTER_Y + 175, 12, 12, 184, 18, 184, 30));
        groupEditorAccept = addButton(new GuiButtonImageLayered(background, 5, GUI_CENTER_X + 133, GUI_CENTER_Y + 175, 12, 12, 184, 18, 232, 18));
        groupEditorAccept.shouldDraw = false;
        groupEditorAccept.enabled = false;

        buttonList.add(playerColor = new GuiButtonColorGrid(background, 1000, GUI_CENTER_X + 130, GUI_CENTER_Y + 52, 4, 4));
        buttonList.add(groupColor = new GuiButtonColorGrid(background, 1001, GUI_CENTER_X + 7, GUI_CENTER_Y + 205, 1, 16));

        buttonList.add(playerSearchClear = new GuiButtonOther(background, 10000, GUI_CENTER_X + 135, GUI_CENTER_Y + 23, 7, 7, 176, 24));
        buttonList.add(playerGroupDropdown = new GuiButtonOther(background, 10001, GUI_CENTER_X + 113, GUI_CENTER_Y + 137, 7, 7, 176, 151));
        buttonList.add(groupEditorDropdown = new GuiButtonOther(background, 10002, GUI_CENTER_X + 113, GUI_CENTER_Y + 177, 7, 7, 176, 151));

        buttonList.add(playerList = new GuiButtonScrollBox<>(this, 100,  GUI_CENTER_X + 8, GUI_CENTER_Y + 52, 4));
        buttonList.add(playerGroupList = new GuiButtonScrollBox<>(this, 101, GUI_CENTER_X + 8, GUI_CENTER_Y + 151, 4));
        buttonList.add(groupList = new GuiButtonScrollBox<>(this, 102, GUI_CENTER_X + 8, GUI_CENTER_Y + 191, 4));
        playerGroupList.enabled = false;
        groupList.enabled = false;

        // I'm sure there's a better way to do this but I don't feel like thinking about it right now

        for (JSONSerializable group : groupConfig.getAll()) {
            groupList.addItem((IffGroup) group);
            playerGroupList.addItem((IffGroup) group);
        }

        for (JSONSerializable player : playerConfig.getAll()) {
            playerList.addItem((IffPlayer) player);
        }

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

        for (GuiTextField textField : this.textBoxes) {
            textField.drawTextBox();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button, int mouseX, int mouseY) throws IOException {
        switch (button.id) {
            case 0: // GUI close button
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case 1: // Player search confirm
                textFieldSubmit(textBoxPlayerSearch);
                break;
            case 2: // Player list confirm
                handlePlayerSubmit();
                break;
            case 3: // Player list cancel
                playerColor.setActiveColor(-1);
                playerList.activeItem = -1;
                break;
            case 4: // Group editor add
                if (activeTextBox != null) activeTextBox.setFocused(false);
                activeTextBox = textBoxGroupEditor;

                textBoxGroupEditor.setText("");
                textBoxGroupEditor.setEnabled(true);
                textBoxGroupEditor.setFocused(true);
                textBoxGroupEditor.setCursorPosition(0);

                groupEditorAdd.shouldDraw = false;
                groupEditorAdd.enabled = false;

                groupEditorAccept.shouldDraw = true;
                groupEditorAccept.enabled = true;
                groupColor.setActiveColor(-1);
                break;
            case 5: // Group editor accept
                textFieldSubmit(textBoxGroupEditor);
                // The lack of a break statement here was intentional - the cancel code should run in both cases
            case 6: // Group editor cancel
                handleGECancel();
                break;
            case 101:
                if(playerGroupList.getHoveredIndex(mouseX, mouseY) > -1) {
                    IffGroup group = playerGroupList.getActiveItem();

                    if (group != null) {
                        playerGroupList.enabled = false;
                        playerGroupList.activeItem = -1;
                        textBoxPlayerGroup.setText(group.getGroupName());
                    }
                }
                break;
            case 102: // Group list (editor)
                if(groupList.getHoveredIndex(mouseX, mouseY) > -1) {
                    IffGroup group = groupList.getActiveItem();

                    if (group != null) {
                        groupList.enabled = false;
                        groupList.activeItem = -1;
                        textBoxGroupEditor.setText(group.getGroupName());
                        groupColor.setActiveColor(group.getDefaultColorIndex());

                        groupEditorAccept.shouldDraw = true;
                        groupEditorAccept.enabled = true;

                        groupEditorAdd.shouldDraw = false;
                        groupEditorAdd.enabled = false;
                    }
                }
                break;
            case 10000:
                textBoxPlayerSearch.setText("");
                break;
            case 10001: // Player group editor dropdown
                playerGroupList.enabled = !playerGroupList.enabled;
                if (!playerGroupList.enabled) playerGroupList.activeItem = -1;
                break;
            case 10002: // Group editor dropdown
                groupList.enabled = !groupList.enabled;
                if (!groupList.enabled) groupList.activeItem = -1;
                break;

        }

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (activeTextBox != null) {
            activeTextBox.textboxKeyTyped(typedChar, keyCode);

            if (keyCode == 28) {
                textFieldSubmit(activeTextBox);

                groupEditorAccept.shouldDraw = false;
                groupEditorAccept.enabled = false;

                groupEditorAdd.shouldDraw = true;
                groupEditorAdd.enabled = true;

                textBoxGroupEditor.setText("");
                textBoxGroupEditor.setEnabled(false);
                groupColor.setActiveColor(-1);
            }
        } else {
            Iffmod.LOGGER.info("This is the problem, dummy");
        }

        super.keyTyped(typedChar, keyCode);
    }

    // Copy default mouse behavior but break if a clicked button returns true
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (activeTextBox != null) activeTextBox.setFocused(false);

            // Iterate backwards so click priority matches draw order
            // Because they're not the same and that's dumb
            for (int i = buttonList.size() - 1; i >= 0; i--) {
                GuiButton button = buttonList.get(i);

                if (button.enabled && button.mousePressed(mc, mouseX, mouseY)) {
                    if (activeTextBox != null) {
                        activeTextBox.setFocused(false);
                        activeTextBox = null;
                    }

                    net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(this, button, this.buttonList);
                    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) break;
                    // In case the button is modified through the event, I guess
                    GuiButton guiButton = event.getButton();

                    this.selectedButton = guiButton;
                    guiButton.playPressSound(mc.getSoundHandler());
                    this.actionPerformed(guiButton, mouseX, mouseY);

                    if (this.equals(mc.currentScreen)) {
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
                    }

                    return;
                }
            }

            for (GuiTextField textField : this.textBoxes) {
                if (((GuiTextFieldButTheIsEnabledBooleanIsAccessibleBecauseItsDumbThatItIsnt) textField).getEnabled() && textField.mouseClicked(mouseX, mouseY, mouseButton)) {
                    activeTextBox = textField;
                    return;
                }
            }

            activeTextBox = null;
        }

    }

    private void textFieldSubmit(GuiTextField field) {
        String text = field.getText();

        if (activeTextBox != null) {
            activeTextBox.setText("");
            activeTextBox.setFocused(false);
        }

        if (!text.equals("")) {
            switch (field.getId()) {
                case -1:
                    GuiButtonScrollBox<IffPlayer> iffPlayerList = playerList;
                    IffPlayer player = (IffPlayer) playerConfig.get(text);

                    if (player == null) {
                        player = new IffPlayer(text);
                        playerConfig.add(text, player);
                        iffPlayerList.addItem(player);
                    }

                    iffPlayerList.setDisplayedItem(player);
                    break;
                case -3:
                    GuiColor color = groupColor.getActiveColor();
                    IffGroup group = (IffGroup) groupConfig.get(text);

                    if (group == null) {
                        group = new IffGroup(text, (color != null) ? color.getColorIndex() : -1);
                        groupConfig.add(text, group);

                        groupList.addItem(group);
                        playerGroupList.addItem(group);
                    }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        playerConfig.saveConfig();
        groupConfig.saveConfig();

        super.onGuiClosed();
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        // Prevents textboxes from duplicating on window resize
        textBoxes.clear();
        super.onResize(mcIn, w, h);
    }

    private void handlePlayerSubmit() {
        GuiButtonScrollBox<IffPlayer> iffPlayerBox = playerList;
        GuiButtonColorGrid grid = playerColor;
        GuiColor color = grid.getActiveColor();

        if (color != null && iffPlayerBox.activeItem > -1) {
            IffPlayer player = iffPlayerBox.getActiveItem();

            player.setColorIndex(color.getColorIndex());
        }

        grid.setActiveColor(-1);
        iffPlayerBox.activeItem = -1;
    }

    private void handleGroupSubmit() {

    }

    private void handleGECancel() {
        groupEditorAccept.shouldDraw = false;
        groupEditorAccept.enabled = false;

        groupEditorAdd.shouldDraw = true;
        groupEditorAdd.enabled = true;

        textBoxGroupEditor.setText("");
        textBoxGroupEditor.setEnabled(false);
        groupColor.setActiveColor(-1);
    }

    public void handleItemRemoved(Object o){
        if (o instanceof IffPlayer) {
            playerConfig.remove((IffPlayer) o);
        } else if (o instanceof IffGroup) {
            groupConfig.remove((IffGroup) o);
        }
    }

}
