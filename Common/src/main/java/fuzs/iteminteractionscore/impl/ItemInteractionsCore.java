package fuzs.iteminteractionscore.impl;

import fuzs.iteminteractionscore.impl.config.ClientConfig;
import fuzs.iteminteractionscore.impl.config.ServerConfig;
import fuzs.iteminteractionscore.impl.handler.EnderChestMenuHandler;
import fuzs.iteminteractionscore.impl.init.ModRegistry;
import fuzs.iteminteractionscore.impl.network.S2CEnderChestSetContentMessage;
import fuzs.iteminteractionscore.impl.network.S2CEnderChestSetSlotMessage;
import fuzs.iteminteractionscore.impl.network.S2CSyncItemContainerProvider;
import fuzs.iteminteractionscore.impl.network.client.C2SContainerClientInputMessage;
import fuzs.iteminteractionscore.impl.network.client.C2SEnderChestMenuMessage;
import fuzs.iteminteractionscore.impl.network.client.C2SEnderChestSetSlotMessage;
import fuzs.iteminteractionscore.impl.world.item.container.ItemContainerProviders;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.event.v1.server.SyncDataPackContentsCallback;
import fuzs.puzzleslib.api.network.v2.MessageDirection;
import fuzs.puzzleslib.api.network.v2.NetworkHandlerV2;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemInteractionsCore implements ModConstructor {
    public static final String MOD_ID = "iteminteractionscore";
    public static final String MOD_NAME = "Item Interactions Core";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandlerV2 NETWORK = NetworkHandlerV2.build(MOD_ID);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class).server(ServerConfig.class);

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
        PlayerTickEvents.START.register(EnderChestMenuHandler::onLivingTick);
        SyncDataPackContentsCallback.EVENT.register(ItemContainerProviders.INSTANCE::sendProvidersToPlayer);
    }

    @Override
    public void onRegisterDataPackReloadListeners(AddReloadListenersContext context) {
        context.registerReloadListener(ItemContainerProviders.ITEM_CONTAINER_PROVIDERS_KEY, ItemContainerProviders.INSTANCE);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
