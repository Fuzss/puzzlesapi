package fuzs.puzzlesapi.mixin.slotcycling.client.accessor;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MouseHandler.class)
public interface MouseHandlerAccessor {

    @Accessor("accumulatedScroll")
    double slotcycling$getAccumulatedScroll();
}
