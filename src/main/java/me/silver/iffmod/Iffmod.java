package me.silver.iffmod;

import me.silver.iffmod.config.GroupConfig;
import me.silver.iffmod.config.PlayerConfig;
import me.silver.iffmod.config.json.JSONConfig;
import me.silver.iffmod.config.json.JSONHandler;
import me.silver.iffmod.gui.GuiNameEditor;
import me.silver.iffmod.util.EventListener;
import me.silver.iffmod.util.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    public JSONConfig playerConfig;
    public JSONConfig groupConfig;

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

        MinecraftForge.EVENT_BUS.register(new EventListener());
        MinecraftForge.EVENT_BUS.register(handler);
    }

    // TODO: Make this server-specific instead of universal
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        playerConfig = new PlayerConfig(getDataPath(), "players.json");
        groupConfig = new GroupConfig(getDataPath(), "groups.json");

        playerConfig.loadConfig();
        groupConfig.loadConfig();

    }

    public void togglePlayerColors(boolean shouldDisplayColors) {
        if (Minecraft.getMinecraft().getConnection() != null) {
            GuiPlayerTabOverlay tabOverlay = Minecraft.getMinecraft().ingameGUI.getTabList();

            for (NetworkPlayerInfo npi : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
//                Iffmod.LOGGER.info(npi.getGameProfile().getName() + " - " + tabOverlay.getPlayerName(npi));
                npi.setDisplayName(new TextComponentString(TextFormatting.DARK_BLUE + npi.getGameProfile().getName()));
            }

            this.doNameReset = !shouldDisplayColors;

            for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                player.refreshDisplayName();
            }
        }
    }

    public void handleKeyInput(KeyBinding key) {
        // Check if the player is in a game and does not already have a GUI open
        if (!(Minecraft.getMinecraft().world == null) && Minecraft.getMinecraft().currentScreen == null) {
            if (key == KeyboardHandler.openGui) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiNameEditor());
            } else if (key == KeyboardHandler.secureScreenshot) {
                JSONArray array = new JSONArray();

                for (EntityPlayer player : mc.world.playerEntities) {
                    JSONObject playerObject = new JSONObject();
                    playerObject.put("playerName", player.getName());

                    array.add(playerObject);
                }

                JSONHandler.writeFile(getDataPath(), "test.json", array);
//                this.resetPlayerNames();
//
//                // Add 1 tick of delay somehow
//
//                this.mc.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(
//                        this.mc.gameDir, this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer()));
            }
        }
    }

    public boolean shouldResetNames() {
        return this.doNameReset;
    }

    public static String getDataPath() {
        return "mods/" + MODID + "/";
    }

}
