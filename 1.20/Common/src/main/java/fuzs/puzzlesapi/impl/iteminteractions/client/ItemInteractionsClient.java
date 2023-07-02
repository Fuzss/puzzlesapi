package fuzs.puzzlesapi.impl.iteminteractions.client;

import fuzs.puzzlesapi.api.client.iteminteractions.v1.tooltip.ClientContainerItemTooltip;
import fuzs.puzzlesapi.api.client.iteminteractions.v1.tooltip.ModClientBundleTooltip;
import fuzs.puzzlesapi.api.iteminteractions.v1.tooltip.ContainerItemTooltip;
import fuzs.puzzlesapi.api.iteminteractions.v1.tooltip.ModBundleTooltip;
import fuzs.puzzlesapi.impl.iteminteractions.ItemInteractions;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.HeldActivationType;
import fuzs.puzzlesapi.impl.iteminteractions.client.core.KeyMappingProvider;
import fuzs.puzzlesapi.impl.iteminteractions.client.handler.ClientInputActionHandler;
import fuzs.puzzlesapi.impl.iteminteractions.client.handler.EnderChestMenuClientHandler;
import fuzs.puzzlesapi.impl.iteminteractions.client.handler.KeyBindingTogglesHandler;
import fuzs.puzzlesapi.impl.iteminteractions.client.handler.MouseDraggingHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ClientTooltipComponentsContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.*;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.api.event.v1.level.PlayLevelSoundEvents;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public class ItemInteractionsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientEntityLevelEvents.LOAD.register(EnderChestMenuClientHandler::onEntityJoinLevel);
        ScreenMouseEvents.beforeMouseClick(AbstractContainerScreen.class).register(EventPhase.BEFORE, ClientInputActionHandler::onBeforeMousePressed);
        ScreenMouseEvents.beforeMouseClick(AbstractContainerScreen.class).register(MouseDraggingHandler::onBeforeMousePressed);
        ScreenMouseEvents.beforeMouseRelease(AbstractContainerScreen.class).register(MouseDraggingHandler::onBeforeMouseRelease);
        ScreenMouseEvents.beforeMouseRelease(AbstractContainerScreen.class).register(ClientInputActionHandler::onBeforeMouseRelease);
        ScreenMouseEvents.beforeMouseScroll(AbstractContainerScreen.class).register(EventPhase.BEFORE, ClientInputActionHandler::onBeforeMouseScroll);
        ScreenMouseEvents.beforeMouseDrag(AbstractContainerScreen.class).register(MouseDraggingHandler::onBeforeMouseDragged);
        ScreenKeyboardEvents.beforeKeyPress(AbstractContainerScreen.class).register(EventPhase.BEFORE, ClientInputActionHandler::onBeforeKeyPressed);
        ScreenKeyboardEvents.beforeKeyPress(AbstractContainerScreen.class).register(KeyBindingTogglesHandler::onBeforeKeyPressed);
        ScreenEvents.afterRender(AbstractContainerScreen.class).register(ClientInputActionHandler::onAfterRender);
        ContainerScreenEvents.FOREGROUND.register(MouseDraggingHandler::onDrawForeground);
        PlayLevelSoundEvents.ENTITY.register(MouseDraggingHandler::onPlaySoundAtPosition);
        PlayLevelSoundEvents.ENTITY.register(ClientInputActionHandler::onPlaySoundAtPosition);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        HeldActivationType.getKeyMappingProviders().map(KeyMappingProvider::getKeyMapping).forEach(context::registerKeyMapping);
    }

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, ClientContainerItemTooltip::new);
        context.registerClientTooltipComponent(ModBundleTooltip.class, ModClientBundleTooltip::new);
    }

    @Override
    public ResourceLocation getPairingIdentifier() {
        return ItemInteractions.id("iteminteractions");
    }
}
