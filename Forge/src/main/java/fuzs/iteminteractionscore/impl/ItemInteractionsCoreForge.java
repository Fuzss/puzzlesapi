package fuzs.iteminteractionscore.impl;

import fuzs.iteminteractionscore.impl.capability.ContainerClientInputCapability;
import fuzs.iteminteractionscore.impl.capability.EnderChestMenuCapability;
import fuzs.iteminteractionscore.impl.data.ModItemContainerProvider;
import fuzs.iteminteractionscore.impl.data.ModLanguageProvider;
import fuzs.iteminteractionscore.impl.init.ModRegistry;
import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.concurrent.CompletableFuture;

@Mod(ItemInteractionsCore.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInteractionsCoreForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(ItemInteractionsCore.MOD_ID, ItemInteractionsCore::new);
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.ENDER_CHEST_MENU_CAPABILITY, new CapabilityToken<EnderChestMenuCapability>() {});
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.CONTAINER_SLOT_CAPABILITY, new CapabilityToken<ContainerClientInputCapability>() {});
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        final DataGenerator dataGenerator = evt.getGenerator();
        final PackOutput packOutput = dataGenerator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = evt.getLookupProvider();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModItemContainerProvider(packOutput));
        dataGenerator.addProvider(true, new ModLanguageProvider(packOutput, ItemInteractionsCore.MOD_ID));
    }
}
