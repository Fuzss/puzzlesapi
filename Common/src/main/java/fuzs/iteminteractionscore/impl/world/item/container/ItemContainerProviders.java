package fuzs.iteminteractionscore.impl.world.item.container;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.iteminteractionscore.api.container.v1.ItemContainerProviderSerializers;
import fuzs.iteminteractionscore.api.container.v1.provider.ItemContainerProvider;
import fuzs.iteminteractionscore.impl.ItemInteractionsCore;
import fuzs.iteminteractionscore.impl.network.S2CSyncItemContainerProvider;
import fuzs.puzzleslib.api.config.v3.json.JsonConfigFileUtil;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ItemContainerProviders extends SimpleJsonResourceReloadListener {
    public static final ItemContainerProviders INSTANCE = new ItemContainerProviders();
    public static final String ITEM_CONTAINER_PROVIDERS_KEY = "item_container_providers";

    private Map<ResourceLocation, JsonElement> rawProviders = ImmutableMap.of();
    private Map<Item, ItemContainerProvider> providers = ImmutableMap.of();

    private ItemContainerProviders() {
        super(JsonConfigFileUtil.GSON, ITEM_CONTAINER_PROVIDERS_KEY);
    }

    @Nullable
    public ItemContainerProvider get(ItemStack stack) {
        return stack.isEmpty() ? null : this.providers.get(stack.getItem());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.rawProviders = ImmutableMap.copyOf(object);
        this.buildProviders(object);
    }

    public void buildProviders(Map<ResourceLocation, JsonElement> object) {
        ImmutableMap.Builder<Item, ItemContainerProvider> builder = ImmutableMap.builder();
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation item = entry.getKey();
            try {
                JsonObject jsonObject = entry.getValue().getAsJsonObject();
                // modded items may not be present, but we register default providers for some
                if (!BuiltInRegistries.ITEM.containsKey(item)) continue;
                ItemContainerProvider provider = ItemContainerProviderSerializers.deserialize(jsonObject);
                builder.put(BuiltInRegistries.ITEM.get(item), new ForwardingItemContainerProvider(provider));
            } catch (Exception e) {
                ItemInteractionsCore.LOGGER.error("Couldn't parse item container provider {}", item, e);
            }
        }
        this.providers = builder.build();
    }

    public void sendProvidersToPlayer(ServerPlayer player, boolean joined) {
        if (ModLoaderEnvironment.INSTANCE.isServer()) {
            ItemInteractionsCore.NETWORK.sendTo(new S2CSyncItemContainerProvider(this.rawProviders), player);
        }
    }
}
