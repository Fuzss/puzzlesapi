package fuzs.puzzlesapi.impl.slotcycling.client;

import fuzs.puzzlesapi.impl.slotcycling.client.handler.CyclingInputHandler;
import fuzs.puzzlesapi.impl.slotcycling.client.handler.SlotsRendererHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.InputEvents;
import fuzs.puzzleslib.api.client.event.v1.RenderGuiCallback;

public class SlotCyclingClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.START.register(CyclingInputHandler::onClientTick$Start);
        InputEvents.BEFORE_MOUSE_SCROLL.register(CyclingInputHandler::onBeforeMouseScroll);
        RenderGuiCallback.EVENT.register(SlotsRendererHandler::onRenderGui);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(CyclingInputHandler.CYCLE_LEFT_KEY_MAPPING, CyclingInputHandler.CYCLE_RIGHT_KEY_MAPPING);
    }
}
