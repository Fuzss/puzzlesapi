package fuzs.iteminteractionscore.api.client.container.v1.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.client.handler.ClientInputActionHandler;
import fuzs.iteminteractionscore.impl.config.ClientConfig;
import fuzs.iteminteractionscore.impl.world.inventory.ContainerSlotHelper;
import fuzs.puzzleslib.api.client.screen.v2.TooltipRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;
import java.util.Optional;

public abstract class AbstractClientContainerItemTooltip extends ExpandableClientTooltipComponent {
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation TEXTURE_LOCATION = ItemInteractionsCore.id("textures/gui/container/inventory_tooltip.png");
    private static final int BORDER_SIZE = 7;
    private static final MutableInt ACTIVE_CONTAINER_ITEM_TOOLTIPS = new MutableInt();

    protected final NonNullList<ItemStack> items;
    private final float[] backgroundColor;

    public AbstractClientContainerItemTooltip(NonNullList<ItemStack> items, float[] backgroundColor) {
        this.items = items;
        this.backgroundColor = backgroundColor;
    }

    protected abstract int getGridSizeX();

    protected abstract int getGridSizeY();

    protected boolean isSlotBlocked(int itemIndex) {
        return false;
    }

    @Override
    public int getExpandedHeight() {
        return this.getGridSizeY() * 18 + 2 * BORDER_SIZE;
    }

    @Override
    public int getExpandedWidth(Font font) {
        return this.getGridSizeX() * 18 + 2 * BORDER_SIZE;
    }

