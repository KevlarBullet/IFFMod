package me.silver.iffmod.gui;

import me.silver.iffmod.IffGroup;
import me.silver.iffmod.IffPlayer;
import me.silver.iffmod.Iffmod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Each button is a healthy mix of good and lazy programming practices
public class GuiNameEditor extends GuiScreen {

    public final ResourceLocation background = new ResourceLocation(Iffmod.MODID,"textures/background.png");

    private int GUI_CENTER_X;
    private int GUI_CENTER_Y;

    private final List<GuiTextField> textBoxes = new ArrayList<>();
    private GuiTextField activeTextBox;

    private GuiTextField textBoxPlayerSearch; // 138 x 18 (8, 18)
    private GuiTextField textBoxPlayerGroup; // 116 x 18 (8, 132)
    private GuiTextField textBoxGroupEditor; // 116 x 18 (8, 172)

    private GuiButton closeButton; // 12 x 12 (157, 4)
    private GuiButton playerSearchConfirm; // 18 x 18 (151, 18)
    private GuiButton playerListAccept; // 12 x 12 (133, 95)
    private GuiButton playerListCancel; // 12 x 12 (153, 95)
    private GuiButton groupEditorCancel; // 12 x 12 (153, 175)

    private GuiButton groupEditorA; // 12 x 12 (133, 175)
    private GuiButton groupEditorAdd;
    private GuiButton groupEditorAccept;

    private GuiButton playerList; // 4 (8, 25)
    private GuiButton playerGroupList; // 4 (8, 144)
    private GuiButton groupList; // 4 (8, 183)

    private GuiButton playerColor; // 4 x 4 (130, 52)
    private GuiButton groupColor; // 16 x 1 (9, 205)

    private GuiButton playerSearchClear; // 7 x 7 (135, 23)
    private GuiButton playerGroupDropdown; // 7 x 7 (113, 137)
    private GuiButton groupEditorDropdown; // 7 x 7 (113, 177)

    @Override
    public void initGui() {
        buttonList.clear();

        GUI_CENTER_X = width / 2 - 88;
        GUI_CENTER_Y = height / 2 - 111;

        textBoxes.add(textBoxPlayerSearch = new GuiTextField(-1, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 18, 138, 18));
        textBoxes.add(textBoxPlayerGroup = new GuiTextField(-2, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 132, 116, 18));
        textBoxes.add(textBoxGroupEditor = new GuiTextField(-3, mc.fontRenderer, this.GUI_CENTER_X + 8, this.GUI_CENTER_Y + 172, 116, 18));

        buttonList.add(closeButton = new GuiButtonImageLayered(background, 0, GUI_CENTER_X + 157, GUI_CENTER_Y + 4, 12, 12, 184, 18, 244, 18));
        buttonList.add(playerSearchConfirm = new GuiButtonImageLayered(background, 1, GUI_CENTER_X + 151, GUI_CENTER_Y + 18, 18, 18, 184, 0, 238, 30));
        buttonList.add(playerListAccept = new GuiButtonImageLayered(background, 2, GUI_CENTER_X + 133, GUI_CENTER_Y + 95, 12, 12, 184, 18, 232, 18));
        buttonList.add(playerListCancel = new GuiButtonImageLayered(background, 3, GUI_CENTER_X + 153, GUI_CENTER_Y + 95, 12, 12, 184, 18, 244, 18));
        buttonList.add(groupEditorCancel = new GuiButtonImageLayered(background, 6, GUI_CENTER_X + 153, GUI_CENTER_Y + 175, 12, 12, 184, 18, 244, 18));

        // These two are handled slightly differently than the others (Only one drawn at a time)
        groupEditorAdd = new GuiButtonImageLayered(background, 4, GUI_CENTER_X + 133, GUI_CENTER_Y + 175, 12, 12, 184, 18, 184, 30);
        groupEditorAccept = new GuiButtonImageLayered(background, 5, GUI_CENTER_X + 133, GUI_CENTER_Y + 175, 12, 12, 184, 18, 232, 18);
        buttonList.add(groupEditorA = groupEditorAdd);

        buttonList.add(playerList = new GuiButtonScrollBox<IffPlayer>(this, 100,  GUI_CENTER_X + 8, GUI_CENTER_Y + 52, 4));
        buttonList.add(playerGroupList = new GuiButtonScrollBox<IffGroup>(this, 101, GUI_CENTER_X + 8, GUI_CENTER_Y + 144, 4));
        buttonList.add(groupList = new GuiButtonScrollBox<IffGroup>(this, 102, GUI_CENTER_X + 8, GUI_CENTER_Y + 183, 4));
        playerGroupList.enabled = false;
        groupList.enabled = false;

        buttonList.add(playerColor = new GuiButtonColorGrid(background, 1000, GUI_CENTER_X + 130, GUI_CENTER_Y + 52, 4, 4));
        buttonList.add(groupColor = new GuiButtonColorGrid(background, 1001, GUI_CENTER_X + 9, GUI_CENTER_Y + 205, 1, 16));

        buttonList.add(playerSearchClear = new GuiButtonOther(background, 10000, GUI_CENTER_X + 135, GUI_CENTER_Y + 23, 7, 7, 176, 24));
        buttonList.add(playerGroupDropdown = new GuiButtonOther(background, 10001, GUI_CENTER_X + 113, GUI_CENTER_Y + 137, 7, 7, 176, 151));
        buttonList.add(groupEditorDropdown = new GuiButtonOther(background, 10002, GUI_CENTER_X + 113, GUI_CENTER_Y + 177, 7, 7, 176, 151));

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

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        super.keyTyped(typedChar, keyCode);
    }

    // Copy default mouse behavior but break if a clicked button returns true
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (activeTextBox != null) activeTextBox.setFocused(false);

            for (GuiButton button : this.buttonList) {
                if (button.enabled && button.mousePressed(mc, mouseX, mouseY)) {
                    net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(this, button, this.buttonList);
                    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) break;
                    // In case the event is modified to change the button, I guess
                    GuiButton guiButton = event.getButton();

                    this.selectedButton = guiButton;
                    guiButton.playPressSound(mc.getSoundHandler());
                    this.actionPerformed(guiButton);

                    if (this.equals(mc.currentScreen)) {
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
                    }

                    activeTextBox = null;
                    return;
                }
            }

            for (GuiTextField textField : this.textBoxes) {
                if (textField.mouseClicked(mouseX, mouseY, mouseButton)) {
                    activeTextBox = textField;
                    return;
                }
            }

            activeTextBox = null;
        }

    }

    public void handleItemRemoved(Object o){
        if (o instanceof IffPlayer) {
            Iffmod.LOGGER.info("This worked");
        }

    }

}
