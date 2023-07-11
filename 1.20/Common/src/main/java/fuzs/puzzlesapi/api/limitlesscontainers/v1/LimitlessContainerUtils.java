package fuzs.puzzlesapi.api.limitlesscontainers.v1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

public class LimitlessContainerUtils {

    public static CompoundTag saveAllItems(CompoundTag tag, NonNullList<ItemStack> items, boolean saveEmpty) {
        ListTag list = saveAllItems(items::get, items.size());

        if (!list.isEmpty() || saveEmpty) {
            tag.put("Items", list);
        }

        return tag;
    }

    public static ListTag saveAllItems(IntFunction<ItemStack> extractor, int containerSize) {
        ListTag list = new ListTag();

        for (int i = 0; i < containerSize; ++i) {
            ItemStack itemStack = extractor.apply(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte) i);
                itemStack.save(compoundTag);
                compoundTag.putInt("Count", itemStack.getCount());
                list.add(compoundTag);
            }
        }

        return list;
    }

    public static void loadAllItems(CompoundTag tag, NonNullList<ItemStack> items) {
        loadAllItems(tag.getList("Items", Tag.TAG_COMPOUND), items::set, items.size());
    }

    public static void loadAllItems(ListTag list, BiConsumer<Integer, ItemStack> consumer, int containerSize) {
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundTag = list.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if (j < containerSize) {
                ItemStack itemStack = ItemStack.of(compoundTag);
                itemStack.setCount(compoundTag.getInt("Count"));
                consumer.accept(j, itemStack);
            }
        }
    }

    public static void dropContents(Level level, BlockPos pos, Container inventory) {
        dropContents(level, pos.getX(), pos.getY(), pos.getZ(), inventory);
    }

    private static void dropContents(Level level, double x, double y, double z, Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack item = inventory.getItem(i);
            dropItemStack(level, x, y, z, item);
        }
    }

    public static void dropItemStack(Level level, double x, double y, double z, ItemStack stack) {
        double d = EntityType.ITEM.getWidth();
        double e = 1.0 - d;
        double f = d / 2.0;
        double g = Math.floor(x) + level.random.nextDouble() * e + f;
        double h = Math.floor(y) + level.random.nextDouble() * e;
        double i = Math.floor(z) + level.random.nextDouble() * e + f;

        while (!stack.isEmpty()) {
            // don't split stacks into smaller parts like vanilla, keep them as big as possible
            ItemEntity itemEntity = new ItemEntity(level, g, h, i, stack.split(stack.getMaxStackSize()));
            // remove any motion, will help with lag
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
        }
    }

    public static int getMaxStackSizeOrDefault(ItemStack stack, int stackSizeMultiplier) {
        return getMaxStackSize(stack, stackSizeMultiplier).orElseGet(stack::getMaxStackSize);
    }

    public static OptionalInt getMaxStackSize(ItemStack stack, int stackSizeMultiplier) {
        return stack.getMaxStackSize() > 1 || !stack.isDamageableItem() ? OptionalInt.of(stack.getMaxStackSize() * stackSizeMultiplier) : OptionalInt.empty();
    }

    public static void getQuickCraftSlotCount(Set<Slot> dragSlots, int dragMode, ItemStack stack, int slotStackSize, Slot slot) {
        switch (dragMode) {
            case 0:
                stack.setCount(Mth.floor((float) stack.getCount() / (float) dragSlots.size()));
                break;
            case 1:
                stack.setCount(1);
                break;
            case 2:
                stack.setCount(slot.getMaxStackSize(stack));
        }

        stack.grow(slotStackSize);
    }

    public static boolean canItemQuickReplace(@Nullable Slot slot, ItemStack stack, boolean stackSizeMatters) {
        boolean bl = slot == null || !slot.hasItem();
        if (!bl && ItemStack.isSameItemSameTags(stack, slot.getItem())) {
            return slot.getItem().getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slot.getMaxStackSize(stack);
        } else {
            return bl;
        }
    }

    public static int getRedstoneSignalFromBlockEntity(@Nullable BlockEntity blockEntity) {
        return blockEntity instanceof MultipliedContainer container ? getRedstoneSignalFromContainer(container) : 0;
    }

    public static int getRedstoneSignalFromContainer(@Nullable MultipliedContainer container) {
        if (container == null) {
            return 0;
        } else {
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < container.getContainerSize(); ++j) {
                ItemStack itemStack = container.getItem(j);
                if (!itemStack.isEmpty()) {
                    f += (float) itemStack.getCount() / Math.min(container.getMaxStackSize(), getMaxStackSizeOrDefault(itemStack, container.getStackSizeMultiplier()));
                    ++i;
                }
            }

            f /= (float) container.getContainerSize();
            return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }

    public static void placeItemBackInPlayerInventory(Player player, ItemStack itemStack) {
        while (itemStack.getCount() > itemStack.getMaxStackSize()) {
            player.getInventory().placeItemBackInInventory(itemStack);
        }
        player.getInventory().placeItemBackInInventory(itemStack);
    }

    public static void dropPlayerItem(Player player, ItemStack itemStack, boolean includeThrowerName) {
        while (itemStack.getCount() > itemStack.getMaxStackSize()) {
            player.drop(itemStack.split(itemStack.getMaxStackSize()), includeThrowerName);
        }
        player.drop(itemStack, includeThrowerName);
    }
}
