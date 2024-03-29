package fuzs.puzzlesapi.impl.iteminteractions.client.helper;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import fuzs.puzzlesapi.api.iteminteractions.v1.provider.ItemContainerProvider;
import fuzs.puzzlesapi.impl.iteminteractions.ItemInteractions;
import fuzs.puzzlesapi.impl.iteminteractions.config.ClientConfig;
import fuzs.puzzlesapi.impl.iteminteractions.world.item.container.ItemContainerProviders;
import fuzs.puzzlesapi.mixin.iteminteractions.client.accessor.AbstractContainerMenuAccessor;
import fuzs.puzzleslib.api.client.init.v1.DynamicItemDecorator;
import fuzs.puzzleslib.api.client.screen.v2.ScreenHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BooleanSupplier;

public class ItemDecorationHelper {
    private static final Map<ItemContainerProvider, DynamicItemDecorator> DECORATORS_CACHE = Maps.newIdentityHashMap();

    @Nullable
    private static Slot activeSlot;

    private static DynamicItemDecorator getDynamicItemDecorator(ItemDecoratorProvider filter, BooleanSupplier allow) {
        return (GuiGraphics guiGraphics, Font font, ItemStack stack, int itemPosX, int itemPosY) -> {
            if (!allow.getAsBoolean()) return false;
            return tryRenderItemDecorations(guiGraphics, font, stack, itemPosX, itemPosY, filter);
        };
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean tryRenderItemDecorations(GuiGraphics guiGraphics, Font font, ItemStack stack, int itemPosX, int itemPosY, ItemDecoratorProvider filter) {
        Minecraft minecraft = Minecraft.getInstance();
        // prevent rendering on items used as icons for creative mode tabs and for backpacks in locked slots (like Inmis)
        if (!(minecraft.screen instanceof AbstractContainerScreen<?> screen)) return false;
        AbstractContainerMenu menu = screen.getMenu();
        if (activeSlot != null && activeSlot.getItem() == stack && activeSlot.allowModification(minecraft.player) && !isCreativeInventorySlot(menu, activeSlot)) {
            ItemStack carriedStack = menu.getCarried();
            if (stack != carriedStack) {
                ItemDecoratorType type = filter.get(screen, stack, carriedStack);
                if (type != ItemDecoratorType.NONE) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0.0, 0.0, 200.0);
                    MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    font.drawInBatch(type.getText(), (float) (itemPosX + 19 - 2 - type.getWidth(font)), (float) (itemPosY + 6 + 3), type.getColor(), true, guiGraphics.pose().last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880);
                    multibuffersource$buffersource.endBatch();
                    guiGraphics.pose().popPose();
                    // font renderer modifies render states, so this tells the implementation to reset them
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("ConstantValue")
    private static boolean isCreativeInventorySlot(AbstractContainerMenu menu, Slot slot) {
        // stupid hack to filter out creative mode inventory slots, as the slot class is client-only and package private (if not package private proxy would've worked)
        // also cannot use vanilla getter here as it throws an exception when menu type is null which is exactly what we want
        // accessor is definitely more performant than catching the exception, especially as this is called in rendering code
        if (((AbstractContainerMenuAccessor) menu).puzzlesapi$getMenuType() == null) {
            // Scout mod uses slots with null container apparently
            if (slot.getClass() != Slot.class && slot.container != null) {
                return slot.container.getContainerSize() == 45;
            }
        }
        return false;
    }

    public static void render(GuiGraphics guiGraphics, Font font, ItemStack stack, int itemPosX, int itemPosY) {
        ItemContainerProvider provider = ItemContainerProviders.INSTANCE.get(stack);
        if (provider != null) {
            resetRenderState();
            DynamicItemDecorator itemDecorator = DECORATORS_CACHE.computeIfAbsent(provider, $ -> ItemDecorationHelper.getDynamicItemDecorator((AbstractContainerScreen<?> screen, ItemStack containerStack, ItemStack carriedStack) -> {
                Minecraft minecraft = ScreenHelper.INSTANCE.getMinecraft(screen);
                return ItemDecoratorType.getItemDecoratorType(provider, containerStack, carriedStack, minecraft.player);
            }, () -> ItemInteractions.CONFIG.get(ClientConfig.class).containerItemIndicator));
            if (itemDecorator.renderItemDecorations(guiGraphics, font, stack, itemPosX, itemPosY)) {
                resetRenderState();
            }
        }
    }

    private static void resetRenderState() {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void clearCache() {
        DECORATORS_CACHE.clear();
    }

    public static void setActiveSlot(@Nullable Slot activeSlot) {
        ItemDecorationHelper.activeSlot = activeSlot;
    }

    @FunctionalInterface
    public interface ItemDecoratorProvider {

        ItemDecoratorType get(AbstractContainerScreen<?> screen, ItemStack stack, ItemStack carriedStack);
    }

    private enum ItemDecoratorType {
        NONE("", -1),
        SPACE("+", ChatFormatting.YELLOW),
        PRESENT_AND_SPACE("+", ChatFormatting.GREEN),
        PRESENT_NO_SPACE("+", ChatFormatting.RED);

        private final String text;
        private final int color;

        ItemDecoratorType(String text, ChatFormatting color) {
            this(text, color.getColor());
        }

        ItemDecoratorType(String text, int color) {
            this.text = text;
            this.color = color;
        }

        public String getText() {
            return this.text;
        }

        public int getWidth(Font font) {
            return font.width(this.text);
        }

        public int getColor() {
            return this.color;
        }

        public static ItemDecoratorType getItemDecoratorType(ItemContainerProvider provider, ItemStack containerStack, ItemStack carriedStack, Player player) {
            if (provider.canAddItem(containerStack, carriedStack, player)) {
                if (provider.hasAnyOf(containerStack, carriedStack, player)) {
                    return ItemDecoratorType.PRESENT_AND_SPACE;
                }
                return ItemDecoratorType.SPACE;
            } else if (provider.hasAnyOf(containerStack, carriedStack, player)) {
                return ItemDecoratorType.PRESENT_NO_SPACE;
            }
            return ItemDecoratorType.NONE;
        }
    }
}
