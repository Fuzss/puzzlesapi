package fuzs.iteminteractionscore.mixin.client;

import fuzs.iteminteractionscore.impl.client.helper.ItemDecorationHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
abstract class GuiGraphicsMixin {

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderItemDecorations(Font font, ItemStack stack, int xPosition, int yPosition, @Nullable String text, CallbackInfo callback) {
        if (!stack.isEmpty()) ItemDecorationHelper.render(GuiGraphics.class.cast(this), font, stack, xPosition, yPosition);
    }
}
