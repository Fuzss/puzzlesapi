package fuzs.iteminteractionscore.impl.init;

import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.capability.ContainerClientInputCapability;
import fuzs.iteminteractionscore.impl.capability.ContainerClientInputCapabilityImpl;
import fuzs.iteminteractionscore.impl.capability.EnderChestMenuCapability;
import fuzs.iteminteractionscore.impl.capability.EnderChestMenuCapabilityImpl;
import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.capability.v2.data.PlayerRespawnStrategy;

public class ModRegistry {
    private static final CapabilityController CAPABILITIES = CapabilityController.from(ItemInteractionsCore.MOD_ID);
    public static final CapabilityKey<ContainerClientInputCapability> CONTAINER_SLOT_CAPABILITY = CAPABILITIES.registerPlayerCapability("container_client_input", ContainerClientInputCapability.class, player -> new ContainerClientInputCapabilityImpl(), PlayerRespawnStrategy.ALWAYS_COPY);
    public static final CapabilityKey<EnderChestMenuCapability> ENDER_CHEST_MENU_CAPABILITY = CAPABILITIES.registerPlayerCapability("ender_chest_menu", EnderChestMenuCapability.class, player -> new EnderChestMenuCapabilityImpl(), PlayerRespawnStrategy.NEVER);

    public static void touch() {

    }
}
