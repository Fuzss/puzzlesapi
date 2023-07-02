package fuzs.puzzlesapi.impl.slotcycling.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.puzzlesapi.api.client.slotcycling.v1.ItemCyclingProvider;
import fuzs.puzzlesapi.api.client.slotcycling.v1.SlotCyclingProvider;
import fuzs.puzzlesapi.impl.slotcycling.SlotCycling;
import fuzs.puzzlesapi.impl.slotcycling.config.ClientConfig;
import fuzs.puzzlesapi.impl.slotcycling.config.ModifierKey;
import fuzs.puzzlesapi.mixin.slotcycling.client.accessor.ItemInHandRendererAccessor;
import fuzs.puzzlesapi.mixin.slotcycling.client.accessor.MouseHandlerAccessor;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class CyclingInputHandler {
    public static final KeyMapping CYCLE_LEFT_KEY_MAPPING = new KeyMapping("key.cycleLeft", InputConstants.KEY_G, "key.categories." + SlotCycling.MOD_ID);
    public static final KeyMapping CYCLE_RIGHT_KEY_MAPPING = new KeyMapping("key.cycleRight", InputConstants.KEY_H, "key.categories." + SlotCycling.MOD_ID);
    private static final int DEFAULT_SLOTS_DISPLAY_TICKS = 15;

    private static int slotsDisplayTicks;
    private static int globalPopTime;

    public static EventResult onBeforeMouseScroll(boolean leftDown, boolean middleDown, boolean rightDown, double horizontalAmount, double verticalAmount) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (!player.isSpectator() && SlotCycling.CONFIG.get(ClientConfig.class).scrollingModifierKey.isActive()) {
            double totalScroll = verticalAmount + ((MouseHandlerAccessor) minecraft.mouseHandler).essentialpotions$getAccumulatedScroll();
            if (totalScroll > 0.0) {
                cycleSlot(minecraft, player, SlotCyclingProvider::cycleSlotBackward);
            } else if (totalScroll < 0.0) {
                cycleSlot(minecraft, player, SlotCyclingProvider::cycleSlotForward);
            }
            return EventResult.INTERRUPT;
        }
        return EventResult.PASS;
    }

    public static void onClientTick$Start(Minecraft minecraft) {
        if (slotsDisplayTicks > 0) slotsDisplayTicks--;
        if (globalPopTime > 0) globalPopTime--;
        if (minecraft.player != null && !minecraft.player.isSpectator()) {
            if (minecraft.getOverlay() == null && minecraft.screen == null) {
                handleModKeybinds(minecraft, minecraft.player);
                handleHotbarKeybinds(minecraft, minecraft.player, minecraft.options);
            }
            if (SlotCycling.CONFIG.get(ClientConfig.class).scrollingModifierKey.isActive()) {
                slotsDisplayTicks = DEFAULT_SLOTS_DISPLAY_TICKS;
            }
        }
    }

    private static void handleModKeybinds(Minecraft minecraft, Player player) {
        while (CYCLE_LEFT_KEY_MAPPING.consumeClick()) {
            cycleSlot(minecraft, player, SlotCyclingProvider::cycleSlotBackward);
        }
        while (CYCLE_RIGHT_KEY_MAPPING.consumeClick()) {
            cycleSlot(minecraft, player, SlotCyclingProvider::cycleSlotForward);
        }
    }

    private static void handleHotbarKeybinds(Minecraft minecraft, Player player, Options options) {
        if (!SlotCycling.CONFIG.get(ClientConfig.class).doublePressHotbarKey) return;
        boolean saveHotbarActivatorDown = options.keySaveHotbarActivator.isDown();
        boolean loadHotbarActivatorDown = options.keyLoadHotbarActivator.isDown();
        if (!player.isCreative() || !loadHotbarActivatorDown && !saveHotbarActivatorDown) {
            ModifierKey scrollingModifierKey = SlotCycling.CONFIG.get(ClientConfig.class).scrollingModifierKey;
            boolean forward = !scrollingModifierKey.isKey() || !scrollingModifierKey.isActive();
            for (int i = 0; i < options.keyHotbarSlots.length; i++) {
                while (i == player.getInventory().selected && options.keyHotbarSlots[i].consumeClick()) {
                    cycleSlot(minecraft, player, forward ? SlotCyclingProvider::cycleSlotForward : SlotCyclingProvider::cycleSlotBackward);
                }
            }
        }
    }

    private static void cycleSlot(Minecraft minecraft, Player player, Predicate<SlotCyclingProvider> cycleAction) {
        SlotCyclingProvider provider = SlotCyclingProvider.getProvider(player);
        if (provider != null && cycleAction.test(provider)) {
            slotsDisplayTicks = DEFAULT_SLOTS_DISPLAY_TICKS;
            globalPopTime = 5;
            player.stopUsingItem();
            if (provider instanceof ItemCyclingProvider itemProvider) {
                clearItemRendererInHand(minecraft, itemProvider.interactionHand());
            }
        }
    }

    private static void clearItemRendererInHand(Minecraft minecraft, InteractionHand interactionHand) {
        // force the reequip animation for the new held item
        ItemInHandRenderer itemInHandRenderer = minecraft.gameRenderer.itemInHandRenderer;
        if (interactionHand == InteractionHand.OFF_HAND) {
            ((ItemInHandRendererAccessor) itemInHandRenderer).essentialpotions$setOffHandItem(ItemStack.EMPTY);
        } else {
            ((ItemInHandRendererAccessor) itemInHandRenderer).essentialpotions$setMainHandItem(ItemStack.EMPTY);
        }
    }

    public static int getSlotsDisplayTicks() {
        return slotsDisplayTicks;
    }

    public static int getGlobalPopTime() {
        return globalPopTime;
    }
}
