package fuzs.puzzlesapi.impl.iteminteractions.client.handler;

import fuzs.puzzlesapi.impl.iteminteractions.client.core.HeldActivationType;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.KeyBackedActivationType;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.KeyMappingProvider;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class KeyBindingTogglesHandler {
    public static final HeldActivationType SHIFT = HeldActivationType.of("Shift", Screen::hasShiftDown);
    public static final HeldActivationType CONTROL = HeldActivationType.of("Control", Minecraft.ON_OSX ? "Cmd" : "Ctrl", Screen::hasControlDown);
    public static final HeldActivationType ALT = HeldActivationType.of("Alt", Screen::hasAltDown);
    public static final HeldActivationType ALWAYS = HeldActivationType.of("Always", () -> true);
    public static final KeyBackedActivationType VISUAL_ITEM_CONTENTS_KEY = new KeyBackedActivationType("toggleVisualItemContents");
    public static final KeyBackedActivationType SELECTED_ITEM_TOOLTIPS_KEY = new KeyBackedActivationType("toggleSelectedItemTooltips");
    public static final KeyBackedActivationType CARRIED_ITEM_TOOLTIPS_KEY = new KeyBackedActivationType("toggleCarriedItemTooltips");

    public static EventResult onBeforeKeyPressed(AbstractContainerScreen<?> screen, int keyCode, int scanCode, int modifiers) {
        for (KeyMappingProvider provider : HeldActivationType.getKeyMappingProviders().toList()) {
            if (provider.keyPressed(keyCode, scanCode)) return EventResult.INTERRUPT;
        }
        return EventResult.PASS;
    }
}
