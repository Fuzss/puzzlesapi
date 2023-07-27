package fuzs.puzzlesapi.api.client.statues.v1.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class NewTextureButton extends Button {
    private final int textureX;
    private final int textureY;
    protected final ResourceLocation textureLocation;

    public NewTextureButton(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation, Component component, OnPress onPress) {
        super(x, y, width, height, component, onPress, Button.DEFAULT_NARRATION);
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureLocation = textureLocation;
    }

    protected int getYImage() {
        return !this.active || this.isHoveredOrFocused() ? 2 : 1;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        int i = this.getYImage();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        guiGraphics.blit(this.textureLocation, this.getX(), this.getY(), this.textureX, this.textureY + i * 20, this.width / 2, this.height);
        guiGraphics.blit(this.textureLocation, this.getX() + this.width / 2, this.getY(), this.textureX + 200 - this.width / 2, this.textureY + i * 20, this.width / 2, this.height);
        this.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        final int j = this.active && this.isHoveredOrFocused() ? ChatFormatting.YELLOW.getColor() : 4210752;
        drawCenteredString(guiGraphics, minecraft.font, this.getMessage(), this.getX() + this.width / 2 + this.getMessageXOffset(), this.getY() + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24, false);
    }

    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {

    }

    protected int getMessageXOffset() {
        return 0;
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean dropShadow) {
        FormattedCharSequence formattedCharSequence = text.getVisualOrderText();
        guiGraphics.drawString(font, formattedCharSequence, x - font.width(formattedCharSequence) / 2, y, color, dropShadow);
    }
}
