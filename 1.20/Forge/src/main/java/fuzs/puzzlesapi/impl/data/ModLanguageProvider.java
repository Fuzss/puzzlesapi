package fuzs.puzzlesapi.impl.data;

import fuzs.puzzlesapi.api.client.iteminteractions.v1.tooltip.ExpandableClientTooltipComponent;
import fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand.*;
import fuzs.puzzlesapi.api.statues.v1.helper.ArmorStandInteractHelper;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.*;
import fuzs.puzzlesapi.impl.PuzzlesApi;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.HeldActivationType;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.KeyBackedActivationType;
import fuzs.puzzlesapi.impl.iteminteractions.client.handler.KeyBindingTogglesHandler;
import fuzs.puzzlesapi.impl.slotcycling.client.handler.CyclingInputHandler;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        // Item Interactions
        this.add(ExpandableClientTooltipComponent.REVEAL_CONTENTS_TRANSLATION_KEY, "%s %s to reveal contents");
        this.add(HeldActivationType.TOOLTIP_HOLD_TRANSLATION_KEY, "Hold");
        this.add(KeyBackedActivationType.TOOLTIP_PRESS_TRANSLATION_KEY, "Press");
        this.add(KeyBindingTogglesHandler.VISUAL_ITEM_CONTENTS_KEY.getKeyMapping(), "Toggle Visual Item Contents");
        this.add(KeyBindingTogglesHandler.SELECTED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Selected Item Tooltips");
        this.add(KeyBindingTogglesHandler.CARRIED_ITEM_TOOLTIPS_KEY.getKeyMapping(), "Toggle Carried Item Tooltips");
        this.add("key.categories." + PuzzlesApi.MOD_ID, PuzzlesApi.MOD_NAME);
        // Slot Cycling
        this.add(CyclingInputHandler.CYCLE_LEFT_KEY_MAPPING, "Cycle Hotbar Slot Left");
        this.add(CyclingInputHandler.CYCLE_RIGHT_KEY_MAPPING, "Cycle Hotbar Slot Right");
        // Statues
        this.add(ArmorStandInteractHelper.OPEN_SCREEN_TRANSLATION_KEY, "Use [%s] + [%s] with an empty hand to open configuration screen.");
        this.add(ArmorStandPosesScreen.POSE_SOURCE_TRANSLATION_KEY, "By %s");
        this.add(ArmorStandPose.ATHENA.getTranslationKey(), "Athena");
        this.add(ArmorStandPose.BRANDISH.getTranslationKey(), "Brandish");
        this.add(ArmorStandPose.CANCAN.getTranslationKey(), "Cancan");
        this.add(ArmorStandPose.DEFAULT.getTranslationKey(), "Default");
        this.add(ArmorStandPose.ENTERTAIN.getTranslationKey(), "Entertain");
        this.add(ArmorStandPose.HERO.getTranslationKey(), "Hero");
        this.add(ArmorStandPose.HONOR.getTranslationKey(), "Honor");
        this.add(ArmorStandPose.RIPOSTE.getTranslationKey(), "Riposte");
        this.add(ArmorStandPose.SALUTE.getTranslationKey(), "Salute");
        this.add(ArmorStandPose.SOLEMN.getTranslationKey(), "Solemn");
        this.add(ArmorStandPose.ZOMBIE.getTranslationKey(), "Zombie");
        this.add(ArmorStandPose.WALKING.getTranslationKey(), "Walking");
        this.add(ArmorStandPose.RUNNING.getTranslationKey(), "Running");
        this.add(ArmorStandPose.POINTING.getTranslationKey(), "Pointing");
        this.add(ArmorStandPose.BLOCKING.getTranslationKey(), "Blocking");
        this.add(ArmorStandPose.LUNGEING.getTranslationKey(), "Lungeing");
        this.add(ArmorStandPose.WINNING.getTranslationKey(), "Winning");
        this.add(ArmorStandPose.SITTING.getTranslationKey(), "Sitting");
        this.add(ArmorStandPose.ARABESQUE.getTranslationKey(), "Arabesque");
        this.add(ArmorStandPose.CUPID.getTranslationKey(), "Cupid");
        this.add(ArmorStandPose.CONFIDENT.getTranslationKey(), "Confident");
        this.add(ArmorStandPose.DEATH.getTranslationKey(), "Death");
        this.add(ArmorStandPose.FACEPALM.getTranslationKey(), "Facepalm");
        this.add(ArmorStandPose.LAZING.getTranslationKey(), "Lazing");
        this.add(ArmorStandPose.CONFUSED.getTranslationKey(), "Confused");
        this.add(ArmorStandPose.FORMAL.getTranslationKey(), "Formal");
        this.add(ArmorStandPose.SAD.getTranslationKey(), "Sad");
        this.add(ArmorStandPose.JOYOUS.getTranslationKey(), "Joyous");
        this.add(ArmorStandPose.STARGAZING.getTranslationKey(), "Stargazing");
        this.add(ArmorStandScreenType.EQUIPMENT.getTranslationKey(), "Equipment");
        this.add(ArmorStandScreenType.ROTATIONS.getTranslationKey(), "Rotations");
        this.add(ArmorStandScreenType.STYLE.getTranslationKey(), "Style");
        this.add(ArmorStandScreenType.POSES.getTranslationKey(), "Poses");
        this.add(ArmorStandScreenType.POSITION.getTranslationKey(), "Position");
        this.add(ArmorStandStyleScreen.TEXT_BOX_TRANSLATION_KEY, "Set a name to display above the entity if enabled.");
        this.add(ArmorStandStyleOptions.SHOW_ARMS.getTranslationKey(), "Show Arms");
        this.add(ArmorStandStyleOptions.SHOW_ARMS.getDescriptionKey(), "Shows the statue's arms, so it may hold items in both hands.");
        this.add(ArmorStandStyleOptions.SMALL.getTranslationKey(), "Small");
        this.add(ArmorStandStyleOptions.SMALL.getDescriptionKey(), "Makes the statue half it's size like a baby mob.");
        this.add(ArmorStandStyleOptions.INVISIBLE.getTranslationKey(), "Invisible");
        this.add(ArmorStandStyleOptions.INVISIBLE.getDescriptionKey(), "Makes the statue itself invisible, but still shows all equipped items.");
        this.add(ArmorStandStyleOptions.NO_BASE_PLATE.getTranslationKey(), "No Base Plate");
        this.add(ArmorStandStyleOptions.NO_BASE_PLATE.getDescriptionKey(), "Hide the stone base plate at the statue's feet.");
        this.add(ArmorStandStyleOptions.SHOW_NAME.getTranslationKey(), "Show Name");
        this.add(ArmorStandStyleOptions.SHOW_NAME.getDescriptionKey(), "Render the statue's name tag above it's head.");
        this.add(ArmorStandStyleOptions.NO_GRAVITY.getTranslationKey(), "No Gravity");
        this.add(ArmorStandStyleOptions.NO_GRAVITY.getDescriptionKey(), "Prevents the statue from falling down, so it may float freely.");
        this.add(ArmorStandStyleOptions.SEALED.getTranslationKey(), "Sealed");
        this.add(ArmorStandStyleOptions.SEALED.getDescriptionKey(), "The statue can no longer be broken, equipment cannot be changed. Disallows opening this menu in survival mode.");
        this.add(ArmorStandPositionScreen.ROTATION_TRANSLATION_KEY, "Rotation:");
        this.add(ArmorStandPositionScreen.POSITION_X_TRANSLATION_KEY, "X-Position:");
        this.add(ArmorStandPositionScreen.POSITION_Y_TRANSLATION_KEY, "Y-Position:");
        this.add(ArmorStandPositionScreen.POSITION_Z_TRANSLATION_KEY, "Z-Position:");
        this.add(ArmorStandPositionScreen.INCREMENT_TRANSLATION_KEY, "Increment by %s");
        this.add(ArmorStandPositionScreen.DECREMENT_TRANSLATION_KEY, "Decrement by %s");
        this.add(ArmorStandPositionScreen.DEGREES_TRANSLATION_KEY, "%s\u00B0");
        this.add(ArmorStandPositionScreen.MOVE_BY_TRANSLATION_KEY, "Move By:");
        this.add(ArmorStandPositionScreen.PIXELS_TRANSLATION_KEY, "%s Pixel(s)");
        this.add(ArmorStandPositionScreen.BLOCKS_TRANSLATION_KEY, "%s Block(s)");
        this.add(ArmorStandPositionScreen.CENTERED_TRANSLATION_KEY, "Align Centered");
        this.add(ArmorStandPositionScreen.CENTERED_DESCRIPTION_TRANSLATION_KEY, "Align an armor stand in the center of the block position it is placed on.");
        this.add(ArmorStandPositionScreen.CORNERED_TRANSLATION_KEY, "Align Cornered");
        this.add(ArmorStandPositionScreen.CORNERED_DESCRIPTION_TRANSLATION_KEY, "Align an armor stand at the corner of the block position it is placed on.");
        this.add(ArmorStandPositionScreen.ALIGNED_TRANSLATION_KEY, "Aligned!");
        this.add(PosePartMutator.HEAD.getTranslationKey(), "Head");
        this.add(PosePartMutator.BODY.getTranslationKey(), "Body");
        this.add(PosePartMutator.LEFT_ARM.getTranslationKey(), "Left Arm");
        this.add(PosePartMutator.RIGHT_ARM.getTranslationKey(), "Right Arm");
        this.add(PosePartMutator.LEFT_LEG.getTranslationKey(), "Left Leg");
        this.add(PosePartMutator.RIGHT_LEG.getTranslationKey(), "Right Leg");
        this.add(PosePartMutator.AXIS_X_TRANSLATION_KEY, "X: %s");
        this.add(PosePartMutator.AXIS_Y_TRANSLATION_KEY, "Y: %s");
        this.add(PosePartMutator.AXIS_Z_TRANSLATION_KEY, "Z: %s");
        this.add(ArmorStandRotationsScreen.TIP_TRANSLATION_KEY + 1, "Hold any [§dShift§r] or [§dAlt§r] key to lock two-dimensional sliders to a single axis while dragging!");
        this.add(ArmorStandRotationsScreen.TIP_TRANSLATION_KEY + 2, "Use arrow keys to move sliders with greater precision than when dragging! Focus a slider first by clicking.");
        this.add(ArmorStandRotationsScreen.RESET_TRANSLATION_KEY, "Reset");
        this.add(ArmorStandRotationsScreen.RANDOMIZE_TRANSLATION_KEY, "Randomize");
        this.add(ArmorStandRotationsScreen.LIMITED_TRANSLATION_KEY, "Limited Rotations");
        this.add(ArmorStandRotationsScreen.UNLIMITED_TRANSLATION_KEY, "Unlimited Rotations");
        this.add(ArmorStandRotationsScreen.COPY_TRANSLATION_KEY, "Copy");
        this.add(ArmorStandRotationsScreen.PASTE_TRANSLATION_KEY, "Paste");
        this.add(ArmorStandRotationsScreen.MIRROR_TRANSLATION_KEY, "Mirror");
        this.add(ArmorStandAlignment.BLOCK.getTranslationKey(), "Align Block On Surface");
        this.add(ArmorStandAlignment.BLOCK.getDescriptionsKey(), "Align an armor stand placed on a surface so that a block held by it appears on the surface.");
        this.add(ArmorStandAlignment.FLOATING_ITEM.getTranslationKey(), "Align Item On Surface");
        this.add(ArmorStandAlignment.FLOATING_ITEM.getDescriptionsKey(), "Align an armor stand placed on a surface so that an item held by it appears upright on the surface.");
        this.add(ArmorStandAlignment.FLAT_ITEM.getTranslationKey(), "Align Item Flat On Surface");
        this.add(ArmorStandAlignment.FLAT_ITEM.getDescriptionsKey(), "Align an armor stand placed on a surface so that a non-tool item held by it appears flat on the surface.");
        this.add(ArmorStandAlignment.TOOL.getTranslationKey(), "Align Tool Flat On Surface");
        this.add(ArmorStandAlignment.TOOL.getDescriptionsKey(), "Align an armor stand placed on a surface so that a tool held by it appears flat on the surface.");
        this.add(AbstractArmorStandScreen.CREDITS_TRANSLATION_KEY, "Some content on this page originates from the Vanilla Tweaks \"Armor Statues\" data pack. Click this button to go to their website!");
    }
}
