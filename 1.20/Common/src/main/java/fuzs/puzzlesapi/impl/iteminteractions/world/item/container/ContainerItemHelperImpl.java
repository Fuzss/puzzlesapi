package fuzs.puzzlesapi.impl.iteminteractions.world.item.container;

import fuzs.puzzlesapi.api.iteminteractions.v1.ContainerItemHelper;
import fuzs.puzzlesapi.api.iteminteractions.v1.provider.ItemContainerProvider;
import fuzs.puzzlesapi.mixin.iteminteractions.accessor.BundleItemAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class ContainerItemHelperImpl implements ContainerItemHelper {

    @Override
    public @Nullable ItemContainerProvider getItemContainerProvider(ItemStack stack) {
        return ItemContainerProviders.INSTANCE.get(stack);
    }

    @Override
    public SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, IntFunction<SimpleContainer> containerFactory, boolean allowSaving, String nbtKey) {
        CompoundTag tag = provider.getItemContainerData(stack);
        ListTag items = null;
        if (tag != null && tag.contains(nbtKey)) {
            items = tag.getList(nbtKey, 10);
        }
        SimpleContainer simpleContainer = containerFactory.apply(items != null ? items.size() : 0);
        if (items != null) {
            simpleContainer.fromTag(items);
        }
        if (allowSaving) {
            simpleContainer.addListener(container -> {
                ListTag itemsTag = ((SimpleContainer) container).createTag();
                provider.setItemContainerData(stack, itemsTag, nbtKey);
            });
        }
        return simpleContainer;
    }

    @Override
    public int getItemWeight(ItemStack stack) {
        return BundleItemAccessor.puzzlesapi$getWeight(stack);
    }

    @Override
    public NonNullList<ItemStack> getListFromContainer(SimpleContainer container) {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.add(container.getItem(i));
        }
        return items;
    }

    @Override
    public float[] getBackgroundColor(@Nullable DyeColor backgroundColor) {
        if (backgroundColor == null) {
            return new float[]{1.0F, 1.0F, 1.0F};
        } else if (backgroundColor == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            return backgroundColor.getTextureDiffuseColors();
        }
    }
}
