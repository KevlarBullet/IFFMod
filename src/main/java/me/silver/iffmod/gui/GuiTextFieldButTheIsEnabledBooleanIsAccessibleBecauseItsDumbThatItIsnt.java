package me.silver.iffmod.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldButTheIsEnabledBooleanIsAccessibleBecauseItsDumbThatItIsnt extends GuiTextField {

    private boolean isEnabled = true;

    public GuiTextFieldButTheIsEnabledBooleanIsAccessibleBecauseItsDumbThatItIsnt(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        super.setEnabled(enabled);
    }

    public boolean getEnabled() {
        return isEnabled;
    }
}
