package me.silver.iffmod;

import me.silver.iffmod.config.GroupConfig;
import me.silver.iffmod.config.PlayerConfig;
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

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod(modid = IffMod.MODID, name = IffMod.NAME, version = IffMod.VERSION)
public class IffMod {
    public static final String MODID = "iffmod";
    public static final String NAME = "IFFMod";
    public static final String VERSION = "1.0";

    public static IffMod getInstance() {
        return INSTANCE;
    }
    public static Logger LOGGER;

    private final Pattern PATTERN = Pattern.compile("(\\u00a7[0-9a-f])(\\[\\w+] )?(\\u00a7[0-9a-f])?(\\w{3,})");
//    private final Pattern PATTERN = Pattern.compile("(?i)\\u00a7[0-9A-FK-OR]");
    private boolean doNameReset = false;
    private boolean isReady = false;

    private HashMap<String, String> originalNames;

    public Minecraft mc;
    public PlayerConfig playerConfig;
    public GroupConfig groupConfig;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MODID)
    private static IffMod INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        this.mc = Minecraft.getMinecraft();

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

        this.load();
    }

    public void load() {
        if (!this.isReady) {
            playerConfig.loadConfig();
            groupConfig.loadConfig();

            if (originalNames == null) originalNames = new HashMap<>();

            this.isReady = true;
        }
    }

    public void unload() {
        if (this.isReady) {
            playerConfig.saveConfig();
            groupConfig.saveConfig();

            originalNames.clear();

            this.isReady = false;
        }
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
            } else if (key == KeyboardHandler.toggleColorDisplay) {
                this.doNameReset = !doNameReset;

                for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                    player.refreshDisplayName();
                }
            }
        }
    }

    public void fixTabNames() {
        if (Minecraft.getMinecraft().getConnection() != null) {
            HashMap<String, String> originalNameMap = new HashMap<>();

            // Set tab list names (Thanks, Einstein)
            GuiPlayerTabOverlay tabOverlay = Minecraft.getMinecraft().ingameGUI.getTabList();

            for (NetworkPlayerInfo npi : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
                String username = npi.getGameProfile().getName();
                String tabName = tabOverlay.getPlayerName(npi);

                String originalName = this.originalNames.get(username);

                if (originalName != null) {
                    originalNameMap.put(username, originalName);
                } else {
                    originalNameMap.put(username, originalName = tabName);
                }

                if (doNameReset) {
                    npi.setDisplayName(new TextComponentString(originalName));
                } else {
                    if (playerConfig.get(username) != null)
                        npi.setDisplayName(new TextComponentString(getIffName(username, tabName)));
                }
            }

            this.originalNames = originalNameMap;

        }

    }

    public String getIffName(String username, String displayName) {
        if (this.playerConfig.get(username) != null) {
            Matcher matcher = PATTERN.matcher(displayName);

            if (matcher.find()) {
                IffPlayer player = this.playerConfig.get(username);
                String prefix = matcher.group(1);
                if (matcher.group(2) != null) prefix += matcher.group(2);
//                LOGGER.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//                LOGGER.info(displayName);
//                LOGGER.info(matcher.group(1));
//                LOGGER.info(matcher.group(2));
//                LOGGER.info(matcher.group(3));

                StringBuilder newName = new StringBuilder();
                newName.append(prefix)
                        .append(TextFormatting.fromColorIndex(player.getColorIndex()))
                        .append(username);

                if (!player.getGroup().isEmpty() && groupConfig.get(player.getGroup()) != null) {
                    IffGroup group = groupConfig.get(player.getGroup());

                    newName.append(" ")
                            .append(TextFormatting.fromColorIndex(group.getDefaultColorIndex()))
                            .append("[")
                            .append(group.getGroupName())
                            .append("]");
                }

                return newName.toString();
            }
        }

        return displayName;
    }

    public boolean shouldResetNames() {
        return this.doNameReset;
    }

    public static String getDataPath() {
        return "mods/" + MODID + "/";
    }

}
