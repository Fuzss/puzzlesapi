package fuzs.puzzlesapi.impl.slotcycling;

import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.slotcycling.config.ClientConfig;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class SlotCycling implements ModConstructor {
    public static final String MOD_ID = PuzzlesApi.MOD_ID;
    public static final String MOD_NAME = PuzzlesApi.MOD_NAME;
    public static final Logger LOGGER = PuzzlesApi.LOGGER;

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class)
            .setFileName(ClientConfig.class, modId -> modId + "-slotcycling-client.toml");

    @Override
    public ResourceLocation getPairingIdentifier() {
        return id("slotcycling");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
