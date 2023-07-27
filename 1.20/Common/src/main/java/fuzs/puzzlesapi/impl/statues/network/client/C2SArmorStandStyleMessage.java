package fuzs.puzzlesapi.impl.statues.network.client;

import fuzs.puzzlesapi.api.statues.v1.world.inventory.ArmorStandMenu;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.data.ArmorStandStyleOption;
import fuzs.puzzleslib.api.network.v2.MessageV2;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class C2SArmorStandStyleMessage implements MessageV2<C2SArmorStandStyleMessage> {
    private ResourceLocation styleOption;
    private boolean value;

    public C2SArmorStandStyleMessage() {

    }

    public C2SArmorStandStyleMessage(ArmorStandStyleOption styleOption, boolean value) {
        this.styleOption = styleOption.getId();
        this.value = value;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.styleOption);
        buf.writeBoolean(this.value);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.styleOption = buf.readResourceLocation();
        this.value = buf.readBoolean();
    }

    @Override
    public MessageHandler<C2SArmorStandStyleMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SArmorStandStyleMessage message, Player player, Object gameInstance) {
                if (player.containerMenu instanceof ArmorStandMenu menu && menu.stillValid(player)) {
                    ArmorStandStyleOption.get(message.styleOption).setOption(menu.getArmorStand(), message.value);
                }
            }
        };
    }
}
