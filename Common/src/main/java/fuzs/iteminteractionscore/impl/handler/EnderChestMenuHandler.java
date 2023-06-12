package fuzs.iteminteractionscore.impl.handler;

import fuzs.iteminteractionscore.impl.capability.EnderChestMenuCapability;
import fuzs.iteminteractionscore.impl.init.ModRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;

import java.util.Optional;

public class EnderChestMenuHandler {

    public static void onLivingTick(Player player) {
        if (player.level().isClientSide) return;
        // vanilla only syncs ender chest contents to open ender chest menu, but not to Player::getEnderChestInventory
        // but since this is what we use for item interactions make sure to sync it
        if (player.containerMenu instanceof ChestMenu menu && menu.getContainer() == player.getEnderChestInventory()) {
            ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player)
                    .map(EnderChestMenuCapability::getEnderChestMenu)
                    .ifPresent(AbstractContainerMenu::broadcastChanges);
        }
    }

    public static Optional<AbstractContainerMenu> openEnderChestMenu(Player player) {
        return ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(capability -> {
            // container id doesn't matter since we do the syncing ourselves where the id is never used
            ChestMenu menu = ChestMenu.threeRows(-100, new Inventory(player), player.getEnderChestInventory());
            capability.setEnderChestMenu(menu);
            return menu;
        });
    }
}
