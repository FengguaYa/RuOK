package team.teampotato.ruok.gui.aui.draw;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DrawUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void drawCenteredText(@NotNull GuiGraphicsExtractor context, Component text, int x, int y, float scale) {
        context.text(DrawUtil.mc.font, text, x, y, 0xFFFFFF, false);
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

    @SuppressWarnings("unused")
    public static int getEntityHeight(@NotNull Entity entity) {
        return (int) entity.getBbHeight();
    }
}
