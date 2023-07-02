package fuzs.puzzlesapi.api.client.slotcycling.v1;

import com.google.common.collect.Maps;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public interface SlotCyclingProvider {
    Factory[] GLOBAL_PROVIDER = new Factory[1];
    Map<Item, ItemCyclingProvider.Factory> ITEM_PROVIDERS = Maps.newIdentityHashMap();

    static void registerProvider(@Nullable SlotCyclingProvider.Factory factory) {
        GLOBAL_PROVIDER[0] = factory;
    }

    static void registerProvider(Item item, ItemCyclingProvider.Factory factory) {
        Objects.requireNonNull(item, "item is null");
        Objects.requireNonNull(factory, "factory is null");
        if (ITEM_PROVIDERS.put(item, factory) != null) {
            throw new IllegalStateException("Duplicate item cycling provider for item " + item);
        }
    }

    @Nullable
    static SlotCyclingProvider getProvider(Player player) {
        for (Map.Entry<Item, ItemCyclingProvider.Factory> entry : ITEM_PROVIDERS.entrySet()) {
            for (InteractionHand interactionHand : InteractionHand.values()) {
                ItemStack itemInHand = player.getItemInHand(interactionHand);
                if (itemInHand.is(entry.getKey())) {
                    return entry.getValue().apply(itemInHand, interactionHand);
                }
            }
        }
        return GLOBAL_PROVIDER[0] != null ? GLOBAL_PROVIDER[0].apply(player) : null;
    }

    @Nullable
    static SlotCyclingProvider getProvider(Player player, InteractionHand interactionHand) {
        for (Map.Entry<Item, ItemCyclingProvider.Factory> entry : ITEM_PROVIDERS.entrySet()) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(entry.getKey())) {
                return entry.getValue().apply(itemInHand, interactionHand);
            }
        }
        return GLOBAL_PROVIDER[0] != null ? GLOBAL_PROVIDER[0].apply(player) : null;
    }

    ItemStack getSelectedStack();

    ItemStack getForwardStack();

    ItemStack getBackwardStack();

    int getForwardSlot();

    int getBackwardSlot();

    boolean cycleSlotForward();

    boolean cycleSlotBackward();

    interface Factory {

        SlotCyclingProvider apply(Player player);
    }
}
