package fuzs.puzzlesapi.api.statues.v1.world.inventory;

import fuzs.puzzlesapi.api.statues.v1.world.entity.decoration.ArmorStandDataProvider;
import net.minecraft.world.entity.decoration.ArmorStand;

public interface ArmorStandHolder {

    ArmorStand getArmorStand();

    default ArmorStandDataProvider getDataProvider() {
        return this.getArmorStand() instanceof ArmorStandDataProvider dataProvider ? dataProvider : ArmorStandDataProvider.INSTANCE;
    }
}
