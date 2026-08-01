package team.teampotato.ruok.gui.aui.widget.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.tooltip.TooltipStateUtil;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class BooleanWidget extends AbstractWidget {
    public boolean value;
    public final BooleanSupplier getter;
    public final Consumer<Boolean> setter;
    @Nullable
    public final TooltipData tooltip;
    public final TooltipStateUtil tooltipState = new TooltipStateUtil();
    public double buttonX;
    public static final double ANIMATION_SPEED = 0.25;
    public static final int SIZE = 10;
    private final TooltipHost tooltipHost;

    public BooleanWidget(int x, int y, int width, int height, Component message, boolean initial,
                         Consumer<Boolean> setter, @Nullable BooleanSupplier getter,
                         @Nullable TooltipData tooltip, TooltipHost tooltipHost) {
        super(x, y, width, height, message);
        this.value = initial;
        this.setter = setter;
        this.getter = getter;
        this.tooltip = tooltip;
        this.tooltipState.setTooltipData(tooltip);
        this.tooltipHost = tooltipHost;
    }

    protected TooltipHost getTooltipHost() {
        return this.tooltipHost;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
        if (this.active && this.visible && this.isMouseOver(event.x(), event.y())) {
            this.value = !this.value;
            this.setter.accept(this.value);
            this.buttonX = 0.0;
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
        }
        return false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (this.getter != null) {
            this.value = this.getter.getAsBoolean();
        }
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        this.tooltipState.tick(hovered, this.tooltipHost, mouseX, mouseY);
        ControlPart.renderWidgetLineAsBg(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), hovered);
        int endX = this.getX() + this.getWidth();
        int fillXStart = endX - 22;
        int fillXEnd = endX - 4;
        boolean buttonOn = this.value;
        int targetButtonX = !buttonOn ? fillXStart - 5 : fillXEnd - 5 - 5 - 3;
        if (this.buttonX == 0.0) {
            this.buttonX = targetButtonX;
        }
        this.buttonX = this.lerp(this.buttonX, targetButtonX, ANIMATION_SPEED);
        int bgWidth = fillXEnd - fillXStart + 10;
        int bgHeight = 10;
        int bgX = fillXStart - 5;
        int bgY = this.getY() + (this.getHeight() - bgHeight) / 2;
        this.renderButtonLinePart(context, bgX, bgY + 1, bgWidth - 5 - 3, bgHeight - 1, buttonOn);
        int knobY = this.getY() + (this.getHeight() - SIZE) / 2;
        ControlPart.renderButtonPart(context, (int) this.buttonX, knobY, SIZE);
        team.teampotato.ruok.gui.aui.draw.DrawUtil.drawText(context, Minecraft.getInstance().font, this.getMessage(), this.getX() + 4, this.getY() + (this.getHeight() - 8) / 2, 0xFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    protected void renderButtonLinePart(GuiGraphicsExtractor context, int x, int y, int width, int height, boolean buttonOn) {
        int x2 = x + width - 1;
        int y2 = y + height - 2;
        context.horizontalLine(x, x2, y, -14803426);
        context.horizontalLine(x, x2, y2, -14803426);
        context.verticalLine(x, y, y2, -14803426);
        context.verticalLine(x2, y, y2, -14803426);
        int ix1 = x + 1;
        int iy1 = y + 1;
        int ix2 = x2 - 1;
        int iy2 = y2 - 1;
        int innerColor = buttonOn ? -11493813 : -7039852;
        context.horizontalLine(ix1, ix2, iy1, innerColor);
        context.horizontalLine(ix1, ix2, iy2, innerColor);
        context.verticalLine(ix1, iy1, iy2, innerColor);
        context.verticalLine(ix2, iy1, iy2, innerColor);
        int fx1 = ix1 + 1;
        int fy1 = iy1 + 1;
        int fx2 = ix2;
        int fy2 = iy2;
        int fillColor = buttonOn ? -16742650 : -7829368;
        context.fill(fx1, fy1, fx2, fy2, fillColor);
    }

    protected double lerp(double from, double to, double progress) {
        return from + (to - from) * progress;
    }
}
