package me.silver.iffmod.util;

import me.silver.iffmod.IffGroup;
import me.silver.iffmod.IffMod;
import me.silver.iffmod.IffPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventListener {

    private final IffMod instance = IffMod.getInstance();

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        if (Minecraft.getMinecraft().getConnection() != null) {
            if (!instance.shouldResetNames() && instance.playerConfig.get(event.getUsername()) != null) {
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

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent event) {
        // TODO: Find a better event for this to live in
        if (Minecraft.getMinecraft().world != null) {
            instance.load();
        }
    }

    // Can't really put instance.load() here because it seems to be called before WorldEvent.Unload when switching worlds
    // Event priority doesn't appear to change this
//    @SubscribeEvent(priority = EventPriority.NORMAL)
//    public void worldLoad(WorldEvent.Load event) {
//        IffMod.LOGGER.info(event.getWorld().hashCode());
//    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void worldUnload(WorldEvent.Unload event) {
//        IffMod.LOGGER.info("b");
        instance.unload();
    }

}
