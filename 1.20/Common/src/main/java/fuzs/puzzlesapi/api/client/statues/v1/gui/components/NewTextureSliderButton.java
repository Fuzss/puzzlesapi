package fuzs.puzzlesapi.api.client.statues.v1.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandPose;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class NewTextureSliderButton extends AbstractSliderButton implements UnboundedSliderButton {
    private final int textureX;
    private final int textureY;
    protected final ResourceLocation textureLocation;
    private boolean canChangeValue;
    public double snapInterval = -1.0;

    public NewTextureSliderButton(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation, Component component, double value) {
        super(x, y, width, height, component, value);
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureLocation = textureLocation;
    }

    protected int getYImage() {
        if (!this.active) {
            return 0;
        } else if (this.isHovered || this.canChangeValue) {
            return 2;
        }
        return 1;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics.blitNineSliced(this.textureLocation, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, this.textureX, this.textureY);
        guiGraphics.blitNineSliced(this.textureLocation, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 8, 20, 20, 4, 200, 20, this.textureX, this.textureY + (this.getYImage() == 2 ? 40 : 20));
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
        this.renderScrollingString(guiGraphics, minecraft.font, 2, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.canChangeValue = false;
        } else {
            InputType inputType = Minecraft.getInstance().getLastInputType();
            if (inputType == InputType.MOUSE || inputType == InputType.KEYBOARD_TAB) {
                this.canChangeValue = true;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (CommonInputs.selected(keyCode)) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                return this.keyPressed(keyCode);
            }
            return false;
        }
    }

    private boolean keyPressed(int keyCode) {
        boolean bl = keyCode == InputConstants.KEY_LEFT;
        if (bl || keyCode == InputConstants.KEY_RIGHT) {
            float f = bl ? -1.0F : 1.0F;
            this.setValue(this.value + (double) (f / (float) (this.width - 8)), true);
        }

        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setValue((mouseX - (double) (this.getX() + 4)) / (double) (this.width - 8), true);
    }

    private void setValue(double value, boolean snapValue) {
        double oldValue = this.value;
        this.value = Mth.clamp(value, 0.0, 1.0);
        if (snapValue) {
            this.value = ArmorStandPose.snapValue(this.value, this.snapInterval);
        }
        if (oldValue != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setValueFromMouse(mouseX);
    }
}
