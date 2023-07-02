package fuzs.puzzlesapi.impl.limitlesscontainers.client;

import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class AdvancedItemRenderer {
    private static final DecimalFormat DECIMAL_FORMAT = Util.make(new DecimalFormat(), decimalFormat -> {
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    });
    private static final NavigableMap<Integer, Character> NUMBER_SUFFIXES = ImmutableSortedMap.<Integer, Character>naturalOrder().put(1_000, 'K').put(1_000_000, 'M').put(1_000_000_000, 'B').build();

    public static Optional<Component> getStackSizeComponent(ItemStack stack) {
        Map.Entry<Integer, Character> entry = NUMBER_SUFFIXES.floorEntry(stack.getCount());
        return entry == null ? Optional.empty() : Optional.of(Component.literal(DECIMAL_FORMAT.format(stack.getCount())).withStyle(ChatFormatting.GRAY));
    }

    /**
     * Pretty much copied from {@link GuiGraphics#renderItemDecorations(Font, ItemStack, int, int, String)}.
     */
    public static void renderItemDecorations(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int i, int j, @Nullable String string) {
        if (!itemStack.isEmpty()) {
            guiGraphics.pose().pushPose();
            if (itemStack.getCount() != 1 || string != null) {
                String string2 = shortenValue(getCountFromString(string).orElse(itemStack.getCount()));
                Style style = getStyleFromString(string);
                Component stackCount = Component.literal(string2).withStyle(style);
                guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
                float scale = Math.min(1.0F, 16.0F / font.width(stackCount));
                guiGraphics.pose().scale(scale, scale, 1.0F);
                int posX = (int) ((i + 17) / scale - font.width(stackCount));
                int posY = (int) ((j + font.lineHeight * 2) / scale - font.lineHeight);
                guiGraphics.drawString(font, string2, posX, posY, 16777215, true);
            }

            int m;
            int n;
            if (itemStack.isBarVisible()) {
                int k = itemStack.getBarWidth();
                int l = itemStack.getBarColor();
                m = i + 2;
                n = j + 13;
                guiGraphics.fill(RenderType.guiOverlay(), m, n, m + 13, n + 2, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), m, n, m + k, n + 1, l | -16777216);
            }

            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localPlayer = minecraft.player;
            float f = localPlayer == null ? 0.0F : localPlayer.getCooldowns().getCooldownPercent(itemStack.getItem(), minecraft.getFrameTime());
            if (f > 0.0F) {
                m = j + Mth.floor(16.0F * (1.0F - f));
                n = m + Mth.ceil(16.0F * f);
                guiGraphics.fill(RenderType.guiOverlay(), i, m, i + 16, n, Integer.MAX_VALUE);
            }

            guiGraphics.pose().popPose();
        }
    }

    private static String shortenValue(int value) {
        Map.Entry<Integer, Character> entry = NUMBER_SUFFIXES.floorEntry(value);
        return entry == null ? String.valueOf(value) : String.valueOf(value / entry.getKey()) + entry.getValue();
    }

    private static OptionalInt getCountFromString(@Nullable String text) {
        if (text != null) {
            try {
                text = ChatFormatting.stripFormatting(text);
                if (text != null) {
                    return OptionalInt.of(Integer.parseInt(text));
                }
            } catch (NumberFormatException ignored) {

            }
        }
        return OptionalInt.empty();
    }

    private static Style getStyleFromString(@Nullable String text) {
        Style style = Style.EMPTY;
        if (text != null) {
            char[] charArray = text.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c == ChatFormatting.PREFIX_CODE) {
                    if (++i >= charArray.length) {
                        break;
                    } else {
                        c = charArray[i];
                        ChatFormatting chatFormatting = ChatFormatting.getByCode(c);
                        if (chatFormatting == ChatFormatting.RESET) {
                            style = Style.EMPTY;
                        } else if (chatFormatting != null) {
                            style = style.applyLegacyFormat(chatFormatting);
                        }
                    }
                }
            }
        }
        return style;
    }
}
