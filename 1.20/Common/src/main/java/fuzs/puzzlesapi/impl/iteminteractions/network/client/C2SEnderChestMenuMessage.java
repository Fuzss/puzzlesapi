package fuzs.puzzlesapi.impl.iteminteractions.network.client;

import fuzs.puzzlesapi.impl.iteminteractions.handler.EnderChestMenuHandler;
import fuzs.puzzlesapi.impl.iteminteractions.world.inventory.EnderChestSynchronizer;
import fuzs.puzzleslib.api.network.v2.MessageV2;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class C2SEnderChestMenuMessage implements MessageV2<C2SEnderChestMenuMessage> {

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void read(FriendlyByteBuf buf) {

    }

    @Override
    public MessageHandler<C2SEnderChestMenuMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SEnderChestMenuMessage message, Player player, Object gameInstance) {
                EnderChestMenuHandler.openEnderChestMenu(player).ifPresent(menu -> {
                    menu.setSynchronizer(new EnderChestSynchronizer((ServerPlayer) player));
                });
            }
        };
    }
}
