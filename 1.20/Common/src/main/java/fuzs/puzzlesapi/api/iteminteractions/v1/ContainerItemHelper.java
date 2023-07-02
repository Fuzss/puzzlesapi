package fuzs.puzzlesapi.api.iteminteractions.v1;

import fuzs.puzzlesapi.api.iteminteractions.v1.provider.ItemContainerProvider;
import fuzs.puzzlesapi.impl.iteminteractions.world.inventory.SimpleSlotContainer;
import fuzs.puzzlesapi.impl.iteminteractions.world.item.container.ContainerItemHelperImpl;
import fuzs.puzzlesapi.impl.iteminteractions.world.item.container.ItemInteractionHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.IntFunction;

public interface ContainerItemHelper {
    ContainerItemHelper INSTANCE = new ContainerItemHelperImpl();

    default ItemContainerProvider getItemContainerProviderOrThrow(ItemStack stack) {
        ItemContainerProvider provider = this.getItemContainerProvider(stack);
        Objects.requireNonNull(provider, "provider is null");
        return provider;
    }

    @Nullable ItemContainerProvider getItemContainerProvider(ItemStack stack);

    default SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, int inventorySize, boolean allowSaving) {
        return this.loadItemContainer(stack, provider, inventorySize, allowSaving, ItemInteractionHelper.TAG_ITEMS);
    }

    default SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, int inventorySize, boolean allowSaving, String nbtKey) {
        return this.loadItemContainer(stack, provider, items -> new SimpleSlotContainer(inventorySize), allowSaving, nbtKey);
    }

    SimpleContainer loadItemContainer(ItemStack stack, ItemContainerProvider provider, IntFunction<SimpleContainer> containerFactory, boolean allowSaving, String nbtKey);

    int getItemWeight(ItemStack stack);

    NonNullList<ItemStack> getListFromContainer(SimpleContainer container);

    float[] getBackgroundColor(@Nullable DyeColor backgroundColor);
}
