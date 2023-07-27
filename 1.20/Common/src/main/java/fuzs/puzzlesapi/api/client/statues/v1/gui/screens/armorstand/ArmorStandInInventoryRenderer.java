package fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;

public interface ArmorStandInInventoryRenderer {
    ArmorStandInInventoryRenderer SIMPLE = InventoryScreen::renderEntityInInventoryFollowsMouse;

    void renderEntityInInventory(GuiGraphics guiGraphics, int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity);

    static void setArmorStandRenderer(ArmorStandInInventoryRenderer armorStandRenderer) {
        AbstractArmorStandScreen.armorStandRenderer = armorStandRenderer;
    }
}
