package fuzs.iteminteractionscore.impl.client.handler;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import fuzs.iteminteractionscore.api.container.v1.provider.ItemContainerProvider;
import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.config.ClientConfig;
import fuzs.iteminteractionscore.impl.config.ServerConfig;
import fuzs.iteminteractionscore.impl.world.item.container.ItemContainerProviders;
import fuzs.iteminteractionscore.mixin.client.accessor.AbstractContainerScreenAccessor;
import fuzs.puzzleslib.api.client.screen.v2.ScreenHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class MouseDraggingHandler {
    private static final Set<Slot> CONTAINER_DRAG_SLOTS = Sets.newHashSet();
    @Nullable
    private static ContainerDragType containerDragType;

    public static EventResult onBeforeMousePressed(Screen screen, double mouseX, double mouseY, int button) {
        if (!shouldHandleMouseDragging(screen)) return EventResult.PASS;
        ItemStack carriedStack = ((AbstractContainerScreen<?>) screen).getMenu().getCarried();
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(carriedStack);
        Minecraft minecraft = ScreenHelper.INSTANCE.getMinecraft(screen);
        if (validMouseButton(button) && provider != null && provider.allowsPlayerInteractions(carriedStack, minecraft.player)) {
            Slot slot = ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$callFindSlot(mouseX, mouseY);
            if (slot != null) {
                if (slot.hasItem() && !ClientInputActionHandler.precisionModeAllowedAndActive()) {
                    containerDragType = ContainerDragType.INSERT;
                } else {
                    containerDragType = ContainerDragType.REMOVE;
                }
                CONTAINER_DRAG_SLOTS.clear();
                return EventResult.INTERRUPT;
            }
        }
        containerDragType = null;
        return EventResult.PASS;
    }

    public static EventResult onBeforeMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!shouldHandleMouseDragging(screen)) return EventResult.PASS;
        if (containerDragType != null) {
            if (!validMouseButton(button)) {
                containerDragType = null;
                CONTAINER_DRAG_SLOTS.clear();
                return EventResult.PASS;
            }
            Slot slot = ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$callFindSlot(mouseX, mouseY);
            AbstractContainerMenu menu = ((AbstractContainerScreen<?>) screen).getMenu();
            if (slot != null && menu.canDragTo(slot) && !CONTAINER_DRAG_SLOTS.contains(slot)) {
                ItemStack carriedStack = menu.getCarried();
                ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(carriedStack);
                Objects.requireNonNull(provider, "provider is null");
                Minecraft minecraft = ScreenHelper.INSTANCE.getMinecraft(screen);
                boolean interact = false;
                if (containerDragType == ContainerDragType.INSERT && slot.hasItem() && provider.canAddItem(carriedStack, slot.getItem(), minecraft.player)) {
                    interact = true;
                } else if (containerDragType == ContainerDragType.REMOVE) {
                    boolean normalInteraction = button == InputConstants.MOUSE_BUTTON_RIGHT && !slot.hasItem() && !provider.getItemContainer(carriedStack, minecraft.player, false).isEmpty();
                    if (normalInteraction || slot.hasItem() && ClientInputActionHandler.precisionModeAllowedAndActive()) {
                        interact = true;
                    }
                }
                if (interact) {
                    ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$callSlotClicked(slot, slot.index, button, ClickType.PICKUP);
                    CONTAINER_DRAG_SLOTS.add(slot);
                    return EventResult.INTERRUPT;
                }
            }
        }
        return EventResult.PASS;
    }

    public static EventResult onBeforeMouseRelease(Screen screen, double mouseX, double mouseY, int button) {
        if (!shouldHandleMouseDragging(screen)) return EventResult.PASS;
        if (containerDragType != null) {
            if (validMouseButton(button) && !CONTAINER_DRAG_SLOTS.isEmpty()) {
                if (!ItemInteractionsCore.CONFIG.get(ClientConfig.class).disableInteractionSounds) {
                    // play this manually at the end, we suppress all interaction sounds played while dragging
                    SimpleSoundInstance sound = SimpleSoundInstance.forUI(containerDragType.sound, 0.8F, 0.8F + SoundInstance.createUnseededRandom().nextFloat() * 0.4F);
                    ScreenHelper.INSTANCE.getMinecraft(screen).getSoundManager().play(sound);
                }
                containerDragType = null;
                CONTAINER_DRAG_SLOTS.clear();
                return EventResult.INTERRUPT;
            }
            containerDragType = null;
        }
        CONTAINER_DRAG_SLOTS.clear();
        return EventResult.PASS;
    }

    private static boolean shouldHandleMouseDragging(Screen screen) {
        if (!(screen instanceof AbstractContainerScreen<?>)) return false;
        return ItemInteractionsCore.CONFIG.get(ServerConfig.class).allowMouseDragging;
    }

    private static boolean validMouseButton(int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT) {
            return ClientInputActionHandler.precisionModeAllowedAndActive();
        }
        return button == InputConstants.MOUSE_BUTTON_RIGHT;
    }

    public static void onDrawForeground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (Slot slot : screen.getMenu().slots) {
            if (CONTAINER_DRAG_SLOTS.contains(slot)) {
                // slots will sometimes be added to dragged slots when simply clicking on a slot, so don't render our overlay then
                if (CONTAINER_DRAG_SLOTS.size() > 1 || !isHovering(screen, slot, mouseX, mouseY)) {
                    guiGraphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, -2130706433);
                }
            }
        }
    }

    private static boolean isHovering(AbstractContainerScreen<?> screen, Slot slot, double mouseX, double mouseY) {
        return isHovering(slot.x, slot.y, 16, 16, ScreenHelper.INSTANCE.getLeftPos(screen), ScreenHelper.INSTANCE.getTopPos(screen), mouseX, mouseY);
    }

    private static boolean isHovering(int x, int y, int width, int height, int leftPos, int topPos, double mouseX, double mouseY) {
        mouseX -= leftPos;
        mouseY -= topPos;
        return mouseX >= (double)(x - 1) && mouseX < (double)(x + width + 1) && mouseY >= (double)(y - 1) && mouseY < (double)(y + height + 1);
    }

    public static EventResult onPlaySoundAtPosition(Level level, Entity entity, MutableValue<Holder<SoundEvent>> sound, MutableValue<SoundSource> source, DefaultedFloat volume, DefaultedFloat pitch) {
        // prevent the bundle sounds from being spammed when dragging, not a nice solution, but it works
        if (containerDragType != null && source.get() == SoundSource.PLAYERS) {
            if (sound.get().value() == containerDragType.sound) {
                return EventResult.INTERRUPT;
            }
        }
        return EventResult.PASS;
    }

    private enum ContainerDragType {
        INSERT(SoundEvents.BUNDLE_INSERT), REMOVE(SoundEvents.BUNDLE_REMOVE_ONE);

        public final SoundEvent sound;

        ContainerDragType(SoundEvent sound) {
            this.sound = sound;
        }
    }
}
