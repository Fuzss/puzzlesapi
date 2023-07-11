package fuzs.puzzlesapi.api.limitlesscontainers.v1;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class MultipliedSimpleSlotContainer extends MultipliedSimpleContainer {

    public MultipliedSimpleSlotContainer(int stackSizeMultiplier, int size) {
        super(stackSizeMultiplier, size);
    }

    public MultipliedSimpleSlotContainer(int stackSizeMultiplier, ItemStack... items) {
        super(stackSizeMultiplier, items);
    }

    @Override
    public void fromTag(ListTag containerNbt) {
        LimitlessContainerUtils.loadAllItems(containerNbt, this::setItem, this.getContainerSize());
    }

    @Override
    public ListTag createTag() {
        return LimitlessContainerUtils.saveAllItems(this::getItem, this.getContainerSize());
    }
}
