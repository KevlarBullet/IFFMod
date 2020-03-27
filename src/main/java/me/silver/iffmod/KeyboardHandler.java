package me.silver.iffmod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyboardHandler {

    public static KeyBinding secureScreenshot = new KeyBinding("key.iff_secure_screenshot", Keyboard.KEY_P, "IFFMod");

    public final KeyBinding[] keys = {
        secureScreenshot
    };

    public void registerKeyBindings() {
        for (KeyBinding keyBinding : this.keys) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }
    }

    @SubscribeEvent
    public void keyInputEvent(InputEvent.KeyInputEvent event) {
        for (KeyBinding keyBinding : this.keys) {
            if (keyBinding.isPressed()) {
                Iffmod.getInstance().handleKeyInput(keyBinding);
            }
        }
    }
}
