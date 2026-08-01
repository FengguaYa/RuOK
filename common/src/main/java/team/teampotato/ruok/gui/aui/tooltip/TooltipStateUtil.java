package team.teampotato.ruok.gui.aui.tooltip;

import org.jetbrains.annotations.Nullable;
import team.teampotato.ruok.gui.aui.TooltipHost;

import java.time.Duration;

public class TooltipStateUtil {
    @Nullable
    private TooltipData tooltipData;
    private Duration delay = Duration.ZERO;
    private long hoverStart = 0L;
    private boolean hovered = false;

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public void setTooltipData(@Nullable TooltipData tooltip) {
        this.tooltipData = tooltip;
    }

    @Nullable
    public TooltipData getTooltipData() {
        return this.tooltipData;
    }

    public void tick(boolean hovered, TooltipHost host, int mouseX, int mouseY) {
        if (this.tooltipData == null) {
            this.hovered = false;
            return;
        }
        if (hovered != this.hovered) {
            this.hovered = hovered;
            this.hoverStart = System.currentTimeMillis();
        }
        if (hovered && System.currentTimeMillis() - this.hoverStart >= this.delay.toMillis()) {
            host.showTooltip(this.tooltipData, mouseX, mouseY);
        }
    }
}
