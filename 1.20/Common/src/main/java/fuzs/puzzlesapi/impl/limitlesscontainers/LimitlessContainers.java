package fuzs.puzzlesapi.impl.limitlesscontainers;

import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.limitlesscontainers.network.ClientboundContainerSetContentMessage;
import fuzs.puzzlesapi.impl.limitlesscontainers.network.ClientboundContainerSetSlotMessage;
import fuzs.puzzlesapi.impl.limitlesscontainers.network.client.ServerboundContainerClickMessage;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.network.v2.MessageDirection;
import fuzs.puzzleslib.api.network.v2.NetworkHandlerV2;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class LimitlessContainers implements ModConstructor {
    public static final String MOD_ID = PuzzlesApi.MOD_ID;
    public static final String MOD_NAME = PuzzlesApi.MOD_NAME;
    public static final Logger LOGGER = PuzzlesApi.LOGGER;

    public static final NetworkHandlerV2 NETWORK = NetworkHandlerV2.build(MOD_ID, "limitlesscontainers", true, true);

    @Override
    public void onConstructMod() {
        registerMessages();
    }

    private static void registerMessages() {
        NETWORK.register(ServerboundContainerClickMessage.class, ServerboundContainerClickMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(ClientboundContainerSetSlotMessage.class, ClientboundContainerSetSlotMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(ClientboundContainerSetContentMessage.class, ClientboundContainerSetContentMessage::new, MessageDirection.TO_CLIENT);
    }

    @Override
    public ResourceLocation getPairingIdentifier() {
        return id("limitlesscontainers");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
