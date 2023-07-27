package fuzs.puzzlesapi.impl;

import fuzs.puzzlesapi.impl.data.ModLanguageProvider;
import fuzs.puzzlesapi.impl.data.ModSpriteSourceProvider;
import fuzs.puzzlesapi.impl.iteminteractions.ItemInteractions;
import fuzs.puzzlesapi.impl.iteminteractions.capability.ContainerClientInputCapability;
import fuzs.puzzlesapi.impl.iteminteractions.capability.EnderChestMenuCapability;
import fuzs.puzzlesapi.impl.iteminteractions.init.ModRegistry;
import fuzs.puzzlesapi.impl.limitlesscontainers.LimitlessContainers;
import fuzs.puzzlesapi.impl.slotcycling.SlotCycling;
import fuzs.puzzlesapi.impl.statues.Statues;
import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(PuzzlesApi.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PuzzlesApiForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(PuzzlesApi.MOD_ID, ItemInteractions::new);
        ModConstructor.construct(PuzzlesApi.MOD_ID, SlotCycling::new);
        ModConstructor.construct(PuzzlesApi.MOD_ID, LimitlessContainers::new);
        ModConstructor.construct(PuzzlesApi.MOD_ID, Statues::new);
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.ENDER_CHEST_MENU_CAPABILITY, new CapabilityToken<EnderChestMenuCapability>() {});
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.CONTAINER_SLOT_CAPABILITY, new CapabilityToken<ContainerClientInputCapability>() {});
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        evt.getGenerator().addProvider(true, new ModLanguageProvider(evt, PuzzlesApi.MOD_ID));
        evt.getGenerator().addProvider(true, new ModSpriteSourceProvider(evt, PuzzlesApi.MOD_ID));
    }
}
