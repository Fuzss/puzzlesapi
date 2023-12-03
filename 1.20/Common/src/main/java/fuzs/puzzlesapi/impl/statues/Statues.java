package fuzs.puzzlesapi.impl.statues;

import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandStyleOption;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandStyleOptions;
import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.statues.network.client.*;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.network.v2.MessageDirection;
import fuzs.puzzleslib.api.network.v2.NetworkHandlerV2;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Locale;

public class Statues implements ModConstructor {
    public static final String MOD_ID = PuzzlesApi.MOD_ID;
    public static final String MOD_NAME = PuzzlesApi.MOD_NAME;
    public static final Logger LOGGER = PuzzlesApi.LOGGER;

    public static final NetworkHandlerV2 NETWORK = NetworkHandlerV2.build(MOD_ID, "statues", true, true);

    @Override
    public void onConstructMod() {
        registerMessages();
    }

    private static void registerMessages() {
        NETWORK.register(C2SArmorStandNameMessage.class, C2SArmorStandNameMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SArmorStandStyleMessage.class, C2SArmorStandStyleMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SArmorStandPositionMessage.class, C2SArmorStandPositionMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SArmorStandPoseMessage.class, C2SArmorStandPoseMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SArmorStandRotationMessage.class, C2SArmorStandRotationMessage::new, MessageDirection.TO_SERVER);
    }

    @Override
    public void onCommonSetup() {
        // do this here instead of in enum constructor to avoid potential issues with the enum class not having been loaded yet on server-side, therefore nothing being registered
        for (ArmorStandStyleOptions styleOption : ArmorStandStyleOptions.values()) {
            ArmorStandStyleOption.register(id(styleOption.getName().toLowerCase(Locale.ROOT)), styleOption);
        }
    }

    @Override
    public ResourceLocation getPairingIdentifier() {
        return id("statues");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
