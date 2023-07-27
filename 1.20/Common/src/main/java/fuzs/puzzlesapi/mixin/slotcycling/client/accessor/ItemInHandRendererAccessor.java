package fuzs.puzzlesapi.mixin.slotcycling.client.accessor;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemInHandRenderer.class)
public interface ItemInHandRendererAccessor {

    @Accessor("mainHandItem")
    void puzzlesapi$setMainHandItem(ItemStack mainHandItem);

    @Accessor("offHandItem")
    void puzzlesapi$setOffHandItem(ItemStack offHandItem);
}
