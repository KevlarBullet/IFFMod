package me.silver.iffmod.gui;

import me.silver.iffmod.IffGroup;
import me.silver.iffmod.IffPlayer;
import me.silver.iffmod.IffMod;
import me.silver.iffmod.config.GroupConfig;
import me.silver.iffmod.config.PlayerConfig;
import me.silver.iffmod.util.GuiColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// A healthy mix of good and lazy programming practices
@SuppressWarnings("FieldCanBeLocal")
public class GuiNameEditor extends GuiScreen {

    private final PlayerConfig playerConfig = IffMod.getInstance().playerConfig;
    private final GroupConfig groupConfig = IffMod.getInstance().groupConfig;

    private final ArrayList<String> onlinePlayers = new ArrayList<>();
    private String playerSearchAutoFill = "";
    private String currentPlayer = "";
    private int autoFillPosition = -1;

    public final ResourceLocation background = new ResourceLocation(IffMod.MODID,"textures/background.png");

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
//        textBoxGroupEditor.setText("Test I guess");
        textBoxGroupEditor.setEnabled(false);
        textBoxPlayerGroup.setEnabled(false);

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

        for (IffGroup group : groupConfig.getAll()) {
            groupList.addItem(group);
            playerGroupList.addItem(group);
        }

        for (IffPlayer player : playerConfig.getAll()) {
            playerList.addItem(player);
        }

