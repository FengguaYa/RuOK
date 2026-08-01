package team.teampotato.ruok.gui.aui.tooltip;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import team.teampotato.ruok.gui.aui.widget.controls.ControlPart;

public final class TooltipBackground {
    private static final int PADDING = 3;

    public static void render(GuiGraphicsExtractor context, int contentX, int contentY, int contentWidth, int contentHeight, int headerHeight) {
        int bgLeft = contentX - PADDING;
        int bgTop = contentY - PADDING;
        int bgRight = contentX + contentWidth + PADDING;
        int bgBottom = contentY + contentHeight + headerHeight + PADDING;
        ControlPart.drawRectBorder(context, bgLeft, bgTop, bgRight, bgBottom, -14803425);
        ControlPart.drawRectBorderTColor(context, bgLeft + 1, bgTop + 1, bgRight - 1, bgBottom - 1, -9605778, -13421772);
        context.fill(bgLeft + 2, bgTop + 2, bgRight - 1, bgTop + 2 + headerHeight, ControlPart.withAlpha(-13734939, 80));
        context.fill(bgLeft + 2, bgTop + 2 + headerHeight, bgRight - 1, bgTop + 3 + headerHeight, ControlPart.withAlpha(-13210948, 80));
        context.fill(bgLeft + 2, bgTop + 3 + headerHeight, bgRight - 1, bgBottom - 1, ControlPart.withAlpha(-12631999, 80));
    }
}
