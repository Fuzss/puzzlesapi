package fuzs.puzzlesapi.impl.iteminteractions.world.inventory;

import fuzs.puzzlesapi.impl.iteminteractions.ItemInteractions;
import fuzs.puzzlesapi.impl.iteminteractions.network.S2CEnderChestSetContentMessage;
import fuzs.puzzlesapi.impl.iteminteractions.network.S2CEnderChestSetSlotMessage;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;

public class EnderChestSynchronizer implements ContainerSynchronizer {
    private final ServerPlayer player;

    public EnderChestSynchronizer(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void sendInitialData(AbstractContainerMenu abstractContainerMenu, NonNullList<ItemStack> nonNullList, ItemStack itemStack, int[] is) {
        ItemInteractions.NETWORK.sendTo(new S2CEnderChestSetContentMessage(abstractContainerMenu.incrementStateId(), nonNullList, itemStack), this.player);
    }

    @Override
    public void sendSlotChange(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
        ItemInteractions.NETWORK.sendTo(new S2CEnderChestSetSlotMessage(abstractContainerMenu.incrementStateId(), i, itemStack), this.player);
    }

    @Override
    public void sendCarriedChange(AbstractContainerMenu abstractContainerMenu, ItemStack itemStack) {

    }

    @Override
    public void sendDataChange(AbstractContainerMenu abstractContainerMenu, int i, int j) {

    }
}
