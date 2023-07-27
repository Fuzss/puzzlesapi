package fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand;

import fuzs.puzzlesapi.api.client.statues.v1.gui.components.TickButton;
import fuzs.puzzlesapi.api.statues.v1.network.client.data.DataSyncHandler;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.ArmorStandHolder;
import fuzs.puzzlesapi.impl.statues.Statues;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public abstract class AbstractArmorStandPositionScreen extends ArmorStandWidgetsScreen {
    public static final String CENTERED_TRANSLATION_KEY = Statues.MOD_ID + ".screen.centered";
    public static final String CENTERED_DESCRIPTION_TRANSLATION_KEY = Statues.MOD_ID + ".screen.centered.description";
    public static final String CORNERED_TRANSLATION_KEY = Statues.MOD_ID + ".screen.cornered";
    public static final String CORNERED_DESCRIPTION_TRANSLATION_KEY = Statues.MOD_ID + ".screen.cornered.description";

    public AbstractArmorStandPositionScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
    }

    protected class PositionAlignWidget extends AbstractPositionScreenWidget {

        public PositionAlignWidget() {
            super(Component.empty());
        }

        @Override
        protected boolean shouldTick() {
            return true;
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            this.children.add(Util.make(AbstractArmorStandPositionScreen.this.addRenderableWidget(new TickButton(posX, posY + 1, 94, 20, Component.translatable(CENTERED_TRANSLATION_KEY), Component.translatable(ALIGNED_TRANSLATION_KEY), button -> {
                Vec3 newPosition = AbstractArmorStandPositionScreen.this.holder.getArmorStand().position().align(EnumSet.allOf(Direction.Axis.class)).add(0.5, 0.0, 0.5);
                AbstractArmorStandPositionScreen.this.dataSyncHandler.sendPosition(newPosition.x(), newPosition.y(), newPosition.z());
            })), widget -> {
                widget.setTooltip(Tooltip.create(Component.translatable(CENTERED_DESCRIPTION_TRANSLATION_KEY)));
            }));
            this.children.add(Util.make(AbstractArmorStandPositionScreen.this.addRenderableWidget(new TickButton(posX + 100, posY + 1, 94, 20, Component.translatable(CORNERED_TRANSLATION_KEY), Component.translatable(ALIGNED_TRANSLATION_KEY), button -> {
                Vec3 newPosition = AbstractArmorStandPositionScreen.this.holder.getArmorStand().position().align(EnumSet.allOf(Direction.Axis.class));
                AbstractArmorStandPositionScreen.this.dataSyncHandler.sendPosition(newPosition.x(), newPosition.y(), newPosition.z());
            })), widget -> {
                widget.setTooltip(Tooltip.create(Component.translatable(CORNERED_TRANSLATION_KEY)));
            }));
        }
    }
}
