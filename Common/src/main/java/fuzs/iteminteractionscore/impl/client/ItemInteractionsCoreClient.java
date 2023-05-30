package fuzs.iteminteractionscore.impl.client;

import fuzs.iteminteractionscore.api.client.container.v1.tooltip.ClientContainerItemTooltip;
import fuzs.iteminteractionscore.api.client.container.v1.tooltip.ModClientBundleTooltip;
import fuzs.iteminteractionscore.api.container.v1.tooltip.ContainerItemTooltip;
import fuzs.iteminteractionscore.api.container.v1.tooltip.ModBundleTooltip;
import fuzs.iteminteractionscore.impl.client.core.HeldActivationType;
import fuzs.iteminteractionscore.impl.client.core.KeyMappingProvider;
import fuzs.iteminteractionscore.impl.client.handler.ClientInputActionHandler;
import fuzs.iteminteractionscore.impl.client.handler.EnderChestMenuClientHandler;
import fuzs.iteminteractionscore.impl.client.handler.KeyBindingTogglesHandler;
import fuzs.iteminteractionscore.impl.client.handler.MouseDraggingHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ClientTooltipComponentsContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenKeyboardEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenMouseEvents;
import fuzs.puzzleslib.api.event.v1.PlayLevelSoundEvents;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class ItemInteractionsCoreClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientEntityLevelEvents.LOAD.register(EnderChestMenuClientHandler::onEntityJoinLevel);
        ScreenMouseEvents.beforeMouseClick(AbstractContainerScreen.class).register(EventPhase.BEFORE, ClientInputActionHandler::onBeforeMousePressed);
        ScreenMouseEvents.beforeMouseClick(AbstractContainerScreen.class).register(MouseDraggingHandler.INSTANCE::onBeforeMousePressed);
        ScreenMouseEvents.beforeMouseRelease(AbstractContainerScreen.class).register(MouseDraggingHandler.INSTANCE::onBeforeMouseRelease);
        ScreenMouseEvents.beforeMouseRelease(AbstractContainerScreen.class).register(ClientInputActionHandler::onBeforeMouseRelease);
        ScreenMouseEvents.beforeMouseScroll(AbstractContainerScreen.class).register(EventPhase.BEFORE, ClientInputActionHandler::onBeforeMouseScroll);
        ScreenMouseEvents.beforeMouseDrag(AbstractContainerScreen.class).register(MouseDraggingHandler.INSTANCE::onBeforeMouseDragged);
        ScreenKeyboardEvents.beforeKeyPress(AbstractContainerScreen.class).register(EventPhase.BEFORE, ClientInputActionHandler::onBeforeKeyPressed);
        ScreenKeyboardEvents.beforeKeyPress(AbstractContainerScreen.class).register(KeyBindingTogglesHandler::onBeforeKeyPressed);
        ScreenEvents.afterRender(AbstractContainerScreen.class).register(ClientInputActionHandler::onAfterRender);
        PlayLevelSoundEvents.ENTITY.register(MouseDraggingHandler.INSTANCE::onPlaySoundAtPosition);
        PlayLevelSoundEvents.ENTITY.register(ClientInputActionHandler::onPlaySoundAtPosition);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        HeldActivationType.getKeyMappingProviders().map(KeyMappingProvider::getKeyMapping).forEach(context::registerKeyMappings);
    }

    @Override
    public void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
        context.registerClientTooltipComponent(ContainerItemTooltip.class, ClientContainerItemTooltip::new);
        context.registerClientTooltipComponent(ModBundleTooltip.class, ModClientBundleTooltip::new);
    }
}
