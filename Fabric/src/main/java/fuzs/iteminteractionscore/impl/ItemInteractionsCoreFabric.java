package fuzs.iteminteractionscore.impl;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class ItemInteractionsCoreFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(ItemInteractionsCore.MOD_ID, ItemInteractionsCore::new);
    }
}
