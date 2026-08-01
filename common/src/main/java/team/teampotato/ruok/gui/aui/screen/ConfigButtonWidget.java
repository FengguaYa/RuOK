package team.teampotato.ruok.gui.aui.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.widget.controls.BooleanWidget;
import team.teampotato.ruok.gui.aui.widget.controls.ControlPart;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ConfigButtonWidget extends BooleanWidget {

    public ConfigButtonWidget(Component message, int x, int y, int width, int height, boolean initial,
                              Consumer<Boolean> setter, BooleanSupplier getter, TooltipData tooltip, TooltipHost tooltipHost) {
        super(x, y, width, height, message, initial, setter, getter, tooltip, tooltipHost);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (this.getter != null) {
            this.value = this.getter.getAsBoolean();
        }
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        this.tooltipState.tick(hovered, this.getTooltipHost(), mouseX, mouseY);
        ControlPart.drawWidget(context, this, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getMessage(), hovered, 1.0f);
        int endX = this.getX() + this.getWidth();
        int fillXStart = endX - 22;
        int fillXEnd = endX - 4;
        boolean buttonOn = this.value;
        int targetButtonX = !buttonOn ? fillXStart - 5 : fillXEnd - 5 - 5 - 3;
        if (this.buttonX == 0.0) {
            this.buttonX = targetButtonX;
        }
        this.buttonX = this.lerp(this.buttonX, targetButtonX, 0.25);
        int bgWidth = fillXEnd - fillXStart + 10;
        int bgHeight = 10;
        int bgX = fillXStart - 5;
        int bgY = this.getY() + (this.getHeight() - bgHeight) / 2;
        this.renderButtonLinePart(context, bgX, bgY + 1, bgWidth - 5 - 3, bgHeight - 1, buttonOn);
        int knobY = this.getY() + (this.getHeight() - 10) / 2;
        ControlPart.renderButtonPart(context, (int) this.buttonX, knobY, 10);
    }

    public static ConfigButtonWidget builder(Component text, int x, int y, int width, int height,
                                             BooleanSupplier getter, Consumer<Boolean> setter,
                                             TooltipData tooltip, TooltipHost tooltipHost) {
        return new ConfigButtonWidget(text, x, y, width, height, getter.getAsBoolean(), setter, getter, tooltip, tooltipHost);
    }
}
