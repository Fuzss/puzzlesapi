package fuzs.iteminteractionscore.impl.client.handler;

import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.handler.EnderChestMenuHandler;
import fuzs.iteminteractionscore.impl.network.client.C2SEnderChestMenuMessage;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EnderChestMenuClientHandler {

    public static EventResult onEntityJoinLevel(Entity entity, ClientLevel level) {
        // client needs to notify server it has created its menu, otherwise server runs way too early and client isn't ready for syncing menu data
        if (!(entity instanceof Player player)) return EventResult.PASS;
        EnderChestMenuHandler.openEnderChestMenu(player).ifPresent(menu -> {
            ItemInteractionsCore.NETWORK.sendToServer(new C2SEnderChestMenuMessage());
        });
        return EventResult.PASS;
    }
}
