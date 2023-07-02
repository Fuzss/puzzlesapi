package fuzs.puzzlesapi.impl.iteminteractions.network.client;

import fuzs.puzzlesapi.impl.iteminteractions.capability.EnderChestMenuCapability;
import fuzs.puzzlesapi.impl.iteminteractions.init.ModRegistry;
import fuzs.puzzlesapi.impl.iteminteractions.network.S2CEnderChestSetSlotMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class C2SEnderChestSetSlotMessage extends S2CEnderChestSetSlotMessage {

    public C2SEnderChestSetSlotMessage() {

    }

    public C2SEnderChestSetSlotMessage(int stateId, int slot, ItemStack itemStack) {
        super(stateId, slot, itemStack);
    }

    @Override
    public MessageHandler<S2CEnderChestSetSlotMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(S2CEnderChestSetSlotMessage message, Player player, Object gameInstance) {
                if (((ServerPlayer) player).gameMode.isCreative()) {
                    ModRegistry.ENDER_CHEST_MENU_CAPABILITY.maybeGet(player).map(EnderChestMenuCapability::getEnderChestMenu).ifPresent(menu -> menu.setItem(message.slot, message.stateId, message.itemStack));
                }
            }
        };
    }
}
