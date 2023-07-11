package fuzs.puzzlesapi.api.limitlesscontainers.v1;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface MultipliedContainer extends Container {

    @Override
    default int getMaxStackSize() {
        return Container.super.getMaxStackSize() * this.getStackSizeMultiplier();
    }

    default int getMaxStackSize(ItemStack itemStack) {
        return LimitlessContainerUtils.getMaxStackSize(itemStack, this.getStackSizeMultiplier()).orElseGet(this::getMaxStackSize);
    }

    int getStackSizeMultiplier();
}
