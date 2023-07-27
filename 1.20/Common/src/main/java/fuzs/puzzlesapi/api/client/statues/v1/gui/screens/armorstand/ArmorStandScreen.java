package fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand;

import fuzs.puzzlesapi.api.statues.v1.network.client.data.DataSyncHandler;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.ArmorStandHolder;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.ArmorStandMenu;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandScreenType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;

public interface ArmorStandScreen {

    ArmorStandHolder getHolder();

    ArmorStandScreenType getScreenType();

    <T extends Screen & MenuAccess<ArmorStandMenu> & ArmorStandScreen> T createScreenType(ArmorStandScreenType screenType);

    void setMouseX(int mouseX);

    void setMouseY(int mouseY);

    DataSyncHandler getDataSyncHandler();

    default void renderArmorStandInInventory(GuiGraphics guiGraphics, int posX, int posY, int scale, float mouseX, float mouseY) {
        AbstractArmorStandScreen.armorStandRenderer.renderEntityInInventory(guiGraphics, posX, posY, scale, mouseX, mouseY, this.getHolder().getArmorStand());
    }
}
