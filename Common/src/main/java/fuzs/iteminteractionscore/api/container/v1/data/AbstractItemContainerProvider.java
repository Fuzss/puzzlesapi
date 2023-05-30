package fuzs.iteminteractionscore.api.container.v1.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import fuzs.iteminteractionscore.api.container.v1.ItemContainerProviderSerializers;
import fuzs.iteminteractionscore.api.container.v1.provider.ItemContainerProvider;
import fuzs.iteminteractionscore.impl.world.item.container.ItemContainerProviders;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.ItemLike;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractItemContainerProvider implements DataProvider {
    private final Map<ResourceLocation, ItemContainerProvider> providers = Maps.newHashMap();
    private final PackOutput packOutput;

    public AbstractItemContainerProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public final CompletableFuture<?> run(CachedOutput output) {
        this.registerBuiltInProviders();
        Path outputFolder = this.packOutput.getOutputFolder();
        ArrayList<CompletableFuture<?>> futures = Lists.newArrayList();
        for (Map.Entry<ResourceLocation, ItemContainerProvider> entry : this.providers.entrySet()) {
            JsonElement jsonElement = ItemContainerProviderSerializers.serialize(entry.getValue());
            ResourceLocation item = entry.getKey();
            Path path = outputFolder.resolve(PackType.SERVER_DATA.getDirectory()).resolve(item.getNamespace()).resolve(ItemContainerProviders.ITEM_CONTAINER_PROVIDERS_KEY).resolve(item.getPath() + ".json");
            futures.add(DataProvider.saveStable(output, jsonElement, path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    protected abstract void registerBuiltInProviders();

    public void add(ItemLike item, ItemContainerProvider provider) {
        this.add(BuiltInRegistries.ITEM.getKey(item.asItem()), provider);
    }

    public void add(ResourceLocation item, ItemContainerProvider provider) {
        this.providers.put(item, provider);
    }

    @Override
    public String getName() {
        return "Item Container Provider";
    }
}
