package fuzs.iteminteractionscore.impl.capability;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface EnderChestMenuCapability extends CapabilityComponent {

    void setEnderChestMenu(AbstractContainerMenu menu);

    AbstractContainerMenu getEnderChestMenu();
}
