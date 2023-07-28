package fuzs.puzzlesapi.api.client.statues.v1.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand.AbstractArmorStandScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.BooleanSupplier;

public class TickBoxButton extends Button {
    private final int textMargin;
    private BooleanSupplier supplier;

    public TickBoxButton(int posX, int posY, int textMargin, int textWidth, Component component, BooleanSupplier supplier, OnPress onPress) {
        super(posX, posY, 20 + textMargin + textWidth, 20, component, onPress, DEFAULT_NARRATION);
        this.textMargin = textMargin;
        this.supplier = supplier;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blit(AbstractArmorStandScreen.getArmorStandWidgetsLocation(), this.getX() + 2, this.getY() + 2, 196, this.isHoveredOrFocused() ? 16 : 0, 16, 16);
        if (this.supplier.getAsBoolean()) {
            guiGraphics.blit(AbstractArmorStandScreen.getArmorStandWidgetsLocation(), this.getX() + 2, this.getY() + 2, 196, 32 + (this.isHoveredOrFocused() ? 16 : 0), 16, 16);
        }
        final int textColor = this.active ? (this.isHoveredOrFocused() ? ChatFormatting.YELLOW.getColor() : 16777215) : 10526880;
        guiGraphics.drawString(minecraft.font, this.getMessage(), this.getX() + 20 + this.textMargin, this.getY() + 2 + 4, textColor | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