        if (Minecraft.getMinecraft().getConnection() != null) {
            for (NetworkPlayerInfo npi : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
                this.onlinePlayers.add(npi.getGameProfile().getName());
            }
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

        // TODO: Disable drawing of textbox cursor when autofill is displayed (This is harder than I'd like it to be)
        if (!currentPlayer.isEmpty() && autoFillPosition > -1 && activeTextBox == textBoxPlayerSearch) {
            drawString(mc.fontRenderer, playerSearchAutoFill + " (TAB)", textBoxPlayerSearch.x + 4 + autoFillPosition, textBoxPlayerSearch.y + 5, 16777045);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    // TODO: Make each button's action its own method for better clarity
    protected void actionPerformed(GuiButton button, int mouseX, int mouseY) throws IOException {
        switch (button.id) {
            case 0: // GUI close button
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case 1: // Player search confirm
                handlePlayerSearchSubmit();
                break;
            case 2: // Player list confirm
                handlePlayerSubmit();
                // Lack of a break statement here was intentional
            case 3: // Player list cancel
                playerColor.setActiveColor(-1);
                playerList.activeItem = -1;
                textBoxPlayerGroup.setText("");
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
                handleGroupSubmit();
                break;
            case 6: // Group editor cancel
                handleGECancel();
                break;
            case 100: // Player list
                if (playerList.getHoveredIndex(mouseX, mouseY) > -1) {
                    IffPlayer player = playerList.getActiveItem();

                    if (player != null) {
                        IffGroup group = groupConfig.get(player.getGroup());
                        int color = player.getColorIndex();

                        playerColor.setActiveColor(color);

                        if (group != null) {
                            textBoxPlayerGroup.setText(group.getGroupName());

                            if (color == -1) {
                                playerColor.setActiveColor(group.getDefaultColorIndex());
                            }
                        } else {
                            textBoxPlayerGroup.setText("");
                        }

                    } else {
                        playerColor.setActiveColor(-1);
                        textBoxPlayerGroup.setText("");
                    }
                }
                break;
            case 101: // Player group list
                if(playerGroupList.getHoveredIndex(mouseX, mouseY) > -1) {
                    IffGroup group = playerGroupList.getActiveItem();
                    IffPlayer player = playerList.getActiveItem();

                    if (group != null) {
                        playerGroupList.enabled = false;
                        playerGroupList.activeItem = -1;

                        if (player != null) {
                            textBoxPlayerGroup.setText(group.getGroupName());

                            if (player.getColorIndex() == -1) {
                                playerColor.setActiveColor(group.getDefaultColorIndex());
                                player.setColorIndex(group.getDefaultColorIndex());
                            }
                        }
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
            case 10000: // Player search cancel
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
                if (activeTextBox == textBoxGroupEditor) {
                    handleGroupSubmit();
                } else if (activeTextBox == textBoxPlayerSearch) {
                    handlePlayerSearchSubmit();
                }
            } else {
                if (activeTextBox == textBoxPlayerSearch) {
                    String text = textBoxPlayerSearch.getText().toLowerCase();

                    if (!text.isEmpty()) {
                        if (keyCode == 15) { // Tab key
                            if (!currentPlayer.isEmpty()) {
                                // TODO: Make this less stupid (also get rid of textFieldSubmit method entirely)
                                activeTextBox.setText(currentPlayer);
                                handlePlayerSearchSubmit();
                                clearAutoFill();
                                return;
                            }
                        }

                        String matchedPlayer = "";

                        for (String name : onlinePlayers) {
                            if (name.toLowerCase().startsWith(text)) {
                                matchedPlayer = name;
                                break;
                            }
                        }

                        if (matchedPlayer.isEmpty()) {
                            clearAutoFill();
                        } else {
                            setAutoFill(matchedPlayer.substring(text.length()), matchedPlayer, mc.fontRenderer.getStringWidth(text));
                        }
                    }

                }
            }


//                if (keyCode == 14 || String.valueOf(typedChar).matches("\\w")) {
//                if (activeTextBox == textBoxPlayerSearch) {
//                    String text = textBoxPlayerSearch.getText().toLowerCase();
//
//                    if (text.length() >= 1) {
//                        for (String name : onlinePlayers) {
//                            if (name.toLowerCase().startsWith(text)) {
//                                setAutoFill(name.substring(text.length()), name, mc.fontRenderer.getStringWidth(text));
//                            } else {
//                                clearAutoFill();
//                            }
//                        }
//                    } else {
//                        clearAutoFill();
//                    }
//                }
//            } else if (keyCode == 15) {
//                if (activeTextBox == textBoxPlayerSearch && !currentPlayer.isEmpty()) {
//                    // TODO: Make this less dumb (involves basically removing textFieldSubmit())
//                    activeTextBox.setText(currentPlayer);
//                    textFieldSubmit(activeTextBox);
//                    clearAutoFill();
//                }
//            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    private void setAutoFill(String partialName, String fullName, int position) {
        playerSearchAutoFill = partialName;
        currentPlayer = fullName;
        autoFillPosition = position;
    }

    private void clearAutoFill() {
        IffMod.LOGGER.info("is it this tho");
        setAutoFill("", "", -1);
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

    private void handleGroupSubmit() {
        if (activeTextBox != null) {
            activeTextBox.setText("");
            activeTextBox.setFocused(false);
        }

        String text = textBoxGroupEditor.getText();

        GuiColor color = groupColor.getActiveColor();
        IffGroup group = groupConfig.get(text);

        if (group == null) {
            group = new IffGroup(text, (color != null) ? color.getColorIndex() : -1);
            groupConfig.add(text, group);

            groupList.addItem(group);
            playerGroupList.addItem(group);
        } else {
            if (color != null) {
                group.setColorIndex(color.getColorIndex());
            }

        }

        handleGECancel();
    }

    private void handlePlayerSearchSubmit() {
        String text = textBoxPlayerSearch.getText();

        if (activeTextBox != null) {
            activeTextBox.setText("");
            activeTextBox.setFocused(false);
        }

        if (!text.equals("")) {
//            switch (field.getId()) {
//                case -1:
            IffPlayer player = playerConfig.get(text);

            if (player == null) {
                player = new IffPlayer(text);
                playerConfig.add(text, player);
                playerList.addItem(player);
            }

            playerList.setDisplayedItem(player);
            textBoxPlayerGroup.setText(player.getGroup());
            playerColor.setActiveColor(player.getColorIndex());
            textBoxPlayerSearch.setText("");
//                    break;
//            }
        }
    }

    @Override
    public void onGuiClosed() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).refreshDisplayName();
            }
        }

        super.onGuiClosed();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onResize(Minecraft mcIn, int w, int h) {
        // Prevents textboxes from duplicating on window resize
        textBoxes.clear();
        super.onResize(mcIn, w, h);
    }

    private void handlePlayerSubmit() {
        GuiColor color = playerColor.getActiveColor();

        if (playerList.activeItem > -1) {
            IffPlayer player = playerList.getActiveItem();

            if (player != null) {
                if (color != null) player.setColorIndex(color.getColorIndex());

                player.setGroup(textBoxPlayerGroup.getText());
            }
        }

        playerList.activeItem = -1;
        playerColor.setActiveColor(-1);
        textBoxPlayerGroup.setText("");
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
