package me.silver.iffmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventListener {

    private final Iffmod instance = Iffmod.getInstance();

//    private final Pattern PATTERN = Pattern.compile("(.[0-9a-f])(\\[[A-Za-z]+])? ?(\\w+)");

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        if (instance.shouldResetNames()) {
            event.setDisplayname(null);
        } else {
            String userName = event.getUsername();
            event.setDisplayname(TextFormatting.DARK_BLUE + userName);
        }

    }

    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().getConnection() != null) {
            // Set tab list names
            if (Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isPressed()) {
                GuiPlayerTabOverlay tabOverlay = Minecraft.getMinecraft().ingameGUI.getTabList();

                for (NetworkPlayerInfo npi : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
                    String username = npi.getGameProfile().getName();
                    String tabName = tabOverlay.getPlayerName(npi);

                    if (!instance.originalNames.containsKey(username)) {
                        instance.originalNames.put(username, tabName);
                    }

                    if (instance.modifiedNames.containsKey(username)) {
                        npi.setDisplayName(instance.modifiedNames.get(username));
                    } else {
                        String rank = "";
                        String[] splitName = tabName.split(" ");

                        if (splitName.length > 1) {
                            rank = splitName[0] + " ";
                        }

                        TextComponentString nameComponent = new TextComponentString(rank + TextFormatting.DARK_BLUE + username);
                        npi.setDisplayName(nameComponent);
                        instance.modifiedNames.put(username, nameComponent);
                    }
                }

            }
        }

    }

}
