package team.teampotato.ruok.gui.aui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.tooltip.TooltipUtil;
import team.teampotato.ruok.gui.aui.widget.AuiButtonWidget;
import team.teampotato.ruok.gui.aui.widget.controls.ButtonWidget;
import team.teampotato.ruok.gui.aui.widget.options.OptionsWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfigListScreen extends Screen implements TooltipHost {
    protected final Screen parent;
    private final List<String> allIds = new ArrayList<>();
    private EditBox searchBox;
    private Mode mode = Mode.WHITELIST;
    private Filter filter = Filter.ALL;
    private OptionsWidget listWidget;
    private String searchText = "";
    private TooltipData tooltip;
    private int tooltipX;
    private int tooltipY;

    protected ConfigListScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        this.allIds.addAll(this.getAllIds());
    }

    protected abstract List<String> getAllIds();

    protected abstract Component getDisplayName(String id);

    protected abstract String getSearchLabelKey();

    protected abstract List<String> getList(Mode mode);

    protected abstract void saveList(Mode mode, List<String> list);

    protected abstract void reloadCaches();

    @Override
    protected void init() {
        super.init();
        int x = 10;
        this.searchBox = new EditBox(this.font, x, 10, 180, 16, Component.empty());
        this.searchBox.setMaxLength(64);
        this.addRenderableWidget(this.searchBox);

        int y = 34;
        int gap = 4;
        int toggleWidth = 14;
        int toggleGap = 6;
        int textPadding = 12;
        int minWidth = 60;
        int w = Math.max(minWidth, this.font.width(Component.translatable("ruok.options.gui.all")) + textPadding + toggleGap + toggleWidth);
        this.addRenderableWidget(this.createModeButton(Component.translatable("ruok.options.gui.white"), x, y, w, 18, Mode.WHITELIST));
        this.addRenderableWidget(this.createModeButton(Component.translatable("ruok.options.gui.black"), x + w + gap, y, w, 18, Mode.BLACKLIST));
        this.addRenderableWidget(this.createFilterButton(Component.translatable("ruok.options.gui.all"), x, y + 24, w, 18, Filter.ALL));
        this.addRenderableWidget(this.createFilterButton(Component.translatable("ruok.options.gui.on"), x + w + gap, y + 24, w, 18, Filter.ENABLED));
        this.addRenderableWidget(this.createFilterButton(Component.translatable("ruok.options.gui.off"), x + (w + gap) * 2, y + 24, w, 18, Filter.DISABLED));

        this.initList();
        this.initBottomButtons();
    }

    private void initList() {
        List<AbstractWidget> widgets = new ArrayList<>();
        for (String id : this.allIds) {
            widgets.add(new EntryWidget(this.getDisplayName(id), id, this));
        }
        this.listWidget = new OptionsWidget(10, 80, this.width - 10, this.height - 42, widgets, this);
        this.addRenderableWidget(this.listWidget);
        this.refreshList();
    }

    protected void refreshList() {
        if (this.listWidget == null) {
            return;
        }
        this.listWidget.getWidgets().clear();
        for (String id : this.allIds) {
            boolean inList = this.getList(this.mode).contains(id);
            if (!this.searchText.isEmpty()
                    && !id.contains(this.searchText)
                    && !this.getDisplayName(id).getString().toLowerCase().contains(this.searchText)) {
                continue;
            }
            if (this.filter == Filter.ENABLED && !inList) {
                continue;
            }
            if (this.filter == Filter.DISABLED && inList) {
                continue;
            }
            this.listWidget.getWidgets().add(new EntryWidget(this.getDisplayName(id), id, this));
        }
    }

    private AbstractWidget createModeButton(Component text, int x, int y, int w, int h, Mode target) {
        return AuiButtonWidget.builder(x, y, w, h, text,
                List.of(Component.translatable("ruok.options.gui.click.switch")),
                b -> {
                    this.mode = target;
                    this.refreshList();
                }, this, 1.0f);
    }

    private AbstractWidget createFilterButton(Component text, int x, int y, int w, int h, Filter target) {
        return AuiButtonWidget.builder(x, y, w, h, text,
                List.of(Component.translatable("ruok.options.gui.list.filter")),
                b -> {
                    this.filter = target;
                    this.refreshList();
                }, this, 1.0f);
    }

    private void initBottomButtons() {
        int margin = 6;
        int btnW = 80;
        int btnH = 20;
        int btnY = this.height - btnH - margin;
        int backX = this.width - btnW - margin;
        TooltipData data = new TooltipData(Component.translatable("ruok.options.gui.back"),
                List.of(Component.translatable("ruok.options.gui.back.tooltip")));
        this.addRenderableWidget(AuiButtonWidget.builder(backX, btnY, btnW, btnH,
                Component.translatable("ruok.options.gui.back"), data,
                b -> {
                    this.saveNow();
                    Minecraft.getInstance().setScreen(this.parent);
                }, this, 1.1f));
    }

    public void saveNow() {
        RuOK.save();
        this.reloadCaches();
    }

    @Override
    public void tick() {
        String value = this.searchBox.getValue().toLowerCase();
        if (!value.equals(this.searchText)) {
            this.searchText = value;
            this.refreshList();
        }
        super.tick();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (Minecraft.getInstance().level == null) {
            this.extractMenuBackground(context);
        }
        int labelX = this.searchBox.getX() - 6 - this.font.width(Component.translatable(this.getSearchLabelKey()));
        int labelY = this.searchBox.getY() + (this.searchBox.getHeight() - 8) / 2;
        context.text(this.font, Component.translatable(this.getSearchLabelKey()), labelX, labelY, 0xE0E0E0, false);
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

    public class EntryWidget extends AbstractWidget {
        private final Component name;
        private final String id;
        private final ConfigListScreen screen;
        private final ConfigButtonWidget toggle;

        public EntryWidget(Component name, String id, ConfigListScreen screen) {
            super(0, 0, 100, 20, name);
            this.name = name;
            this.id = id;
            this.screen = screen;
            this.toggle = ConfigButtonWidget.builder(Component.translatable("ruok.options.gui.enable"),
                    0, 0, 64, 16,
                    () -> ConfigListScreen.this.getList(ConfigListScreen.this.mode).contains(this.id),
                    value -> {
                        List<String> list = new ArrayList<>(ConfigListScreen.this.getList(ConfigListScreen.this.mode));
                        if (value && !list.contains(this.id)) {
                            list.add(this.id);
                        } else if (!value) {
                            list.remove(this.id);
                        }
                        ConfigListScreen.this.saveList(ConfigListScreen.this.mode, list);
                        RuOK.save();
                        ConfigListScreen.this.reloadCaches();
                        ConfigListScreen.this.refreshList();
                    },
                    new TooltipData(Component.translatable("ruok.options.gui.click.switch"), List.of()),
                    ConfigListScreen.this);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
            if (this.toggle.isMouseOver(event.x(), event.y())) {
                return this.toggle.mouseClicked(event, bl);
            }
            return false;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            int textY = this.getY() + (this.getHeight() - 8) / 2;
            context.text(ConfigListScreen.this.font, this.name, this.getX() + 4, textY, 0xFFFFFF, false);
            this.toggle.setX(this.getX() + this.getWidth() - 70);
            this.toggle.setY(this.getY() + (this.getHeight() - 16) / 2);
            this.toggle.extractRenderState(context, mouseX, mouseY, delta);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }

    protected enum Mode {
        WHITELIST,
        BLACKLIST
    }

    private enum Filter {
        ALL,
        ENABLED,
        DISABLED
    }
}
