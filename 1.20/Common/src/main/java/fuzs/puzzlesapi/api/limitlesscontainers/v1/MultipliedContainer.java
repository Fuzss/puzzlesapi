package fuzs.puzzlesapi.api.limitlesscontainers.v1;

import net.minecraft.world.Container;

public interface MultipliedContainer extends Container {

    @Override
    default int getMaxStackSize() {
        return Container.super.getMaxStackSize() * this.getStackSizeMultiplier();
    }

    int getStackSizeMultiplier();
}
