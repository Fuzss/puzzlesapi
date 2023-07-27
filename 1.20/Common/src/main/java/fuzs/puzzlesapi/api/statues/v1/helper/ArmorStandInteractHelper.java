package fuzs.puzzlesapi.api.statues.v1.helper;

import fuzs.puzzlesapi.api.statues.v1.world.entity.decoration.ArmorStandDataProvider;
import fuzs.puzzlesapi.api.statues.v1.world.inventory.ArmorStandMenu;
import fuzs.puzzlesapi.mixin.statues.accessor.ArmorStandAccessor;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.core.v1.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArmorStandInteractHelper {
    public static final String OPEN_SCREEN_TRANSLATION_KEY = Items.ARMOR_STAND.getDescriptionId() + ".description";

    public static Optional<InteractionResult> tryOpenArmorStatueMenu(Player player, Level level, InteractionHand interactionHand, ArmorStand entity, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (player.isShiftKeyDown() && itemInHand.isEmpty() && (!entity.isInvulnerable() || player.getAbilities().instabuild)) {
            openArmorStatueMenu(player, entity, menuType, dataProvider);
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        }
        return Optional.empty();
    }

    public static void openArmorStatueMenu(Player player, ArmorStand entity, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        CommonAbstractions.INSTANCE.openMenu(serverPlayer, new SimpleMenuProvider((containerId, inventory, player1) -> {
            return ArmorStandMenu.create(menuType, containerId, inventory, entity, dataProvider);
        }, entity.getDisplayName()), (serverPlayer1, friendlyByteBuf) -> {
            friendlyByteBuf.writeInt(entity.getId());
            friendlyByteBuf.writeBoolean(entity.isInvulnerable());
            friendlyByteBuf.writeInt(((ArmorStandAccessor) entity).puzzlesapi$getDisabledSlots());
        });
    }

    public static Component getArmorStandHoverText() {
        Component shiftComponent = Component.empty().append(Proxy.INSTANCE.getKeyMappingComponent("key.sneak")).withStyle(ChatFormatting.LIGHT_PURPLE);
        Component useComponent = Component.empty().append(Proxy.INSTANCE.getKeyMappingComponent("key.use")).withStyle(ChatFormatting.LIGHT_PURPLE);
        return Component.translatable(OPEN_SCREEN_TRANSLATION_KEY, shiftComponent, useComponent).withStyle(ChatFormatting.GRAY);
    }
}
