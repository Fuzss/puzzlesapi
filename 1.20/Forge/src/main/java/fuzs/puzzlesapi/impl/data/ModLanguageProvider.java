package fuzs.puzzlesapi.impl.data;

import fuzs.puzzlesapi.api.client.iteminteractions.v1.tooltip.ExpandableClientTooltipComponent;
import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.HeldActivationType;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.KeyBackedActivationType;
import fuzs.puzzlesapi.impl.iteminteractions.client.handler.KeyBindingTogglesHandler;
import fuzs.puzzlesapi.impl.slotcycling.client.handler.CyclingInputHandler;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    protected void addTranslations() {
        // Item Interactions
        this.add(ExpandableClientTooltipComponent.REVEAL_CONTENTS_TRANSLATION_KEY, "%s %s to reveal contents");
        this.add(HeldActivationType.TOOLTIP_HOLD_TRANSLATION_KEY, "Hold");
        this.add(KeyBackedActivationType.TOOLTIP_PRESS_TRANSLATION_KEY, "Press");
        this.add(KeyBindingTogglesHandler.VISUAL_ITEM_CONTENTS_KEY.getKeyMapping(), "Toggle Visual Item Contents");
        this.add(KeyBindingTogglesHandler.SELECTED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Selected Item Tooltips");
        this.add(KeyBindingTogglesHandler.CARRIED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Carried Item Tooltips");
        this.add("key.categories." + PuzzlesApi.MOD_ID, PuzzlesApi.MOD_NAME);
        // Slot Cycling
        this.add(CyclingInputHandler.CYCLE_LEFT_KEY_MAPPING, "Cycle Hotbar Slot Left");
        this.add(CyclingInputHandler.CYCLE_RIGHT_KEY_MAPPING, "Cycle Hotbar Slot Right");
    }
}
