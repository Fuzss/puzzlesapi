package fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand;

import fuzs.puzzlesapi.api.client.statues.v1.gui.components.TickBoxButton;
import fuzs.puzzlesapi.api.statues.v1.network.client.data.DataSyncHandler;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.ArmorStandHolder;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandStyleOption;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandStyleOptions;
import net.minecraft.Util;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;

import java.util.stream.Stream;

public class ArmorStandStyleScreen extends ArmorStandTickBoxScreen<ArmorStandStyleOption> {

    public ArmorStandStyleScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
    }

    @Override
    protected ArmorStandStyleOption[] getAllTickBoxValues() {
        return Stream.of(ArmorStandStyleOptions.values())
                .filter(option -> option.allowChanges(this.minecraft.player))
                .toArray(ArmorStandStyleOption[]::new);
    }

    @Override
    protected AbstractWidget makeTickBoxWidget(ArmorStand armorStand, int buttonStartY, int index, ArmorStandStyleOption option) {
        return Util.make(new TickBoxButton(this.leftPos + 96, this.topPos + buttonStartY + index * 22, 6, 76, Component.translatable(option.getTranslationKey()), option.getOption(armorStand), (Button button) -> {
            this.dataSyncHandler.sendStyleOption(option, ((TickBoxButton) button).isSelected());
        }), widget -> {
            widget.setTooltip(Tooltip.create(Component.translatable(option.getDescriptionKey())));
        });
    }

    @Override
    protected void syncNameChange(String input) {
        this.dataSyncHandler.sendName(input);
    }

    @Override
    protected int getNameMaxLength() {
        return 50;
    }

    @Override
    protected String getNameDefaultValue() {
        return this.holder.getArmorStand().getName().getString();
    }

    @Override
    protected Component getNameComponent() {
        return Component.translatable(ArmorStandStyleOption.TEXT_BOX_TRANSLATION_KEY);
    }

    @Override
    public ArmorStandScreenType getScreenType() {
        return ArmorStandScreenType.STYLE;
    }
}
