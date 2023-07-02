package fuzs.puzzlesapi.impl.iteminteractions.client.handler;

import fuzs.puzzlesapi.impl.iteminteractions.ItemInteractions;
import fuzs.puzzlesapi.impl.iteminteractions.handler.EnderChestMenuHandler;
import fuzs.puzzlesapi.impl.iteminteractions.network.client.C2SEnderChestMenuMessage;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EnderChestMenuClientHandler {

    public static EventResult onEntityJoinLevel(Entity entity, ClientLevel level) {
        // client needs to notify server it has created its menu, otherwise server runs way too early and client isn't ready for syncing menu data
        if (!(entity instanceof Player player)) return EventResult.PASS;
        EnderChestMenuHandler.openEnderChestMenu(player).ifPresent(menu -> {
            ItemInteractions.NETWORK.sendToServer(new C2SEnderChestMenuMessage());
        });
        return EventResult.PASS;
    }
}
