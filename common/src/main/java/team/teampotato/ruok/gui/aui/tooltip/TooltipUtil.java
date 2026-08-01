package team.teampotato.ruok.gui.aui.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TooltipUtil {

    public static void drawTooltip(GuiGraphicsExtractor context, Font font, @Nullable Component title,
                                   List<Component> lines, int mouseX, int mouseY, int screenWidth, int screenHeight) {
        if (lines.isEmpty() && title == null) {
            return;
        }
        int maxTextWidth = 200;
        int fontHeight = font.lineHeight;
        List<FormattedCharSequence> titleLines = title == null ? List.of() : font.split(title, maxTextWidth);
        List<FormattedCharSequence> bodyLines = new ArrayList<>();
        for (Component t : lines) {
            bodyLines.addAll(font.split(t, maxTextWidth));
        }
        boolean hasHeader = !titleLines.isEmpty();
        int contentWidth = 0;
        for (FormattedCharSequence t : titleLines) {
            contentWidth = Math.max(contentWidth, font.width(t));
        }
        for (FormattedCharSequence t : bodyLines) {
            contentWidth = Math.max(contentWidth, font.width(t));
        }
        int headerHeight = hasHeader ? titleLines.size() * fontHeight + 4 : 0;
        int bodyHeight = bodyLines.size() * fontHeight;
        int totalWidth = contentWidth + 2;
        int totalHeight = headerHeight + bodyHeight + 2;
        int x = Math.max(0, Math.min(mouseX + 6, screenWidth - totalWidth - 4));
        int y = Math.max(0, Math.min(mouseY + 6, screenHeight - totalHeight - 4));
        TooltipBackground.render(context, x, y, contentWidth, bodyHeight, headerHeight);
        int textX = x + 1;
        int textY = y + 1;
        if (hasHeader) {
            for (FormattedCharSequence t : titleLines) {
                context.text(font, t, textX, textY, -1, false);
                textY += fontHeight;
            }
            textY += 4;
        }
        for (FormattedCharSequence t : bodyLines) {
            context.text(font, t, textX, textY, -2039584, false);
            textY += fontHeight;
        }
    }
}