    @Override
    public void renderExpandedImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        ACTIVE_CONTAINER_ITEM_TOOLTIPS.increment();
        float[] color = this.getBackgroundColor();
        if (this.defaultSize()) {
            ContainerTexture.FULL.blit(guiGraphics, mouseX, mouseY, color);
        } else {
            this.drawBorder(mouseX, mouseY, this.getGridSizeX(), this.getGridSizeY(), guiGraphics);
        }
        int itemIndex = 0;
        int lastFilledSlot = this.getLastFilledSlot();
        for (int l = 0; l < this.getGridSizeY(); ++l) {
            for (int i1 = 0; i1 < this.getGridSizeX(); ++i1) {
                int posX = mouseX + i1 * 18 + BORDER_SIZE;
                int posY = mouseY + l * 18 + BORDER_SIZE;
                if (!this.defaultSize()) {
                    if (this.isSlotBlocked(itemIndex)) {
                        ContainerTexture.BLOCKED_SLOT.blit(guiGraphics, posX, posY, color);
                    } else {
                        ContainerTexture.SLOT.blit(guiGraphics, posX, posY, color);
                    }
                }
                this.drawSlot(guiGraphics, posX, posY, itemIndex, font);
                if (itemIndex == lastFilledSlot) this.drawSlotOverlay(guiGraphics, posX, posY);
                itemIndex++;
            }
        }
        this.drawSelectedSlotTooltip(font, mouseX, mouseY, guiGraphics, lastFilledSlot);
        ACTIVE_CONTAINER_ITEM_TOOLTIPS.decrement();
    }

    private void drawSelectedSlotTooltip(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics, int lastFilledSlot) {
        if (!ItemInteractionsCore.CONFIG.get(ClientConfig.class).selectedItemTooltips.isActive()) return;
        if (ACTIVE_CONTAINER_ITEM_TOOLTIPS.intValue() > 1) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null && !willTooltipBeMoved(minecraft, font, mouseX, mouseY) && lastFilledSlot >= 0 && lastFilledSlot < this.items.size()) {
            ItemStack stack = this.items.get(lastFilledSlot);
            List<Component> itemTooltip = Screen.getTooltipFromItem(minecraft, stack);
            Optional<TooltipComponent> itemTooltipImage = stack.getTooltipImage();
            List<ClientTooltipComponent> tooltipComponents = TooltipRenderHelper.getTooltip(stack);
            int maxWidth = tooltipComponents.stream().mapToInt(tooltipComponent -> tooltipComponent.getWidth(font)).max().orElse(0);
            guiGraphics.pose().pushPose();
            guiGraphics.renderTooltip(font, itemTooltip, itemTooltipImage, mouseX - maxWidth - 2 * 18, mouseY);
            guiGraphics.pose().popPose();
        }
    }

    private static boolean willTooltipBeMoved(Minecraft minecraft, Font font, int mouseX, int mouseY) {
        if (!(minecraft.screen instanceof AbstractContainerScreen<?> containerScreen)) return false;
        ItemStack stack = ClientInputActionHandler.getContainerStack(containerScreen, true);
        if (stack.isEmpty()) return false;
        List<ClientTooltipComponent> tooltipComponents = TooltipRenderHelper.getTooltip(stack);
        int maxWidth = tooltipComponents.stream().mapToInt(tooltipComponent -> tooltipComponent.getWidth(font)).max().orElse(0);
        // actual mouseX, tooltip components are passed the adjusted position where the tooltip should be rendered
        mouseX = (int) (minecraft.mouseHandler.xpos() * (double) minecraft.getWindow().getGuiScaledWidth() / (double) minecraft.getWindow().getScreenWidth());
        return mouseX + 12 + maxWidth > containerScreen.width;
    }

    private boolean defaultSize() {
        // this is by far the most common size, we use a pre-built image for that
        return this.getGridSizeX() == 9 && this.getGridSizeY() == 3;
    }

    private float[] getBackgroundColor() {
        if (!ItemInteractionsCore.CONFIG.get(ClientConfig.class).colorfulTooltips) {
            return new float[]{1.0F, 1.0F, 1.0F};
        }
        return this.backgroundColor;
    }

    private int getLastFilledSlot() {
        Minecraft minecraft = Minecraft.getInstance();
        int currentContainerSlot = ContainerSlotHelper.getCurrentContainerSlot(minecraft.player);
        if (currentContainerSlot != -1 && currentContainerSlot < this.items.size()) {
            if (!this.items.get(currentContainerSlot).isEmpty()) {
                return currentContainerSlot;
            }
        }
        for (int i = this.items.size() - 1; i >= 0; i--) {
            if (!this.items.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private void drawBorder(int mouseX, int mouseY, int gridSizeX, int gridSizeY, GuiGraphics guiGraphics) {
        float[] color = this.getBackgroundColor();
        ContainerTexture.BORDER_TOP_LEFT.blit(guiGraphics, mouseX, mouseY, color);
        ContainerTexture.BORDER_TOP_RIGHT.blit(guiGraphics, mouseX + gridSizeX * 18 + BORDER_SIZE, mouseY, color);

        for (int i = 0; i < gridSizeX; ++i) {
            ContainerTexture.BORDER_TOP.blit(guiGraphics, mouseX + BORDER_SIZE + i * 18, mouseY, color);
            ContainerTexture.BORDER_BOTTOM.blit(guiGraphics, mouseX + BORDER_SIZE + i * 18, mouseY + gridSizeY * 18 + BORDER_SIZE, color);
        }

        for (int j = 0; j < gridSizeY; ++j) {
            ContainerTexture.BORDER_LEFT.blit(guiGraphics, mouseX, mouseY + j * 18 + BORDER_SIZE, color);
            ContainerTexture.BORDER_RIGHT.blit(guiGraphics, mouseX + gridSizeX * 18 + BORDER_SIZE, mouseY + j * 18 + BORDER_SIZE, color);
        }

        ContainerTexture.BORDER_BOTTOM_LEFT.blit(guiGraphics, mouseX, mouseY + gridSizeY * 18 + BORDER_SIZE, color);
        ContainerTexture.BORDER_BOTTOM_RIGHT.blit(guiGraphics, mouseX + gridSizeX * 18 + BORDER_SIZE, mouseY + gridSizeY * 18 + BORDER_SIZE, color);
    }

    private void drawSlot(GuiGraphics guiGraphics, int posX, int posY, int itemIndex, Font font) {
        if (itemIndex < this.items.size()) {
            ItemStack itemstack = this.items.get(itemIndex);
            guiGraphics.renderItem(itemstack, posX + 1, posY + 1, itemIndex);
            guiGraphics.renderItemDecorations(font, itemstack, posX + 1, posY + 1);
        }
    }

    private void drawSlotOverlay(GuiGraphics guiGraphics, int posX, int posY) {
        if (ACTIVE_CONTAINER_ITEM_TOOLTIPS.intValue() > 1) return;
        ClientConfig.SlotOverlay slotOverlay = ItemInteractionsCore.CONFIG.get(ClientConfig.class).slotOverlay;
        if (slotOverlay == ClientConfig.SlotOverlay.HOTBAR) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics.blit(WIDGETS_LOCATION, posX - 3, posY - 3, 100, 0, 22, 24, 24, 256, 256);
        } else if (slotOverlay == ClientConfig.SlotOverlay.HOVER) {
            AbstractContainerScreen.renderSlotHighlight(guiGraphics, posX + 1, posY + 1, 0);
        }
    }

    private enum ContainerTexture {
        SLOT(BORDER_SIZE, BORDER_SIZE, 18, 18),
        BLOCKED_SLOT(BORDER_SIZE * 3 + 18, BORDER_SIZE, 18, 18),
        BORDER_TOP_LEFT(0, 0, BORDER_SIZE, BORDER_SIZE),
        BORDER_TOP(BORDER_SIZE, 0, 18, BORDER_SIZE),
        BORDER_TOP_RIGHT(18 + BORDER_SIZE, 0, BORDER_SIZE, BORDER_SIZE),
        BORDER_RIGHT(18 + BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, 18),
        BORDER_BOTTOM_RIGHT(18 + BORDER_SIZE, 18 + BORDER_SIZE, BORDER_SIZE, BORDER_SIZE),
        BORDER_BOTTOM(BORDER_SIZE, 18 + BORDER_SIZE, 18, BORDER_SIZE),
        BORDER_BOTTOM_LEFT(0, 18 + BORDER_SIZE, BORDER_SIZE, BORDER_SIZE),
        BORDER_LEFT(0, BORDER_SIZE, BORDER_SIZE, 18),
        FULL(0, 18 + 2 * BORDER_SIZE, 18 * 9 + 2 * BORDER_SIZE, 18 * 3 + 2 * BORDER_SIZE);

        public final int textureX;
        public final int textureY;
        public final int width;
        public final int height;

        ContainerTexture(int textureX, int textureY, int width, int height) {
            this.textureX = textureX;
            this.textureY = textureY;
            this.width = width;
            this.height = height;
        }

        public void blit(GuiGraphics guiGraphics, int posX, int posY, float[] color) {
            RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0F);
            guiGraphics.blit(TEXTURE_LOCATION, posX, posY, 0, this.textureX, this.textureY, this.width, this.height, 256, 256);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}