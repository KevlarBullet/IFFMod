package me.silver.iffmod.util;

import me.silver.iffmod.IffGroup;
import me.silver.iffmod.IffMod;
import me.silver.iffmod.IffPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventListener {

    private final IffMod instance = IffMod.getInstance();

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        if (Minecraft.getMinecraft().getConnection() != null) {
            if (instance.playerConfig.get(event.getUsername()) != null) {
                IffPlayer player = instance.playerConfig.get(event.getUsername());

                StringBuilder newName = new StringBuilder();
                newName.append(TextFormatting.fromColorIndex(player.getColorIndex()))
                        .append(event.getUsername());

                if (instance.groupConfig.get(player.getGroup()) != null) {
                    IffGroup group = instance.groupConfig.get(player.getGroup());

                    newName.append(" ")
                            .append(TextFormatting.fromColorIndex(group.getDefaultColorIndex()))
                            .append("[")
                            .append(group.getGroupName())
                            .append("]");
                }

                event.setDisplayname(newName.toString());
            }
        }

    }

}
