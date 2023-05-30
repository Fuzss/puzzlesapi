package fuzs.iteminteractionscore.impl.capability;

import net.minecraft.world.inventory.AbstractContainerMenu;

public class EnderChestMenuCapabilityImpl implements EnderChestMenuCapability {
    private AbstractContainerMenu menu;

    @Override
    public void setEnderChestMenu(AbstractContainerMenu menu) {
        this.menu = menu;
    }

    @Override
    public AbstractContainerMenu getEnderChestMenu() {
        return this.menu;
    }
}
