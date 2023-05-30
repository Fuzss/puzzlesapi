package fuzs.iteminteractionscore.api.client.container.v1.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.client.core.HeldActivationType;
import fuzs.iteminteractionscore.impl.config.ClientConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

public abstract class ExpandableClientTooltipComponent implements ClientTooltipComponent {
    public static final String REVEAL_CONTENTS_TRANSLATION_KEY = "item.container.tooltip.revealContents";

    @Override
    public final int getHeight() {
        if (!ItemInteractionsCore.CONFIG.get(ClientConfig.class).revealContents.isActive()) {
            return 10;
        }
        return this.getExpandedHeight();
    }

    public abstract int getExpandedHeight();

    @Override
    public final int getWidth(Font font) {
        HeldActivationType activation = ItemInteractionsCore.CONFIG.get(ClientConfig.class).revealContents;
        if (!activation.isActive()) {
            Component component = activation.getComponent(REVEAL_CONTENTS_TRANSLATION_KEY);
            return font.width(component);
        }
        return this.getExpandedWidth(font);
    }

    public abstract int getExpandedWidth(Font font);

    @Override
    public final void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        HeldActivationType activation = ItemInteractionsCore.CONFIG.get(ClientConfig.class).revealContents;
        if (!activation.isActive()) {
            Component component = activation.getComponent(REVEAL_CONTENTS_TRANSLATION_KEY);
            font.drawInBatch(component, (float) mouseX, (float) mouseY, -1, true, matrix4f, bufferSource, false, 0, 15728880);
        }
    }

    @Override
    public final void renderImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        if (!ItemInteractionsCore.CONFIG.get(ClientConfig.class).revealContents.isActive()) return;
        this.renderExpandedImage(font, mouseX, mouseY, poseStack, itemRenderer, blitOffset);
    }

    public abstract void renderExpandedImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset);
}
