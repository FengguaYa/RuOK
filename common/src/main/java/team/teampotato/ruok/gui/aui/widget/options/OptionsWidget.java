package team.teampotato.ruok.gui.aui.widget.options;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.widget.controls.ControlPart;

import java.util.ArrayList;
import java.util.List;

public class OptionsWidget extends AbstractWidget {
    private final List<AbstractWidget> widgets = new ArrayList<>();
    private double scrollAmount = 0.0;
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final int itemHeight;
    private final TooltipHost tooltipHost;

    public OptionsWidget(int x1, int y1, int x2, int y2, List<AbstractWidget> widgets, TooltipHost tooltipHost) {
        super(x1, y1, x2 - x1, y2 - y1, Component.empty());
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.widgets.addAll(widgets);
        this.tooltipHost = tooltipHost;
        int h = widgets.isEmpty() ? 20 : widgets.get(0).getHeight();
        this.itemHeight = h == 0 ? 20 : h;
    }

    public List<AbstractWidget> getWidgets() {
        return this.widgets;
    }

    private int getContentHeight() {
        return this.widgets.size() * this.itemHeight;
    }

    private int getMaxScroll() {
        return Math.max(0, this.getContentHeight() - (this.y2 - this.y1));
    }

    private void positionChildren() {
        int scroll = (int) this.scrollAmount;
        for (int i = 0; i < this.widgets.size(); i++) {
            AbstractWidget widget = this.widgets.get(i);
            widget.setX(this.x1 + 2);
            widget.setWidth(this.x2 - this.x1 - 4);
            widget.setHeight(this.itemHeight);
            widget.setY(this.y1 + i * this.itemHeight - scroll);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scrollAmount = Mth.clamp(this.scrollAmount - verticalAmount * this.itemHeight / 2.0, 0.0, this.getMaxScroll());
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
        this.positionChildren();
        for (AbstractWidget widget : this.widgets) {
            if (widget.visible && widget.isMouseOver(event.x(), event.y())) {
                return widget.mouseClicked(event, bl);
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        this.positionChildren();
        for (AbstractWidget widget : this.widgets) {
            if (widget.visible && widget.isMouseOver(event.x(), event.y())) {
                return widget.mouseReleased(event);
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        this.positionChildren();
        for (AbstractWidget widget : this.widgets) {
            if (widget.visible && widget.isMouseOver(event.x(), event.y())) {
                return widget.mouseDragged(event, deltaX, deltaY);
            }
        }
        return false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        this.positionChildren();
        context.enableScissor(this.x1, this.y1, this.x2, this.y2);
        for (AbstractWidget widget : this.widgets) {
            if (widget.visible && widget.getY() + widget.getHeight() >= this.y1 && widget.getY() <= this.y2) {
                widget.extractRenderState(context, mouseX, mouseY, delta);
            }
        }
        context.disableScissor();
        this.renderScrollbar(context);
        this.tooltipHost.showTooltip(null, mouseX, mouseY);
    }

    private void renderScrollbar(GuiGraphicsExtractor context) {
        if (this.getContentHeight() <= this.y2 - this.y1) {
            return;
        }
        int scrollbarWidth = 6;
        int padding = 2;
        int barX2 = this.x2 - padding;
        int barX1 = barX2 - scrollbarWidth;
        int barY1 = this.y1 + padding;
        int barY2 = this.y2 - padding;
        int viewHeight = barY2 - barY1;
        int contentHeight = this.getContentHeight();
        int thumbHeight = (int) ((float) viewHeight * (float) viewHeight / (float) contentHeight);
        thumbHeight = Mth.clamp(thumbHeight, 32, viewHeight - 8);
        int scrollRange = viewHeight - thumbHeight;
        int maxScroll = this.getMaxScroll();
        int scrollOffset = maxScroll > 0 ? (int) (this.scrollAmount * (double) scrollRange / (double) maxScroll) : 0;
        int thumbY1 = barY1 + scrollOffset;
        int thumbY2 = thumbY1 + thumbHeight;
        ControlPart.drawRectBorder(context, barX1 - 1, barY1 - 1, barX2, barY2 + 1, -1599427926);
        context.fill(barX1, barY1 - 1, barX2, barY2, 0x22999999);
        context.fill(barX1 + 1, thumbY1, barX2 - 1, thumbY2, -7798904);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
