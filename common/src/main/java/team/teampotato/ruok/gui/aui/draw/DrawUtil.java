package team.teampotato.ruok.gui.aui.draw;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DrawUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void drawCenteredText(@NotNull GuiGraphicsExtractor context, Component text, int x, int y, float scale) {
        drawText(context, DrawUtil.mc.font, text, x, y, 0xFFFFFF);
    }

    public static void drawText(@NotNull GuiGraphicsExtractor context, @NotNull Font font, @NotNull Component text, int x, int y, int color) {
        drawText(context, font, text, x, y, color, false);
    }

    public static void drawText(@NotNull GuiGraphicsExtractor context, @NotNull Font font, @NotNull Component text, int x, int y, int color, boolean shadow) {
        List<FormattedCharSequence> lines = font.split(text, 10000);
        if (!lines.isEmpty()) {
            context.text(font, lines.get(0), x, y, color, shadow);
        }
    }

    public static int getListAllEntryHeight(List<? extends net.minecraft.client.gui.components.AbstractWidget> list, boolean add) {
        int height = 0;
        int widgetHeight = 0;
        for (net.minecraft.client.gui.components.AbstractWidget widget : list) {
            height += widget.getHeight();
            widgetHeight = widget.getHeight();
        }
        if (add) {
            height += widgetHeight;
        }
        return height;
    }

    public static int getListEntryHeight(List<? extends net.minecraft.client.gui.components.AbstractWidget> list) {
        if (list.isEmpty()) {
            return 20;
        }
        return list.get(0).getHeight();
    }

}
