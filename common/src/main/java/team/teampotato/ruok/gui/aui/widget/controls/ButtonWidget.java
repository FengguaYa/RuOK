package team.teampotato.ruok.gui.aui.widget.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.tooltip.TooltipStateUtil;

import java.util.List;

public class ButtonWidget extends AbstractWidget {
    protected final PressAction onPress;
    @Nullable
    private final TooltipData tooltip;
    private final TooltipStateUtil tooltipState = new TooltipStateUtil();
    private final TooltipHost tooltipHost;

    public ButtonWidget(int x, int y, int width, int height, Component message, PressAction onPress, TooltipData tooltip, TooltipHost tooltipHost) {
        super(x, y, width, height, message);
        this.onPress = onPress;
        this.tooltip = tooltip;
        this.tooltipState.setTooltipData(tooltip);
        this.tooltipHost = tooltipHost;
    }

    @Nullable
    public TooltipData getTooltipData() {
        return this.tooltip;
    }

    public TooltipStateUtil getTooltipState() {
        return this.tooltipState;
    }

    protected TooltipHost getTooltipHost() {
        return this.tooltipHost;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
        if (this.active && this.visible && this.isMouseOver(event.x(), event.y())) {
            this.onPress.onPress(this);
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
        }
        return false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        this.tooltipState.tick(hovered, this.tooltipHost, mouseX, mouseY);
        ControlPart.renderWidgetLineAsBg(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), hovered);
        team.teampotato.ruok.gui.aui.draw.DrawUtil.drawWidgetText(context, this, this.getMessage(), this.getX() + 4, this.getY() + (this.getHeight() - 8) / 2, 0xFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @NotNull
    public static AbstractWidget builder(int x, int y, int width, int height, Component text, List<Component> tests, PressAction pressAction, TooltipHost host) {
        TooltipData data = new TooltipData(text, tests);
        return new ButtonWidget(x, y, width, height, text, pressAction, data, host);
    }

    @FunctionalInterface
    public interface PressAction {
        void onPress(AbstractWidget var1);
    }
}
