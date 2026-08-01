package team.teampotato.ruok.gui.aui.widget.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.tooltip.TooltipStateUtil;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnumWidget<E extends Enum<E>> extends AbstractWidget {
    private static final int SEGMENT_GAP = 4;
    private final Supplier<E> getter;
    private final Consumer<E> setter;
    private final E[] values;
    private final Function<E, Component> nameProvider;
    private final Font textRenderer;
    private final TooltipData tooltip;
    private final TooltipStateUtil tooltipState = new TooltipStateUtil();
    private final TooltipHost tooltipHost;

    public EnumWidget(int x, int y, int width, int height, Component message, Class<E> enumClass,
                      Function<E, Component> nameProvider, Supplier<E> getter, Consumer<E> setter,
                      TooltipData tooltip, TooltipHost tooltipHost) {
        super(x, y, width, height, message);
        this.textRenderer = Minecraft.getInstance().font;
        this.values = enumClass.getEnumConstants();
        this.getter = getter;
        this.setter = setter;
        this.nameProvider = nameProvider;
        this.tooltip = tooltip;
        this.tooltipState.setTooltipData(tooltip);
        this.tooltipHost = tooltipHost;
    }

    public TooltipData getTooltipData() {
        return this.tooltip;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (event.button() != 0) {
            return false;
        }
        int titleWidth = this.textRenderer.width(this.getMessage());
        int padding = 6;
        int startX = this.getX() + titleWidth + padding * 2;
        int endX = this.getX() + this.getWidth() - 4;
        int y1 = this.getY();
        int y2 = y1 + this.getHeight();
        if (event.x() < startX || event.x() > endX || event.y() < y1 || event.y() > y2) {
            return false;
        }
        int segmentWidth = (endX - startX) / this.values.length;
        if (segmentWidth <= 0) {
            return false;
        }
        int index = Mth.clamp((int) ((event.x() - startX) / segmentWidth), 0, this.values.length - 1);
        this.setter.accept(this.values[index]);
        this.playDownSound(Minecraft.getInstance().getSoundManager());
        return true;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        this.tooltipState.tick(hovered, this.tooltipHost, mouseX, mouseY);
        context.text(this.textRenderer, this.getMessage(), this.getX() + 4, this.getY() + (this.getHeight() - 8) / 2, 0xFFFFFF, false);
        ControlPart.renderWidgetLineAsBg(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), hovered);
        this.renderEnumValue(context);
    }

    private void renderEnumValue(GuiGraphicsExtractor context) {
        int x = this.getX();
        int y = this.getY();
        int h = this.getHeight();
        int titleWidth = this.textRenderer.width(this.getMessage());
        int padding = 6;
        int valueStartX = x + titleWidth + padding * 2;
        int valueEndX = x + this.getWidth() - 4;
        int valueCount = this.values.length;
        if (valueCount == 0) {
            return;
        }
        int totalGap = SEGMENT_GAP * (valueCount - 1);
        int segmentWidth = (valueEndX - valueStartX - totalGap) / valueCount;
        Enum<?> current = this.getter.get();
        int cursorX = valueStartX;
        for (E e : this.values) {
            int x1 = cursorX;
            int x2 = x1 + segmentWidth;
            boolean selected = e == current;
            this.renderEnumPart(context, this.nameProvider.apply(e), x1, y + 1, x2, y + h - 3, selected);
            cursorX = x2 + SEGMENT_GAP;
        }
    }

    private void renderEnumPart(GuiGraphicsExtractor context, Component text, int x1, int y1, int x2, int y2, boolean selected) {
        int borderColor = -14869217;
        int innerBorder = selected ? -5197648 : -10855844;
        int fill = selected ? -11513776 : -13882322;
        int textColor = selected ? -1 : -2039584;
        ControlPart.drawRectBorder(context, x1, y1, x2, y2, borderColor);
        ControlPart.drawRectBorder(context, x1 + 1, y1 + 1, x2 - 1, y2 - 1, innerBorder);
        if (selected) {
            int cx = x1 + (x2 - x1) / 2;
            int yLine = y2 - 2;
            int halfWidth = 4;
            context.horizontalLine(cx - halfWidth, cx + halfWidth, yLine, -1);
            context.horizontalLine(cx - halfWidth, cx + halfWidth, yLine + 1, 0x55000000);
        }
        context.fill(x1 + 2, y1 + 2, x2 - 2, y2 - 2, fill);
        int textWidth = this.textRenderer.width(text);
        int textX = x1 + (x2 - x1 - textWidth) / 2;
        int textY = y1 + (y2 - y1 - 8) / 2;
        context.text(this.textRenderer, text, textX, textY, textColor, false);
    }
}
