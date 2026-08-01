package team.teampotato.ruok.gui.aui.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.widget.controls.ButtonWidget;
import team.teampotato.ruok.gui.aui.widget.controls.ControlPart;

import java.util.List;

public class AuiButtonWidget extends ButtonWidget {
    public float scale = 1.0f;

    public AuiButtonWidget(int x, int y, int width, int height, Component message, PressAction onPress, TooltipData tooltip, TooltipHost tooltipHost, float scale) {
        super(x, y, width, height, message, onPress, tooltip, tooltipHost);
        this.scale = scale;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        this.getTooltipState().tick(hovered, this.getTooltipHost(), mouseX, mouseY);
        ControlPart.drawWidget(context, this, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getMessage(), hovered, this.scale);
    }

    public static AbstractWidget builder(int x, int y, int width, int height, Component text, List<Component> tests, ButtonWidget.PressAction pressAction, TooltipHost host, float scale) {
        TooltipData data = new TooltipData(text, tests);
        return new AuiButtonWidget(x, y, width, height, text, pressAction, data, host, scale);
    }

    public static AbstractWidget builder(int x, int y, int width, int height, Component text, TooltipData data, ButtonWidget.PressAction pressAction, TooltipHost host, float scale) {
        return new AuiButtonWidget(x, y, width, height, text, pressAction, data, host, scale);
    }
}
