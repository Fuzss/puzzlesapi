package fuzs.puzzlesapi.impl.iteminteractions;

import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.iteminteractions.config.ClientConfig;
import fuzs.puzzlesapi.impl.iteminteractions.config.ServerConfig;
import fuzs.puzzlesapi.impl.iteminteractions.handler.EnderChestMenuHandler;
import fuzs.puzzlesapi.impl.iteminteractions.init.ModRegistry;
import fuzs.puzzlesapi.impl.iteminteractions.network.S2CEnderChestSetContentMessage;
import fuzs.puzzlesapi.impl.iteminteractions.network.S2CEnderChestSetSlotMessage;
import fuzs.puzzlesapi.impl.iteminteractions.network.S2CSyncItemContainerProvider;
import fuzs.puzzlesapi.impl.iteminteractions.network.client.C2SContainerClientInputMessage;
import fuzs.puzzlesapi.impl.iteminteractions.network.client.C2SEnderChestMenuMessage;
import fuzs.puzzlesapi.impl.iteminteractions.network.client.C2SEnderChestSetSlotMessage;
import fuzs.puzzlesapi.impl.iteminteractions.world.item.container.ItemContainerProviders;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.event.v1.server.SyncDataPackContentsCallback;
import fuzs.puzzleslib.api.network.v2.MessageDirection;
import fuzs.puzzleslib.api.network.v2.NetworkHandlerV2;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class ItemInteractions implements ModConstructor {
    public static final String MOD_ID = PuzzlesApi.MOD_ID;
    public static final String MOD_NAME = PuzzlesApi.MOD_NAME;
    public static final Logger LOGGER = PuzzlesApi.LOGGER;

    public static final NetworkHandlerV2 NETWORK = NetworkHandlerV2.build(MOD_ID, "iteminteractions", true, true);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class).server(ServerConfig.class)
            .setFileName(ClientConfig.class, modId -> modId + "-iteminteractions-client.toml")
            .setFileName(ServerConfig.class, modId -> modId + "-iteminteractions-server.toml");

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerMessages();
        registerHandlers();
    }

    private static void registerMessages() {
        NETWORK.register(C2SContainerClientInputMessage.class, C2SContainerClientInputMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CEnderChestSetContentMessage.class, S2CEnderChestSetContentMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CEnderChestSetSlotMessage.class, S2CEnderChestSetSlotMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(C2SEnderChestSetSlotMessage.class, C2SEnderChestSetSlotMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SEnderChestMenuMessage.class, C2SEnderChestMenuMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(S2CSyncItemContainerProvider.class, S2CSyncItemContainerProvider::new, MessageDirection.TO_CLIENT);
    }

    private static void registerHandlers() {
        PlayerTickEvents.START.register(EnderChestMenuHandler::onStartPlayerTick);
        SyncDataPackContentsCallback.EVENT.register(ItemContainerProviders.INSTANCE::onSyncDataPackContents);
    }

    @Override
    public void onRegisterDataPackReloadListeners(AddReloadListenersContext context) {
        context.registerReloadListener(ItemContainerProviders.ITEM_CONTAINER_PROVIDERS_KEY, ItemContainerProviders.INSTANCE);
    }

    @Override
    public ResourceLocation getPairingIdentifier() {
        return id("iteminteractions");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
