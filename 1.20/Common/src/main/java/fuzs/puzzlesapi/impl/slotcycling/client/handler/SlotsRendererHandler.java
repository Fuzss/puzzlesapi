package fuzs.puzzlesapi.impl.slotcycling.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.puzzlesapi.api.client.slotcycling.v1.SlotCyclingProvider;
import fuzs.puzzlesapi.impl.slotcycling.SlotCycling;
import fuzs.puzzlesapi.impl.slotcycling.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class SlotsRendererHandler {
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static void onRenderGui(Minecraft minecraft, GuiGraphics guiGraphics, float partialTicks, int screenWidth, int screenHeight) {

        if (!minecraft.options.hideGui && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {

            if (minecraft.getCameraEntity() instanceof Player player) {

                SlotCyclingProvider provider = SlotCyclingProvider.getProvider(player);
                if (provider != null) {

                    ItemStack forwardStack = provider.getForwardStack();
                    ItemStack backwardStack = provider.getBackwardStack();
                    if (!forwardStack.isEmpty() && !backwardStack.isEmpty()) {

                        ItemStack selectedStack = provider.getSelectedStack();
                        renderAdditionalSlots(guiGraphics, partialTicks, screenWidth, screenHeight, minecraft.font, (Player) minecraft.getCameraEntity(), backwardStack, selectedStack, forwardStack);
                    }
                }
            }
        }
    }

    private static void renderAdditionalSlots(GuiGraphics guiGraphics, float partialTicks, int screenWidth, int screenHeight, Font font, Player player, ItemStack backwardStack, ItemStack selectedStack, ItemStack forwardStack) {

        if (forwardStack.isEmpty() || backwardStack.isEmpty()) return;

        boolean renderToRight = player.getMainArm().getOpposite() == HumanoidArm.LEFT;

        if (ItemStack.matches(forwardStack, backwardStack)) {
            if (renderToRight) {
                forwardStack = ItemStack.EMPTY;
            } else {
                backwardStack = ItemStack.EMPTY;
            }
        }

        int posX = screenWidth / 2 + (91 + SlotCycling.CONFIG.get(ClientConfig.class).slotsXOffset) * (renderToRight ? 1 : -1);
        int posY = screenHeight - SlotCycling.CONFIG.get(ClientConfig.class).slotsYOffset;
        if (SlotCycling.CONFIG.get(ClientConfig.class).slotsDisplayState == ClientConfig.SlotsDisplayState.KEY) {
            posY += (screenHeight - posY + 23) * (1.0F - Math.min(1.0F, (CyclingInputHandler.getSlotsDisplayTicks() - partialTicks) / 5.0F));
        }

        renderSlotBackgrounds(guiGraphics, posX, posY, !forwardStack.isEmpty(), !backwardStack.isEmpty(), renderToRight);
        renderSlotItems(partialTicks, posX, posY - (16 + 3), font, guiGraphics, player, selectedStack, forwardStack, backwardStack, renderToRight);
    }

    private static void renderSlotBackgrounds(GuiGraphics guiGraphics, int posX, int posY, boolean renderForwardStack, boolean renderBackwardStack, boolean renderToRight) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (renderToRight) {

            guiGraphics.blit(WIDGETS_LOCATION, posX, posY - 23, 53, 22, 29, 24);
            if (renderForwardStack) {
                guiGraphics.blit(WIDGETS_LOCATION, posX + 40, posY - 23, 53, 22, 29, 24);
            }
            guiGraphics.blit(WIDGETS_LOCATION, posX + 28, posY - 22, 21, 0, 20, 22);
            guiGraphics.blit(WIDGETS_LOCATION, posX + 26, posY - 22 - 1, 0, 22, 24, 24);
        } else {

            if (renderBackwardStack) {
                guiGraphics.blit(WIDGETS_LOCATION, posX - 29 - 40, posY - 23, 24, 22, 29, 24);
            }
            guiGraphics.blit(WIDGETS_LOCATION, posX - 29, posY - 23, 24, 22, 29, 24);
            guiGraphics.blit(WIDGETS_LOCATION, posX - 29 - 19, posY - 22, 21, 0, 20, 22);
            guiGraphics.blit(WIDGETS_LOCATION, posX - 29 - 21, posY - 22 - 1, 0, 22, 24, 24);
        }
    }

    private static void renderSlotItems(float partialTicks, int posX, int posY, Font font, GuiGraphics guiGraphics, Player player, ItemStack selectedStack, ItemStack forwardStack, ItemStack backwardStack, boolean renderToRight) {

        if (renderToRight) {

            renderItemInSlot(font, guiGraphics, posX + 10, posY, partialTicks, player, backwardStack);
            renderItemInSlot(font, guiGraphics, posX + 10 + 20, posY, partialTicks, player, selectedStack);
            renderItemInSlot(font, guiGraphics, posX + 10 + 20 + 20, posY, partialTicks, player, forwardStack);
        } else {

            renderItemInSlot(font, guiGraphics, posX - 26, posY, partialTicks, player, forwardStack);
            renderItemInSlot(font, guiGraphics, posX - 26 - 20, posY, partialTicks, player, selectedStack);
            renderItemInSlot(font, guiGraphics, posX - 26 - 20 - 20, posY, partialTicks, player, backwardStack);
        }
    }

    private static void renderItemInSlot(Font font, GuiGraphics guiGraphics, int posX, int posY, float tickDelta, Player player, ItemStack stack) {

        if (!stack.isEmpty()) {

            float popTime = CyclingInputHandler.getGlobalPopTime() - tickDelta;
            if (popTime > 0.0F) {

                float f1 = 1.0F + popTime / 5.0F;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(posX + 8, posY + 12, 0.0D);
                guiGraphics.pose().scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                guiGraphics.pose().translate(-(posX + 8), -(posY + 12), 0.0D);
                RenderSystem.applyModelViewMatrix();
            }

            guiGraphics.renderItem(player, stack, posX, posY, 0);
            if (popTime > 0.0F) {

                guiGraphics.pose().popPose();
            }

            guiGraphics.renderItemDecorations(font, stack, posX, posY);
        }
    }
}
