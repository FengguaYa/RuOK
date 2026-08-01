package team.teampotato.ruok.gui.aui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.tooltip.TooltipUtil;
import team.teampotato.ruok.gui.aui.widget.AuiButtonWidget;
import team.teampotato.ruok.gui.aui.widget.controls.ButtonWidget;

import java.util.List;

public class HudConfigScreen extends Screen implements TooltipHost {
    private final Minecraft client = Minecraft.getInstance();
    private final Screen parent;
    private TooltipData tooltip;
    private int tooltipX;
    private int tooltipY;

    public HudConfigScreen(Screen parent) {
        super(Component.translatable("ruok.options.gui.hud.pos"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int screenW = this.width;
        int screenH = this.height;
        Component text = Component.translatable("ruok.options.gui.ruok");
        int w = this.font.width(text) + 6;
        int h = 12;
        int x = this.clamp(RuOK.get().GuiX, 0, screenW - w);
        int y = this.clamp(RuOK.get().GuiY, 0, screenH - h);
        List<Component> pos = new java.util.ArrayList<>();
        pos.add(Component.translatable("ruok.options.hud.pos.x", x));
        pos.add(Component.translatable("ruok.options.hud.pos.y", y));
        TooltipData data = new TooltipData(text, pos);
        this.addRenderableWidget(new HudButtonWidget(x, y, w, h, text, data, this));
        this.initButtons();
    }

    private void initButtons() {
        int margin = 6;
        int btnW = 80;
        int btnH = 20;
        int btnY = this.height - btnH - margin;
        int backX = this.width - btnW - margin;
        int resetX = this.width - btnW * 2 - margin * 2;
        this.addRenderableWidget(AuiButtonWidget.builder(resetX, btnY, btnW, btnH,
                Component.translatable("ruok.options.gui.reset.pos"),
                List.of(Component.translatable("ruok.options.gui.reset.pos.tooltip")),
                btn -> {
                    RuOK.get().GuiX = 0;
                    RuOK.get().GuiY = 0;
                    RuOK.save();
                    this.rebuildWidgets();
                }, this, 1.1f));
        this.addRenderableWidget(AuiButtonWidget.builder(backX, btnY, btnW, btnH,
                Component.translatable("ruok.options.gui.back"),
                List.of(Component.translatable("ruok.options.gui.back.tooltip")),
                btn -> {
                    RuOK.save();
                    this.client.setScreen(this.parent);
                }, this, 1.1f));
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (this.client.level == null) {
            this.extractMenuBackground(context);
        }
        super.extractRenderState(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    public void showTooltip(TooltipData tooltip, int mouseX, int mouseY) {
        if (tooltip != null) {
            this.tooltip = tooltip;
            this.tooltipX = mouseX;
            this.tooltipY = mouseY;
        }
    }

    private void renderTooltip(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        if (this.tooltip != null) {
            TooltipUtil.drawTooltip(context, this.font, this.tooltip.text(), this.tooltip.textList(),
                    this.tooltipX, this.tooltipY, this.width, this.height);
            this.tooltip = null;
        }
    }

    public static class HudButtonWidget extends AbstractWidget {
        private final TooltipData tooltip;
        private final HudConfigScreen screen;
        private boolean dragging = false;
        private int dragOffsetX;
        private int dragOffsetY;

        public HudButtonWidget(int x, int y, int width, int height, Component message, TooltipData tooltip, HudConfigScreen screen) {
            super(x, y, width, height, message);
            this.tooltip = tooltip;
            this.screen = screen;
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
            if (event.button() == 0 && this.isMouseOver(event.x(), event.y())) {
                this.dragging = true;
                this.dragOffsetX = (int) event.x() - this.getX();
                this.dragOffsetY = (int) event.y() - this.getY();
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseReleased(MouseButtonEvent event) {
            if (event.button() == 0 && this.dragging) {
                this.dragging = false;
                RuOK.get().GuiX = this.getX();
                RuOK.get().GuiY = this.getY();
                RuOK.save();
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
            if (!this.dragging || event.button() != 0) {
                return false;
            }
            int newX = (int) event.x() - this.dragOffsetX;
            int newY = (int) event.y() - this.dragOffsetY;
            newX = Math.max(0, Math.min(newX, this.screen.width - this.getWidth()));
            newY = Math.max(0, Math.min(newY, this.screen.height - this.getHeight()));
            this.setX(newX);
            this.setY(newY);
            return true;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            boolean hovered = this.isMouseOver(mouseX, mouseY);
            if (hovered) {
                this.screen.showTooltip(this.tooltip, mouseX, mouseY);
            }
            team.teampotato.ruok.gui.aui.widget.controls.ControlPart.drawWidget(context,
                    this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getMessage(), hovered, 1.0f);
        }
    }
}
