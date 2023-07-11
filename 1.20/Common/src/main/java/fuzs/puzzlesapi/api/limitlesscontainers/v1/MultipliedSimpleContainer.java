package fuzs.puzzlesapi.api.limitlesscontainers.v1;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class MultipliedSimpleContainer extends SimpleContainer implements MultipliedContainer {
    private final int stackSizeMultiplier;

    public MultipliedSimpleContainer(int stackSizeMultiplier, int size) {
        super(size);
        this.stackSizeMultiplier = stackSizeMultiplier;
    }

    public MultipliedSimpleContainer(int stackSizeMultiplier, ItemStack... items) {
        super(items);
        this.stackSizeMultiplier = stackSizeMultiplier;
    }

    @Override
    public int getStackSizeMultiplier() {
        return this.stackSizeMultiplier;
    }

    @Override
    public boolean canAddItem(ItemStack stack) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack itemStack = this.getItem(i);
            if (itemStack.isEmpty() || ItemStack.isSameItemSameTags(itemStack, stack) && itemStack.getCount() < LimitlessContainerUtils.getMaxStackSizeOrDefault(itemStack, this.getStackSizeMultiplier())) {
                return true;
            }
        }
        return false;
    }
}
