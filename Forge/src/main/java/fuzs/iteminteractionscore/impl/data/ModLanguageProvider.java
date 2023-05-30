package fuzs.iteminteractionscore.impl.data;

import fuzs.iteminteractionscore.api.client.container.v1.tooltip.ExpandableClientTooltipComponent;
import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.client.core.HeldActivationType;
import fuzs.iteminteractionscore.impl.client.core.KeyBackedActivationType;
import fuzs.iteminteractionscore.impl.client.handler.KeyBindingTogglesHandler;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    protected void addTranslations() {
        this.add(ExpandableClientTooltipComponent.REVEAL_CONTENTS_TRANSLATION_KEY, "%s %s to reveal contents");
        this.add(HeldActivationType.TOOLTIP_HOLD_TRANSLATION_KEY, "Hold");
        this.add(KeyBackedActivationType.TOOLTIP_PRESS_TRANSLATION_KEY, "Press");
        this.add(KeyBindingTogglesHandler.VISUAL_ITEM_CONTENTS_KEY.getKeyMapping(), "Toggle Visual Item Contents");
        this.add(KeyBindingTogglesHandler.SELECTED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Selected Item Tooltips");
        this.add(KeyBindingTogglesHandler.CARRIED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Carried Item Tooltips");
        this.add("key.categories." + ItemInteractionsCore.MOD_ID, ItemInteractionsCore.MOD_NAME);
    }
}
