package team.teampotato.ruok.gui.aui;

import team.teampotato.ruok.gui.aui.tooltip.TooltipData;

public interface TooltipHost {
    void showTooltip(TooltipData tooltip, int mouseX, int mouseY);
}
