package fuzs.puzzlesapi.impl.iteminteractions.network;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import fuzs.puzzlesapi.impl.iteminteractions.client.helper.ItemDecorationHelper;
import fuzs.puzzlesapi.impl.iteminteractions.world.item.container.ItemContainerProviders;
import fuzs.puzzleslib.api.config.v3.json.JsonConfigFileUtil;
import fuzs.puzzleslib.api.network.v2.MessageV2;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class S2CSyncItemContainerProvider implements MessageV2<S2CSyncItemContainerProvider> {
    private Map<ResourceLocation, JsonElement> providers;

    public S2CSyncItemContainerProvider() {

    }

    public S2CSyncItemContainerProvider(Map<ResourceLocation, JsonElement> providers) {
        this.providers = providers;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(this.providers, FriendlyByteBuf::writeResourceLocation, (friendlyByteBuf, jsonElement) -> {
            friendlyByteBuf.writeUtf(JsonConfigFileUtil.GSON.toJson(jsonElement), 262144);
        });
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        Map<ResourceLocation, JsonElement> map = buf.readMap(FriendlyByteBuf::readResourceLocation, friendlyByteBuf -> {
            return JsonConfigFileUtil.GSON.fromJson(friendlyByteBuf.readUtf(262144), JsonElement.class);
        });
        this.providers = ImmutableMap.copyOf(map);
    }

    @Override
    public MessageHandler<S2CSyncItemContainerProvider> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(S2CSyncItemContainerProvider message, Player player, Object gameInstance) {
                ItemContainerProviders.INSTANCE.buildProviders(message.providers);
                ItemDecorationHelper.clearCache();
            }
        };
    }
}
