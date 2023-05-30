package fuzs.iteminteractionscore.impl.client;

import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class ItemInteractionsCoreFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(ItemInteractionsCore.MOD_ID, ItemInteractionsCoreClient::new);
    }
}
