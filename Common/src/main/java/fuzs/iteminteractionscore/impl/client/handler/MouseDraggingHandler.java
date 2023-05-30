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
    public static final MouseDraggingHandler INSTANCE = new MouseDraggingHandler();

    public final Set<Slot> containerDragSlots = Sets.newHashSet();
    @Nullable
    private ContainerDragType containerDragType;

    public EventResult onBeforeMousePressed(Screen screen, double mouseX, double mouseY, int button) {
        if (!shouldHandleMouseDragging(screen)) return EventResult.PASS;
        ItemStack carriedStack = ((AbstractContainerScreen<?>) screen).getMenu().getCarried();
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(carriedStack);
        Minecraft minecraft = ScreenHelper.INSTANCE.getMinecraft(screen);
        if (validMouseButton(button) && provider != null && provider.allowsPlayerInteractions(carriedStack, minecraft.player)) {
            Slot slot = ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$callFindSlot(mouseX, mouseY);
            if (slot != null) {
                if (slot.hasItem() && !ClientInputActionHandler.precisionModeAllowedAndActive()) {
                    this.containerDragType = ContainerDragType.INSERT;
                } else {
                    this.containerDragType = ContainerDragType.REMOVE;
                }
                this.containerDragSlots.clear();
                return EventResult.INTERRUPT;
            }
        }
        this.containerDragType = null;
        return EventResult.PASS;
    }

    public EventResult onBeforeMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!shouldHandleMouseDragging(screen)) return EventResult.PASS;
        if (this.containerDragType != null) {
            if (!validMouseButton(button)) {
                this.containerDragType = null;
                this.containerDragSlots.clear();
                return EventResult.PASS;
            }
            Slot slot = ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$callFindSlot(mouseX, mouseY);
            AbstractContainerMenu menu = ((AbstractContainerScreen<?>) screen).getMenu();
            if (slot != null && menu.canDragTo(slot) && !this.containerDragSlots.contains(slot)) {
                ItemStack carriedStack = menu.getCarried();
                ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(carriedStack);
                Objects.requireNonNull(provider, "provider is null");
                Minecraft minecraft = ScreenHelper.INSTANCE.getMinecraft(screen);
                boolean interact = false;
                if (this.containerDragType == ContainerDragType.INSERT && slot.hasItem() && provider.canAddItem(carriedStack, slot.getItem(), minecraft.player)) {
                    interact = true;
                } else if (this.containerDragType == ContainerDragType.REMOVE) {
                    boolean normalInteraction = button == InputConstants.MOUSE_BUTTON_RIGHT && !slot.hasItem() && !provider.getItemContainer(carriedStack, minecraft.player, false).isEmpty();
                    if (normalInteraction || slot.hasItem() && ClientInputActionHandler.precisionModeAllowedAndActive()) {
                        interact = true;
                    }
                }
                if (interact) {
                    ((AbstractContainerScreenAccessor) screen).easyshulkerboxes$callSlotClicked(slot, slot.index, button, ClickType.PICKUP);
                    this.containerDragSlots.add(slot);
                    return EventResult.INTERRUPT;
                }
            }
        }
        return EventResult.PASS;
    }

    public EventResult onBeforeMouseRelease(Screen screen, double mouseX, double mouseY, int button) {
        if (!shouldHandleMouseDragging(screen)) return EventResult.PASS;
        if (this.containerDragType != null) {
            if (validMouseButton(button) && !this.containerDragSlots.isEmpty()) {
                if (!ItemInteractionsCore.CONFIG.get(ClientConfig.class).disableInteractionSounds) {
                    // play this manually at the end, we suppress all interaction sounds played while dragging
                    SimpleSoundInstance sound = SimpleSoundInstance.forUI(this.containerDragType.sound, 0.8F, 0.8F + SoundInstance.createUnseededRandom().nextFloat() * 0.4F);
                    ScreenHelper.INSTANCE.getMinecraft(screen).getSoundManager().play(sound);
                }
                this.containerDragType = null;
                this.containerDragSlots.clear();
                return EventResult.INTERRUPT;
            }
            this.containerDragType = null;
        }
        this.containerDragSlots.clear();
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

    public EventResult onPlaySoundAtPosition(Level level, Entity entity, MutableValue<Holder<SoundEvent>> sound, MutableValue<SoundSource> source, DefaultedFloat volume, DefaultedFloat pitch) {
        // prevent the bundle sounds from being spammed when dragging, not a nice solution, but it works
        if (this.containerDragType != null && source.get() == SoundSource.PLAYERS) {
            if (sound.get().value() == this.containerDragType.sound) {
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
