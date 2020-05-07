package me.silver.iffmod.util;

import me.silver.iffmod.IffGroup;
import me.silver.iffmod.IffPlayer;
import me.silver.iffmod.Iffmod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventListener {

    private final Iffmod instance = Iffmod.getInstance();

//    private final Pattern PATTERN = Pattern.compile("(\\u00a7[0-9a-f](\\[[A-Za-z]+])?) ?(\\w+)");

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        if (Minecraft.getMinecraft().getConnection() != null) {
            NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(event.getUsername());

            if (playerInfo != null && instance.playerConfig.get(event.getUsername()) != null) {
                String tabName = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(playerInfo);
                event.setDisplayname(instance.getIffName(event.getUsername(), tabName));
            }
        }
//        Iffmod.LOGGER.info("########################################################");
//        Iffmod.LOGGER.info("########################################################");
//
//        Iffmod.LOGGER.info(event.getDisplayname());
//        Iffmod.LOGGER.info(event.getUsername());
//
//        EntityPlayer player = event.getEntityPlayer();
//
////        String whatever = player.getDisplayNameString();
////        String whomever = player.getDisplayName().getFormattedText();
////        String wherever = player.getName();
////        String whenever = player.getCustomNameTag();
//
//        Iffmod.LOGGER.info(player.getDisplayNameString());
//        Iffmod.LOGGER.info(player.getDisplayName().getFormattedText());
//        Iffmod.LOGGER.info(player.getName());
//        Iffmod.LOGGER.info(player.getCustomNameTag());
//
//        Iffmod.LOGGER.info("########################################################");
//        Iffmod.LOGGER.info("########################################################");
//
//        if (instance.shouldResetNames()) {
//            event.setDisplayname(null);
//        } else {
//            String userName = event.getUsername();
//            event.setDisplayname(TextFormatting.DARK_BLUE + userName);
//        }

    }

//    @SubscribeEvent
//    public void keyEvent(InputEvent.KeyInputEvent event) {
//        if (Minecraft.getMinecraft().getConnection() != null) {
//            // Set tab list names
//            if (Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isPressed()) {
//                GuiPlayerTabOverlay tabOverlay = Minecraft.getMinecraft().ingameGUI.getTabList();
//
//                for (NetworkPlayerInfo npi : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
//                    String username = npi.getGameProfile().getName();
//                    String tabName = tabOverlay.getPlayerName(npi);
//
//                    if (instance.playerConfig.get(username) != null) {
//                        Matcher matcher = PATTERN.matcher(tabName);
//                        String prefix = matcher.group(1);
//                        IffPlayer player = (IffPlayer) instance.playerConfig.get(username);
//
//                        StringBuilder newName = new StringBuilder();
//                        newName.append(prefix)
//                                .append(" ")
//                                .append(TextFormatting.fromColorIndex(player.getColorIndex()))
//                                .append(username);
//
//                        if (player.getGroup() != null) {
//                            IffGroup group = player.getGroup();
//
//                            newName.append(" ")
//                                    .append(TextFormatting.fromColorIndex(group.getDefaultColorIndex()))
//                                    .append("[")
//                                    .append(group.getGroupName())
//                                    .append("]");
//                        }
//
//                        npi.setDisplayName(new TextComponentString(newName.toString()));
//                    }
//
//
////                    Iffmod.LOGGER.info("##########################################################");
////                    Iffmod.LOGGER.info(username);
////                    Iffmod.LOGGER.info(tabName);
////                    Iffmod.LOGGER.info("##########################################################");
//
////                    if (!instance.originalNames.containsKey(username)) {
////                        instance.originalNames.put(username, tabName);
////                    }
////
////                    if (instance.modifiedNames.containsKey(username)) {
////                        npi.setDisplayName(instance.modifiedNames.get(username));
////                    } else {
////                        String rank = "";
////                        String[] splitName = tabName.split(" ");
////
////                        if (splitName.length > 1) {
////                            rank = splitName[0] + " ";
////                        }
////
////                        TextComponentString nameComponent = new TextComponentString(rank + TextFormatting.DARK_BLUE + username);
////                        npi.setDisplayName(nameComponent);
////                        instance.modifiedNames.put(username, nameComponent);
////                    }
//                }
//
//            }
//        }
//
//    }

}
