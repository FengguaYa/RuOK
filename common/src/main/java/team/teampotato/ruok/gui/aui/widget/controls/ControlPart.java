package team.teampotato.ruok.gui.aui.widget.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ControlPart {
    private static final int BORDER_DARK = -14803426;
    private static final int BORDER_LIGHT = -1184277;
    private static final int FILL_COLOR = -3289651;

    public static void renderButtonPart(GuiGraphicsExtractor context, int x, int y, int size) {
        context.horizontalLine(x, x + size - 1, y, BORDER_DARK);
        context.horizontalLine(x, x + size - 1, y + size - 1, BORDER_DARK);
        context.verticalLine(x, y, y + size - 1, BORDER_DARK);
        context.verticalLine(x + size - 1, y, y + size - 1, BORDER_DARK);
        context.horizontalLine(x + 1, x + size - 2, y + 1, BORDER_LIGHT);
        context.horizontalLine(x + 1, x + size - 2, y + size - 2, BORDER_LIGHT);
        context.verticalLine(x + 1, y + 1, y + size - 2, BORDER_LIGHT);
        context.verticalLine(x + size - 2, y + 1, y + size - 2, BORDER_LIGHT);
        context.fill(x + 2, y + 2, x + size - 2, y + size - 2, FILL_COLOR);
    }

    public static void renderWidgetLineAsBg(GuiGraphicsExtractor context, int x, int y, int width, int height, boolean hovered) {
        int baseColor = -11974325;
        int bgColor = hovered ? withAlpha(lighten(baseColor, 0.05f), 80) : withAlpha(baseColor, 80);
        context.fill(x, y, x + width + 1, y + height, bgColor);
        int endX = x + width;
        int bottomY = y + height - 1;
        int topLineBase = -12040118;
        int bottomLineBase = -13290185;
        int topLineColor = hovered ? lighten(topLineBase, 0.05f) : topLineBase;
        int bottomLineColor = hovered ? lighten(bottomLineBase, 0.05f) : bottomLineBase;
        context.horizontalLine(x, endX, y, topLineColor);
        context.horizontalLine(x, endX, bottomY, bottomLineColor);
    }

    public static void drawRectBorder(GuiGraphicsExtractor context, int x1, int y1, int x2, int y2, int color) {
        context.horizontalLine(x1, x2, y1, color);
        context.horizontalLine(x1, x2, y2, color);
        context.verticalLine(x1, y1, y2, color);
        context.verticalLine(x2, y1, y2, color);
    }

    public static void drawRectBorderTColor(GuiGraphicsExtractor context, int x1, int y1, int x2, int y2, int colorTopLeft, int colorBottomRight) {
        context.horizontalLine(x1, x2, y1, colorTopLeft);
        context.verticalLine(x1, y1, y2, colorTopLeft);
        context.horizontalLine(x1, x2, y2, colorBottomRight);
        context.verticalLine(x2, y1, y2, colorBottomRight);
    }

    public static int lighten(int color, float percent) {
        int a = color >>> 24 & 0xFF;
        int r = color >>> 16 & 0xFF;
        int g = color >>> 8 & 0xFF;
        int b = color & 0xFF;
        r = (int) ((float) r + (float) (255 - r) * percent);
        g = (int) ((float) g + (float) (255 - g) * percent);
        b = (int) ((float) b + (float) (255 - b) * percent);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int withAlpha(int rgbColor, int alphaPercent) {
        int alpha = Mth.clamp(alphaPercent, 0, 100) * 255 / 100;
        return alpha << 24 | rgbColor & 0xFFFFFF;
    }

    public static int getWidgetColor(int color, boolean hovered) {
        return withAlpha(lighten(color, !hovered ? 0.05f : 0.0f), 80);
    }

    public static void drawWidget(GuiGraphicsExtractor context, int x, int y, int width, int height, Component text, boolean hovered, float scale) {
        int borderOuter = getWidgetColor(-14803425, hovered);
        int borderLight = getWidgetColor(-9605778, hovered);
        int borderDark = getWidgetColor(-13421772, hovered);
        int background = getWidgetColor(-12039862, hovered);
        drawRectBorder(context, x, y, x + width, y + height, borderOuter);
        drawRectBorderTColor(context, x + 1, y + 1, x + width - 1, y + height - 1, borderLight, borderDark);
        context.fill(x + 2, y + 2, x + width - 1, y + height - 1, background);
        drawScaledText(context, text, x + 4, y, height, scale);
    }

    private static void drawScaledText(GuiGraphicsExtractor context, Component text, int x, int y, int height, float scale) {
        Font tr = Minecraft.getInstance().font;
        context.pose().pushMatrix();
        float textHeight = 8.0f * scale;
        float textY = (float) y + ((float) height - textHeight) / 2.0f;
        context.pose().translate(x, textY);
        context.pose().scale(scale, scale);
        context.text(tr, text, 0, 0, -1, false);
        context.pose().popMatrix();
    }

    public static void drawWidget(GuiGraphicsExtractor context, int x, int y, int width, int height, Component text, boolean hovered) {
        drawWidget(context, x, y, width, height, text, hovered, 1.1f);
    }

    public static void drawScrollingText(GuiGraphicsExtractor context, Font tr, Component text, int x, int y, int width, int color) {
        int fullWidth = tr.width(text);
        context.pose().pushMatrix();
        context.enableScissor(x, y - 1, x + width, y + 9 + 1);
        if (fullWidth <= width) {
            context.text(tr, text, x, y, color, false);
        } else {
            long time = System.currentTimeMillis();
            float period = 4000.0f;
            float t = (float) (time % (long) period) / period;
            float maxScroll = fullWidth - width + 6;
            float offset = Mth.lerp(t, 0.0f, maxScroll);
            context.pose().translate(-offset, 0.0f);
            context.text(tr, text, x, y, color, false);
        }
        context.disableScissor();
        context.pose().popMatrix();
    }
}
