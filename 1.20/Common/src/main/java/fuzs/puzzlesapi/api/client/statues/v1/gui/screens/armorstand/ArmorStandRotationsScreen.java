package fuzs.puzzlesapi.api.client.statues.v1.gui.screens.armorstand;

import com.google.common.collect.Maps;
import fuzs.puzzlesapi.api.client.statues.v1.gui.components.BoxedSliderButton;
import fuzs.puzzlesapi.api.client.statues.v1.gui.components.LiveSliderButton;
import fuzs.puzzlesapi.api.client.statues.v1.gui.components.NewTextureTickButton;
import fuzs.puzzlesapi.api.client.statues.v1.gui.components.VerticalSliderButton;
import fuzs.puzzlesapi.api.statues.v1.network.client.data.DataSyncHandler;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.ArmorStandHolder;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandPose;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.PosePartMutator;
import fuzs.puzzlesapi.impl.statues.Statues;
import fuzs.puzzleslib.api.client.screen.v2.ScreenTooltipFactory;
import net.minecraft.Util;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ArmorStandRotationsScreen extends AbstractArmorStandScreen {
    public static final String TIP_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.tip";
    public static final String UNLIMITED_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.unlimited";
    public static final String LIMITED_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.limited";
    public static final String RESET_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.reset";
    public static final String RANDOMIZE_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.randomize";
    public static final String PASTE_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.paste";
    public static final String COPY_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.copy";
    public static final String MIRROR_TRANSLATION_KEY = Statues.MOD_ID + ".screen.rotations.mirror";
    private static final Map<PosePartMutator, Predicate<ArmorStand>> POSE_PART_MUTATOR_FILTERS = Maps.newHashMap();

    private static boolean clampRotations = true;
    @Nullable
    private static ArmorStandPose clipboard;

    private final AbstractWidget[] lockButtons = new AbstractWidget[2];
    private ArmorStandPose currentPose;

    public ArmorStandRotationsScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.inventoryEntityX = 80;
        this.inventoryEntityY = 58;
        this.smallInventoryEntity = true;
        this.currentPose = ArmorStandPose.fromEntity(holder.getArmorStand());
    }

    @Override
    protected void init() {
        super.init();
        this.lockButtons[0] = Util.make(this.addRenderableWidget(new ImageButton(this.leftPos + 83, this.topPos + 10, 20, 20, 156, 124, 20, getArmorStandWidgetsLocation(), 256, 256, button -> {
            clampRotations = true;
            this.toggleLockButtons();
            this.refreshLiveButtons();
        }, CommonComponents.EMPTY)), widget -> {
            widget.setTooltip(Tooltip.create(Component.translatable(UNLIMITED_TRANSLATION_KEY)));
        });
        this.lockButtons[1] = Util.make(this.addRenderableWidget(new ImageButton(this.leftPos + 83, this.topPos + 10, 20, 20, 136, 124, 20, getArmorStandWidgetsLocation(), 256, 256, button -> {
            clampRotations = false;
            this.toggleLockButtons();
            this.refreshLiveButtons();
        }, CommonComponents.EMPTY)), widget -> {
            widget.setTooltip(Tooltip.create(Component.translatable(LIMITED_TRANSLATION_KEY)));
        });
        Component tipComponent = this.getTipComponent();
        if (tipComponent != null) {
            this.addRenderableWidget(new ImageButton(this.leftPos + 107, this.topPos + 10, 20, 20, 136, 64, 20, getArmorStandWidgetsLocation(), 256, 256, button -> {}) {

                @Nullable
                @Override
                public ComponentPath nextFocusPath(FocusNavigationEvent event) {
                    return null;
                }

                @Override
                public boolean mouseClicked(double mouseX, double mouseY, int button) {
                    return false;
                }
            }).setTooltip(Tooltip.create(tipComponent));
        }
        this.addRenderableWidget(new NewTextureTickButton(this.leftPos + 83, this.topPos + 34, 20, 20, 192, 124, getArmorStandWidgetsLocation(), button -> {
            this.setCurrentPose(this.holder.getDataProvider().getRandomPose(true));
        })).setTooltip(Tooltip.create(Component.translatable(RANDOMIZE_TRANSLATION_KEY)));
        this.addRenderableWidget(new NewTextureTickButton(this.leftPos + 107, this.topPos + 34, 20, 20, 240, 124, getArmorStandWidgetsLocation(), button -> {
            this.setCurrentPose(ArmorStandPose.EMPTY);
        })).setTooltip(Tooltip.create(Component.translatable(RESET_TRANSLATION_KEY)));
        this.addRenderableWidget(new NewTextureTickButton(this.leftPos + 83, this.topPos + 134, 44, 20, 179, 0, getArmorStandWidgetsLocation(), button -> {
            this.setCurrentPose(this.currentPose.mirror());
        })).setTooltip(Tooltip.create(Component.translatable(MIRROR_TRANSLATION_KEY)));
        AbstractWidget[] pasteButton = new AbstractWidget[1];
        this.addRenderableWidget(new NewTextureTickButton(this.leftPos + 83, this.topPos + 158, 20, 20, 208, 124, getArmorStandWidgetsLocation(), button -> {
            clipboard = this.currentPose;
            pasteButton[0].active = true;
        })).setTooltip(Tooltip.create(Component.translatable(COPY_TRANSLATION_KEY)));
        pasteButton[0] = Util.make(this.addRenderableWidget(new NewTextureTickButton(this.leftPos + 107, this.topPos + 158, 20, 20, 224, 124, getArmorStandWidgetsLocation(), button -> {
            if (clipboard != null) this.setCurrentPose(clipboard);
        })), widget -> {
            widget.setTooltip(Tooltip.create(Component.translatable(PASTE_TRANSLATION_KEY)));
            widget.active = clipboard != null;
        });
        ArmorStand armorStand = this.holder.getArmorStand();
        PosePartMutator[] posePartMutators = this.holder.getDataProvider().getPosePartMutators();
        ArmorStandPose.checkMutatorsSize(posePartMutators);
        for (int i = 0; i < posePartMutators.length; i++) {
            PosePartMutator mutator = posePartMutators[i];
            boolean isLeft = i % 2 == 0;
            this.addRenderableWidget(new BoxedSliderButton(this.leftPos + 23 + i % 2 * 110, this.topPos + 7 + i / 2 * 60, () -> mutator.getNormalizedRotationsAtAxis(1, this.currentPose, clampRotations), () -> mutator.getNormalizedRotationsAtAxis(0, this.currentPose, clampRotations)) {
                private boolean dirty;

                @Override
                public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    List<Component> lines = Lists.newArrayList();
                    lines.add(Component.translatable(mutator.getTranslationKey()));
                    lines.add(mutator.getAxisComponent(ArmorStandRotationsScreen.this.currentPose, 0));
                    lines.add(mutator.getAxisComponent(ArmorStandRotationsScreen.this.currentPose, 1));
                    this.setTooltip(ScreenTooltipFactory.create(ArmorStandRotationsScreen.this.font, lines));
                    super.render(guiGraphics, mouseX, mouseY, partialTick);
                }

                @Override
                protected ClientTooltipPositioner createTooltipPositioner() {
                    ClientTooltipPositioner tooltipPositioner = super.createTooltipPositioner();
                    if (tooltipPositioner instanceof MenuTooltipPositioner) {
                        return (screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight) -> {
                            if (isLeft) mouseX -= 24 + tooltipWidth;
                            return DefaultTooltipPositioner.INSTANCE.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
                        };
                    }
                    return tooltipPositioner;
                }

                @Override
                protected void applyValue() {
                    this.dirty = true;
                    ArmorStandRotationsScreen.this.currentPose = mutator.setRotationsAtAxis(1, ArmorStandRotationsScreen.this.currentPose, this.horizontalValue, clampRotations);
                    ArmorStandRotationsScreen.this.currentPose = mutator.setRotationsAtAxis(0, ArmorStandRotationsScreen.this.currentPose, this.verticalValue, clampRotations);
                }

                @Override
                public void onRelease(double mouseX, double mouseY) {
                    super.onRelease(mouseX, mouseY);
                    this.clearDirty();
                }

                @Override
                public boolean isDirty() {
                    return this.dirty;
                }

                @Override
                public void clearDirty() {
                    if (this.isDirty()) {
                        this.dirty = false;
                        ArmorStandRotationsScreen.this.dataSyncHandler.sendPose(ArmorStandRotationsScreen.this.currentPose);
                    }
                }
            }).active = isPosePartMutatorActive(mutator, armorStand);
            this.addRenderableWidget(new VerticalSliderButton(this.leftPos + 6 + i % 2 * 183, this.topPos + 7 + i / 2 * 60, () -> mutator.getNormalizedRotationsAtAxis(2, this.currentPose, clampRotations)) {
                private boolean dirty;

                @Override
                public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    List<Component> lines = Lists.newArrayList();
                    lines.add(Component.translatable(mutator.getTranslationKey()));
                    lines.add(mutator.getAxisComponent(ArmorStandRotationsScreen.this.currentPose, 2));
                    this.setTooltip(ScreenTooltipFactory.create(ArmorStandRotationsScreen.this.font, lines));
                    super.render(guiGraphics, mouseX, mouseY, partialTick);
                }

                @Override
                protected ClientTooltipPositioner createTooltipPositioner() {
                    ClientTooltipPositioner tooltipPositioner = super.createTooltipPositioner();
                    if (tooltipPositioner instanceof MenuTooltipPositioner) {
                        return (screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight) -> {
                            if (isLeft) mouseX -= 24 + tooltipWidth;
                            return DefaultTooltipPositioner.INSTANCE.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
                        };
                    }
                    return tooltipPositioner;
                }

                @Override
                protected void applyValue() {
                    this.dirty = true;
                    ArmorStandRotationsScreen.this.currentPose = mutator.setRotationsAtAxis(2, ArmorStandRotationsScreen.this.currentPose, this.value, clampRotations);
                }

                @Override
                public void onRelease(double mouseX, double mouseY) {
                    super.onRelease(mouseX, mouseY);
                    this.clearDirty();
                }

                @Override
                public boolean isDirty() {
                    return this.dirty;
                }

                @Override
                public void clearDirty() {
                    if (this.isDirty()) {
                        this.dirty = false;
                        ArmorStandRotationsScreen.this.dataSyncHandler.sendPose(ArmorStandRotationsScreen.this.currentPose);
                    }
                }
            }).active = isPosePartMutatorActive(mutator, armorStand);
            this.toggleLockButtons();
        }
    }

    @Nullable
    private Component getTipComponent() {
        List<Component> components = Lists.newArrayList();
        for (int i = 1; Language.getInstance().has(TIP_TRANSLATION_KEY + i); i++) {
            components.add(Component.translatable(TIP_TRANSLATION_KEY + i));
        }
        Collections.shuffle(components);
        return components.stream().findAny().orElse(null);
    }

    private void toggleLockButtons() {
        this.lockButtons[0].visible = !clampRotations;
        this.lockButtons[1].visible = clampRotations;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        ArmorStand armorStand = this.holder.getArmorStand();
        ArmorStandPose entityPose = ArmorStandPose.fromEntity(armorStand);
        this.currentPose.applyToEntity(armorStand);
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        entityPose.applyToEntity(armorStand);
    }

    @Override
    protected boolean withCloseButton() {
        return false;
    }

    @Override
    public ArmorStandScreenType getScreenType() {
        return ArmorStandScreenType.ROTATIONS;
    }

    private void setCurrentPose(ArmorStandPose currentPose) {
        this.currentPose = currentPose;
        this.dataSyncHandler.sendPose(this.currentPose);
        this.refreshLiveButtons();
    }

    private void refreshLiveButtons() {
        for (GuiEventListener child : this.children()) {
            if (child instanceof LiveSliderButton button) {
                button.refreshValues();
            }
        }
    }

    public static void registerPosePartMutatorFilter(PosePartMutator mutator, Predicate<ArmorStand> filter) {
        if (POSE_PART_MUTATOR_FILTERS.put(mutator, filter) != null) throw new IllegalStateException("Attempted to register duplicate pose part mutator filter for mutator %s".formatted(mutator));
    }

    private static boolean isPosePartMutatorActive(PosePartMutator mutator, ArmorStand armorStand) {
        return POSE_PART_MUTATOR_FILTERS.getOrDefault(mutator, armorStand1 -> true).test(armorStand);
    }
}
