package fuzs.puzzlesapi.impl.client;

import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.iteminteractions.client.ItemInteractionsClient;
import fuzs.puzzlesapi.impl.slotcycling.client.SlotCyclingClient;
import fuzs.puzzlesapi.impl.statues.client.StatuesClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = PuzzlesApi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PuzzlesApiForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(PuzzlesApi.MOD_ID, ItemInteractionsClient::new);
        ClientModConstructor.construct(PuzzlesApi.MOD_ID, SlotCyclingClient::new);
        ClientModConstructor.construct(PuzzlesApi.MOD_ID, StatuesClient::new);
    }
}
