package fuzs.puzzlesapi.impl.iteminteractions.init;

import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.iteminteractions.capability.ContainerClientInputCapability;
import fuzs.puzzlesapi.impl.iteminteractions.capability.ContainerClientInputCapabilityImpl;
import fuzs.puzzlesapi.impl.iteminteractions.capability.EnderChestMenuCapability;
import fuzs.puzzlesapi.impl.iteminteractions.capability.EnderChestMenuCapabilityImpl;
import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.capability.v2.data.PlayerRespawnCopyStrategy;

public class ModRegistry {
    private static final CapabilityController CAPABILITIES = CapabilityController.from(PuzzlesApi.MOD_ID);
    public static final CapabilityKey<ContainerClientInputCapability> CONTAINER_SLOT_CAPABILITY = CAPABILITIES.registerPlayerCapability("container_client_input", ContainerClientInputCapability.class, player -> new ContainerClientInputCapabilityImpl(), PlayerRespawnCopyStrategy.ALWAYS);
    public static final CapabilityKey<EnderChestMenuCapability> ENDER_CHEST_MENU_CAPABILITY = CAPABILITIES.registerPlayerCapability("ender_chest_menu", EnderChestMenuCapability.class, player -> new EnderChestMenuCapabilityImpl(), PlayerRespawnCopyStrategy.NEVER);

    public static void touch() {

    }
}
