package me.silver.iffmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;

@Mod(
        modid = Iffmod.MOD_ID,
        name = Iffmod.MOD_NAME,
        version = Iffmod.VERSION
)
public class Iffmod {

    public static final String MOD_ID = "iffmod";
    public static final String MOD_NAME = "Iffmod";
    public static final String VERSION = "2020.1-1.3.4";

    public static final Logger LOGGER = LogManager.getLogger(Iffmod.MOD_ID);

    private boolean doNameReset = false;
    private KeyboardHandler keyboardHandler;

    public Map<String, String> originalNames = new HashMap<>();
    public Map<String, String> modifiedNames = new HashMap<>();

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    private static Iffmod INSTANCE;

    public static Iffmod getInstance() {
        return INSTANCE;
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        this.keyboardHandler = new KeyboardHandler();
        this.keyboardHandler.registerKeyBindings();

        MinecraftForge.EVENT_BUS.register(new EventListener());
        MinecraftForge.EVENT_BUS.register(this.keyboardHandler);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        INSTANCE = this;
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

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

    }

    public boolean shouldResetNames() {
        return this.doNameReset;
    }

}
