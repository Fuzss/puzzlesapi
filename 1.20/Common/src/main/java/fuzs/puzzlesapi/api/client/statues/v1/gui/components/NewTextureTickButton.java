package fuzs.puzzlesapi.api.client.statues.v1.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand.AbstractArmorStandScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class NewTextureTickButton extends NewTextureButton implements Tickable {
    private final int imageTextureX;
    private final int imageTextureY;
    private final ResourceLocation imageTextureLocation;
    private int lastClickedTicks;
    protected int lastClickedTicksDelay = 20;

    public NewTextureTickButton(int x, int y, int width, int height, int imageTextureX, int imageTextureY, ResourceLocation imageTextureLocation, OnPress onPress) {
        super(x, y, width, height, 0, 184, AbstractArmorStandScreen.getArmorStandWidgetsLocation(), CommonComponents.EMPTY, onPress);
        this.imageTextureX = imageTextureX;
        this.imageTextureY = imageTextureY;
        this.imageTextureLocation = imageTextureLocation;
    }

    @Override
    protected int getYImage() {
        if (!this.active) {
            return 0;
        } else if (this.isHoveredOrFocused()) {
            return 2;
        }
        return 1;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.lastClickedTicks = this.lastClickedTicksDelay;
    }

    @Override
    public void tick() {
        if (this.lastClickedTicks > 0) this.lastClickedTicks--;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        ResourceLocation resourceLocation = this.lastClickedTicks > 0 ? this.textureLocation : this.imageTextureLocation;
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        final int i = this.getYImage();
        if (this.lastClickedTicks > 0) {
            guiGraphics.blit(resourceLocation, this.getX() + this.width / 2 - 8, this.getY() + this.height / 2 - 8, 176, 124 + i * 16, 16, 16);
        } else {
            guiGraphics.blit(resourceLocation, this.getX() + this.width / 2 - 8, this.getY() + this.height / 2 - 8, this.imageTextureX, this.imageTextureY + i * 16, 16, 16);
        }
    }
}
