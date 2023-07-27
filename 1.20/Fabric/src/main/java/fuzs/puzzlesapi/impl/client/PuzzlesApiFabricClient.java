package fuzs.puzzlesapi.impl.client;

import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.iteminteractions.client.ItemInteractionsClient;
import fuzs.puzzlesapi.impl.slotcycling.client.SlotCyclingClient;
import fuzs.puzzlesapi.impl.statues.client.StatuesClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class PuzzlesApiFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(PuzzlesApi.MOD_ID, ItemInteractionsClient::new);
        ClientModConstructor.construct(PuzzlesApi.MOD_ID, SlotCyclingClient::new);
        ClientModConstructor.construct(PuzzlesApi.MOD_ID, StatuesClient::new);
    }
}
