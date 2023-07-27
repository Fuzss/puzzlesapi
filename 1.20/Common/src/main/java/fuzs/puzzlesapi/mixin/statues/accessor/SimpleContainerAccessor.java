package fuzs.puzzlesapi.mixin.statues.accessor;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleContainer.class)
public interface SimpleContainerAccessor {

    @Accessor("items")
    @Mutable
    void puzzlesapi$setItems(NonNullList<ItemStack> items);
}
