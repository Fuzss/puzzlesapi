package fuzs.iteminteractionscore.impl.network.client;

import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.world.inventory.ContainerSlotHelper;
import fuzs.puzzleslib.api.network.v2.MessageV2;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class C2SContainerClientInputMessage implements MessageV2<C2SContainerClientInputMessage> {
    private int currentSlot;
    private boolean extractSingleItem;

    public C2SContainerClientInputMessage() {

    }

    public C2SContainerClientInputMessage(int currentSlot, boolean extractSingleItem) {
        this.currentSlot = currentSlot;
        this.extractSingleItem = extractSingleItem;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeShort(this.currentSlot);
        buf.writeBoolean(this.extractSingleItem);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.currentSlot = buf.readShort();
        this.extractSingleItem = buf.readBoolean();
    }

    @Override
    public MessageHandler<C2SContainerClientInputMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SContainerClientInputMessage message, Player player, Object gameInstance) {
                AbstractContainerMenu containerMenu = player.containerMenu;
                if (!containerMenu.stillValid(player)) {
                    ItemInteractionsCore.LOGGER.debug("Player {} interacted with invalid menu {}", player, containerMenu);
                    return;
                }
                if (message.currentSlot >= -1) {
                    ContainerSlotHelper.setCurrentContainerSlot(player, message.currentSlot);
                } else {
                    ItemInteractionsCore.LOGGER.warn("{} tried to set an invalid current container item slot", player);
                }
                ContainerSlotHelper.extractSingleItem(player, message.extractSingleItem);
            }
        };
    }
}
