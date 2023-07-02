package fuzs.puzzlesapi.api.limitlesscontainers.v1;

import fuzs.puzzlesapi.impl.limitlesscontainers.LimitlessContainers;
import fuzs.puzzlesapi.impl.limitlesscontainers.network.ClientboundContainerSetContentMessage;
import fuzs.puzzlesapi.impl.limitlesscontainers.network.ClientboundContainerSetSlotMessage;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;

public class LimitlessContainerSynchronizer implements ContainerSynchronizer {
    private final ServerPlayer player;

    public LimitlessContainerSynchronizer(ServerPlayer player) {
        this.player = player;
    }

    public static void setSynchronizerFor(ServerPlayer player, int containerId) {
        if (player.containerMenu instanceof LimitlessContainerMenu menu && menu.containerId == containerId) {
            menu.setActualSynchronizer(new LimitlessContainerSynchronizer(player));
        }
    }

    @Override
    public void sendInitialData(AbstractContainerMenu container, NonNullList<ItemStack> items, ItemStack carriedItem, int[] is) {
        LimitlessContainers.NETWORK.sendTo(new ClientboundContainerSetContentMessage(container.containerId, container.incrementStateId(), items, carriedItem), this.player);

        for (int i = 0; i < is.length; ++i) {
            this.broadcastDataValue(container, i, is[i]);
        }
    }

    @Override
    public void sendSlotChange(AbstractContainerMenu container, int slot, ItemStack itemStack) {
        LimitlessContainers.NETWORK.sendTo(new ClientboundContainerSetSlotMessage(container.containerId, container.incrementStateId(), slot, itemStack), this.player);
    }

    @Override
    public void sendCarriedChange(AbstractContainerMenu containerMenu, ItemStack stack) {
        LimitlessContainers.NETWORK.sendTo(new ClientboundContainerSetSlotMessage(-1, containerMenu.incrementStateId(), -1, stack), this.player);
    }

    @Override
    public void sendDataChange(AbstractContainerMenu container, int id, int value) {
        this.broadcastDataValue(container, id, value);
    }

    private void broadcastDataValue(AbstractContainerMenu container, int id, int value) {
        this.player.connection.send(new ClientboundContainerSetDataPacket(container.containerId, id, value));
    }
}
