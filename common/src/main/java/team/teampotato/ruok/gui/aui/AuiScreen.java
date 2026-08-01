package team.teampotato.ruok.gui.aui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.gui.aui.data.OptionData;
import team.teampotato.ruok.gui.aui.data.DebugData;
import team.teampotato.ruok.gui.aui.draw.DrawUtil;
import team.teampotato.ruok.gui.aui.screen.EntitiesConfigScreen;
import team.teampotato.ruok.gui.aui.screen.HudConfigScreen;
import team.teampotato.ruok.gui.aui.screen.ParticleConfigScreen;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.tooltip.TooltipUtil;
import team.teampotato.ruok.gui.aui.widget.AuiButtonWidget;
import team.teampotato.ruok.gui.aui.widget.controls.ButtonWidget;
import team.teampotato.ruok.gui.aui.widget.controls.ControlPart;
import team.teampotato.ruok.gui.aui.widget.options.OptionsWidget;
import team.teampotato.ruok.gui.base.options.OptionsManager;
import team.teampotato.ruok.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AuiScreen extends Screen implements TooltipHost {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean DEBUG = false;
    private OptionsWidget optionsList;
    private List<AbstractWidget> currentOptions;
    private final Screen parent;
    private final List<AbstractWidget> leftButtons = new ArrayList<>();
    private TooltipData tooltip;
    private int tooltipX;
    private int tooltipY;

    public AuiScreen(Screen parent) {
        super(Component.translatable("ruok.options.gui.ruok"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.initOptions();
        this.initLeftButtons();
        this.initBottomButtons();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context);
        this.renderTitle(context);
        this.renderVersion(context);
        super.extractRenderState(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    public void renderVersion(GuiGraphicsExtractor context) {
        int padding = 8;
        int buttonHeight = 20;
        int buttonY = this.height - buttonHeight - padding;
        Component versionText = Component.translatable("ruok.options.gui.version", "1.7.4");
        int textHeight = 9;
        int textX = Math.round(this.width * 0.25f) + 3;
        int textY = buttonY + (buttonHeight - textHeight) / 2;
        context.text(this.font, versionText, textX, textY, 0xFFFFFF, false);
    }

    public void renderTitle(GuiGraphicsExtractor context) {
        DrawUtil.drawCenteredText(context, Component.translatable("ruok.options.gui.ruok"), 5, 5, 1.5f);
    }

    public void drawBackground(GuiGraphicsExtractor context) {
        if (mc.level == null) {
            this.extractMenuBackground(context);
        }
        int w = this.width;
        int h = this.height;
        float leftRatio = 0.25f;
        int leftWidth = Math.round(w * leftRatio);
        int leftColor = ControlPart.withAlpha(-13553100, 80);
        context.fill(0, 0, leftWidth, h, leftColor);
        int dividerColor = ControlPart.withAlpha(-14868183, 80);
        context.verticalLine(leftWidth, 0, h - 1, dividerColor);
        int bgColor = ControlPart.withAlpha(-11974325, 80);
        context.fill(leftWidth + 1, 0, w, h, bgColor);
    }

    private void initBottomButtons() {
        int w = 80;
        int h = 20;
        int padding = 10;
        int x = this.width - w - padding;
        int y = this.height - h - 5;
        TooltipData data = new TooltipData(Component.translatable("ruok.options.gui.back"),
                List.of(Component.translatable("ruok.options.gui.back.tooltip")));
        AbstractWidget widget = AuiButtonWidget.builder(x, y, w, h, Component.translatable("ruok.options.gui.back"),
                data, v -> {
                    RuOK.save();
                    OptionsManager.reloadAll();
                    mc.setScreen(this.parent);
                }, this, 1.1f);
        this.addRenderableWidget(widget);
    }

    public void initLeftButtons() {
        this.leftButtons.clear();
        int leftWidth = Math.round(this.width * 0.25f);
        int padding = 8;
        int buttonHeight = 20;
        int buttonWidth = leftWidth - padding * 2;
        int y = 25;
        this.leftButtons.add(AuiButtonWidget.builder(padding, y, buttonWidth, buttonHeight,
                Component.translatable("ruok.setting.main"), List.of(Component.translatable("ruok.setting.main.tooltip")),
                b -> this.switchOptions(OptionData.getMain(this)), this, 1.1f));
        this.leftButtons.add(AuiButtonWidget.builder(padding, y, buttonWidth, buttonHeight,
                Component.translatable("ruok.setting.other"), List.of(Component.translatable("ruok.setting.other.tooltip")),
                b -> this.switchOptions(OptionData.getOther(this)), this, 1.1f));
        this.leftButtons.add(AuiButtonWidget.builder(padding, y, buttonWidth, buttonHeight,
                Component.translatable("ruok.setting.hud"), List.of(Component.translatable("ruok.setting.hud.tooltip")),
                b -> this.switchOptions(OptionData.getHUD(this)), this, 1.1f));
        this.leftButtons.add(AuiButtonWidget.builder(padding, y, buttonWidth, buttonHeight,
                Component.translatable("ruok.options.gui.list.entity"), List.of(Component.translatable("ruok.options.gui.list.entity.tooltip")),
                b -> mc.setScreen(new EntitiesConfigScreen(this)), this, 1.1f));
        this.leftButtons.add(AuiButtonWidget.builder(padding, y, buttonWidth, buttonHeight,
                Component.translatable("ruok.options.gui.list.particle"), List.of(Component.translatable("ruok.options.gui.list.particle.tooltip")),
                b -> mc.setScreen(new ParticleConfigScreen(this)), this, 1.1f));
        this.leftButtons.add(AuiButtonWidget.builder(padding, y, buttonWidth, buttonHeight,
                Component.translatable("ruok.options.gui.hud.pos"), List.of(Component.translatable("ruok.options.gui.hud.pos.tooltip")),
                b -> mc.setScreen(new HudConfigScreen(this)), this, 1.1f));
        if (DEBUG) {
            this.leftButtons.add(AuiButtonWidget.builder(padding, y, buttonWidth, buttonHeight,
                    Component.translatable("ruok.setting.debug"), List.of(Component.translatable("ruok.setting.debug.tooltip")),
                    b -> this.switchOptions(DebugData.getWidgets(this)), this, 1.1f));
        }
        OptionsWidget left = new OptionsWidget(padding, y, padding + buttonWidth, this.height, this.leftButtons, this);
        this.addRenderableWidget(left);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == 68 && (keyEvent.modifiers() & 4) != 0) {
            this.onAltDPressed();
            return true;
        }
        return super.keyPressed(keyEvent);
    }

    private void onAltDPressed() {
        DEBUG = !DEBUG;
        ToastUtil.send(Component.translatable("ruok.setting.debug"),
                DEBUG ? Component.translatable("ruok.options.gui.enable") : Component.translatable("ruok.options.gui.disable"));
        this.rebuildWidgets();
    }

    private void switchOptions(List<AbstractWidget> options) {
        this.currentOptions = options;
        this.rebuildWidgets();
    }

    public void initOptions() {
        int w = this.width;
        int h = this.height;
        int leftWidth = Math.round(w * 0.25f);
        int padding = 8;
        int top = 10;
        int bottom = h - 30;
        int x1 = leftWidth + 1 + padding;
        int x2 = w - padding;
        int y1 = top;
        int y2 = bottom;
        List<AbstractWidget> options = this.currentOptions != null ? this.currentOptions : OptionData.getMain(this);
        this.optionsList = new OptionsWidget(x1, y1, x2, y2, options, this);
        this.addRenderableWidget(this.optionsList);
    }

    @Override
    public void showTooltip(TooltipData tooltip, int mouseX, int mouseY) {
        if (tooltip == null) {
            return;
        }
        this.tooltip = tooltip;
        this.tooltipX = mouseX;
        this.tooltipY = mouseY;
    }

    public void renderTooltip(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        if (this.tooltip != null) {
            TooltipUtil.drawTooltip(context, this.font, this.tooltip.text(), this.tooltip.textList(),
                    this.tooltipX, this.tooltipY, this.width, this.height);
            this.tooltip = null;
        }
    }
}
