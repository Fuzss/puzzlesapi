package fuzs.puzzlesapi.api.client.statues.v1.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand.AbstractArmorStandScreen;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandPose;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.DoubleSupplier;

public abstract class BoxedSliderButton extends AbstractWidget implements UnboundedSliderButton, LiveSliderButton {
    static final double VALUE_KEY_INTERVAL = 0.035;
    private static final int SLIDER_SIZE = 13;
    
    private final DoubleSupplier currentHorizontalValue;
    private final DoubleSupplier currentVerticalValue;
    protected double horizontalValue;
    protected double verticalValue;
    private boolean canChangeValue;

    public BoxedSliderButton(int x, int y, DoubleSupplier currentHorizontalValue, DoubleSupplier currentVerticalValue) {
        super(x, y, 54, 54, CommonComponents.EMPTY);
        this.currentHorizontalValue = currentHorizontalValue;
        this.currentVerticalValue = currentVerticalValue;
        this.refreshValues();
    }

    @Override
    public void refreshValues() {
        this.horizontalValue = this.currentHorizontalValue.getAsDouble();
        this.verticalValue = this.currentVerticalValue.getAsDouble();
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        if (this.active) {
            if (this.isFocused()) {
                narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.focused"));
            } else {
                narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.hovered"));
            }
        }
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
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        final int sliderX = (int) (this.horizontalValue * (double) (this.width - SLIDER_SIZE - 2));
        final int sliderY = (int) (this.verticalValue * (double) (this.height - SLIDER_SIZE - 2));
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        if (!this.active || !this.isHoveredOrFocused() || !this.horizontalValueLocked() && !this.verticalValueLocked()) {
            guiGraphics.blit(AbstractArmorStandScreen.getArmorStandWidgetsLocation(), this.getX(), this.getY(), 0, 120, this.width, this.height);
        } else if (this.horizontalValueLocked() && this.verticalValueLocked()) {
            guiGraphics.blit(AbstractArmorStandScreen.getArmorStandWidgetsLocation(), this.getX() + sliderX, this.getY() + sliderY, 164, 0, SLIDER_SIZE + 2, SLIDER_SIZE + 2);
        } else if (this.horizontalValueLocked()) {
            guiGraphics.blit(AbstractArmorStandScreen.getArmorStandWidgetsLocation(), this.getX() + sliderX, this.getY(), 54, 120, SLIDER_SIZE + 2, this.height);
        } else {
            guiGraphics.blit(AbstractArmorStandScreen.getArmorStandWidgetsLocation(), this.getX(), this.getY() + sliderY, 136, 49, this.width, SLIDER_SIZE + 2);
        }
        guiGraphics.blit(AbstractArmorStandScreen.getArmorStandWidgetsLocation(), this.getX() + 1 + sliderX, this.getY() + 1 + sliderY, 151, this.getYImage() * SLIDER_SIZE, SLIDER_SIZE, SLIDER_SIZE);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX, mouseY);
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
        if (this.active && this.visible) {
            switch (keyCode) {
                case InputConstants.KEY_LEFT -> {
                    this.setHorizontalValue(this.horizontalValue - VALUE_KEY_INTERVAL, false);
                    return true;
                }
                case InputConstants.KEY_RIGHT -> {
                    this.setHorizontalValue(this.horizontalValue + VALUE_KEY_INTERVAL, false);
                    return true;
                }
                case InputConstants.KEY_UP -> {
                    this.setVerticalValue(this.verticalValue - VALUE_KEY_INTERVAL, false);
                    return true;
                }
                case InputConstants.KEY_DOWN -> {
                    this.setVerticalValue(this.verticalValue + VALUE_KEY_INTERVAL, false);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setValueFromMouse(mouseX, mouseY);
    }

    private void setValueFromMouse(double mouseX, double mouseY) {
        this.setHorizontalValue((mouseX - (double) (this.getX() + 8)) / (double) (this.width - SLIDER_SIZE - 2), true);
        this.setVerticalValue((mouseY - (double) (this.getY() + 8)) / (double) (this.height - SLIDER_SIZE - 2), true);
    }

    private void setHorizontalValue(double horizontalValue, boolean snapValue) {
        double oldHorizontalValue = this.horizontalValue;
        if (!this.horizontalValueLocked()) {
            this.horizontalValue = Mth.clamp(horizontalValue, 0.0, 1.0);
            if (snapValue) {
                this.horizontalValue = ArmorStandPose.snapValue(this.horizontalValue, ArmorStandPose.DEGREES_SNAP_INTERVAL);
            }
        }
        if (oldHorizontalValue != this.horizontalValue) {
            this.applyValue();
        }
    }

    private void setVerticalValue(double verticalValue, boolean snapValue) {
        double oldVerticalValue = this.verticalValue;
        if (!this.verticalValueLocked()) {
            this.verticalValue = Mth.clamp(verticalValue, 0.0, 1.0);
            if (snapValue) {
                this.verticalValue = ArmorStandPose.snapValue(this.verticalValue, ArmorStandPose.DEGREES_SNAP_INTERVAL);
            }
        }
        if (oldVerticalValue != this.verticalValue) {
            this.applyValue();
        }
    }

    protected boolean verticalValueLocked() {
        return Screen.hasShiftDown();
    }

    protected boolean horizontalValueLocked() {
        return Screen.hasAltDown();
    }

    @Override
    public void playDownSound(SoundManager handler) {

    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    protected abstract void applyValue();
}
