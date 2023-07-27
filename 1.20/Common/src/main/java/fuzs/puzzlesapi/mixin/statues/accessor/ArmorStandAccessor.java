package fuzs.puzzlesapi.mixin.statues.accessor;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStand.class)
public interface ArmorStandAccessor {

    @Accessor("handItems")
    NonNullList<ItemStack> puzzlesapi$getHandItems();

    @Accessor("armorItems")
    NonNullList<ItemStack> puzzlesapi$getArmorItems();

    @Accessor("disabledSlots")
    int puzzlesapi$getDisabledSlots();

    @Accessor("disabledSlots")
    void puzzlesapi$setDisabledSlots(int disabledSlots);

    @Invoker("readPose")
    void puzzlesapi$callReadPose(CompoundTag compound);
}
