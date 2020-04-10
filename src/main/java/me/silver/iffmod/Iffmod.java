package me.silver.iffmod;

import me.silver.iffmod.gui.GuiNameEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = Iffmod.MODID, name = Iffmod.NAME, version = Iffmod.VERSION)
public class Iffmod {
    public static final String MODID = "iffmod";
    public static final String NAME = "IFFMod";
    public static final String VERSION = "1.0";

    public static Logger LOGGER;

    private boolean doNameReset = false;

    public Map<String, String> originalNames = new HashMap<>();
    public Map<String, TextComponentString> modifiedNames = new HashMap<>();

    public Minecraft mc;

    public static Iffmod getInstance() {
        return INSTANCE;
    }

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MODID)
    private static Iffmod INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        this.mc = Minecraft.getMinecraft();

        // some example code
        KeyboardHandler handler = new KeyboardHandler();
        handler.registerKeyBindings();

//        MinecraftForge.EVENT_BUS.register(new EventListener());
        MinecraftForge.EVENT_BUS.register(handler);
    }

    public void resetPlayerNames() {
        if (Minecraft.getMinecraft().getConnection() != null) {
            GuiPlayerTabOverlay tabOverlay = Minecraft.getMinecraft().ingameGUI.getTabList();

            for (NetworkPlayerInfo npi : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
//                Iffmod.LOGGER.info(npi.getGameProfile().getName() + " - " + tabOverlay.getPlayerName(npi));
                npi.setDisplayName(new TextComponentString(TextFormatting.DARK_BLUE + npi.getGameProfile().getName()));
            }

            this.doNameReset = true;
            for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                player.refreshDisplayName();
            }
            this.doNameReset = false;
        }
    }

    public void handleKeyInput(KeyBinding key) {
        // Check if the player is in a game and does not already have a GUI open
        if (!(Minecraft.getMinecraft().world == null) && Minecraft.getMinecraft().currentScreen == null) {
            if (key == KeyboardHandler.openGui) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiNameEditor());
            } else if (key == KeyboardHandler.secureScreenshot) {
                this.resetPlayerNames();

                // Add 1 tick of delay somehow

                this.mc.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(
                        this.mc.gameDir, this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer()));
            }
        }
    }

    public boolean shouldResetNames() {
        return this.doNameReset;
    }

}
