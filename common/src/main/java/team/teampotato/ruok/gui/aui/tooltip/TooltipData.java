package team.teampotato.ruok.gui.aui.tooltip;

import net.minecraft.network.chat.Component;

import java.util.List;

public record TooltipData(Component text, List<Component> textList) {

    public void append(Component component) {
        this.textList.add(component);
    }
}
