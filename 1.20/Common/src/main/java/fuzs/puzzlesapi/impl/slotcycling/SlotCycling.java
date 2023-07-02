package fuzs.puzzlesapi.impl.slotcycling;

import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.slotcycling.config.ClientConfig;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;

public class SlotCycling implements ModConstructor {
    public static final ConfigHolder CONFIG = ConfigHolder.builder(PuzzlesApi.MOD_ID).client(ClientConfig.class)
            .setFileName(ClientConfig.class, modId -> modId + "-slotcycling-client.toml");
}
