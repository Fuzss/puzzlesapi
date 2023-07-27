package fuzs.puzzlesapi.impl.statues.client;

import fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand.*;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.PosePartMutator;
import fuzs.puzzlesapi.impl.statues.Statues;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;

public class StatuesClient implements ClientModConstructor {

    @Override
    public void onClientSetup() {
        ArmorStandScreenFactory.register(ArmorStandScreenType.EQUIPMENT, ArmorStandEquipmentScreen::new);
        ArmorStandScreenFactory.register(ArmorStandScreenType.ROTATIONS, ArmorStandRotationsScreen::new);
        ArmorStandScreenFactory.register(ArmorStandScreenType.STYLE, ArmorStandStyleScreen::new);
        ArmorStandScreenFactory.register(ArmorStandScreenType.POSES, ArmorStandPosesScreen::new);
        ArmorStandScreenFactory.register(ArmorStandScreenType.POSITION, ArmorStandPositionScreen::new);
        ArmorStandRotationsScreen.registerPosePartMutatorFilter(PosePartMutator.LEFT_ARM, ArmorStand::isShowArms);
        ArmorStandRotationsScreen.registerPosePartMutatorFilter(PosePartMutator.RIGHT_ARM, ArmorStand::isShowArms);
    }

    @Override
    public ResourceLocation getPairingIdentifier() {
        return Statues.id("statues");
    }
}
