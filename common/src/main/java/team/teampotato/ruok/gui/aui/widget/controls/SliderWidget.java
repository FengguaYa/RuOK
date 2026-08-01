package team.teampotato.ruok.gui.aui.widget.controls;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.theme.Theme;
import team.teampotato.ruok.gui.aui.theme.ThemeManager;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.tooltip.TooltipStateUtil;

import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

public class SliderWidget extends AbstractWidget {
    private double value;
    private double targetValue;
    private final Consumer<Double> onValueChange;
    private final double min;
    private final double max;
    private boolean dragging = false;
    private double smoothValue = 0.0;
    private static final double SMOOTH_FACTOR = 0.25;
    private DoubleSupplier externalGetter;
    private static final int KNOB_WIDTH = 8;
    private static final int SLIDER_HEIGHT = 10;
    private static final long DOUBLE_CLICK_INTERVAL = 250L;
    private boolean editing = false;
    private long lastClickTime = 0L;
    private static final double TRACK_RATIO = 0.5;
    private static final int TRACK_PADDING_RIGHT = 4;
    private final Font textRenderer;
    private final TooltipStateUtil tooltipState = new TooltipStateUtil();
    @Nullable
    private EditBox inputField;
    private final TooltipData tooltip;
    public String format;
    private final TooltipHost tooltipHost;

    public SliderWidget(int x, int y, int width, int height, Component message, double initialValue,
                        double min, double max, Consumer<Double> onValueChange, TooltipData tooltip, TooltipHost tooltipHost) {
        this(x, y, width, height, message, "", initialValue, min, max, onValueChange, null, tooltip, tooltipHost);
    }

    public SliderWidget(int x, int y, int width, int height, Component message, String format, double initialValue,
                        double min, double max, Consumer<Double> onValueChange, @Nullable DoubleSupplier externalGetter,
                        TooltipData tooltip, TooltipHost tooltipHost) {
        super(x, y, width, height, message);
        this.textRenderer = Minecraft.getInstance().font;
        this.tooltipState.setTooltipData(tooltip);
        this.format = format;
        this.min = min;
        this.max = max;
        this.targetValue = this.value = this.toRatio(initialValue);
        this.onValueChange = onValueChange;
        this.externalGetter = externalGetter;
        this.tooltip = tooltip;
        this.tooltipHost = tooltipHost;
    }

    private double toRatio(double realValue) {
        return Mth.clamp((realValue - this.min) / (this.max - this.min), 0.0, 1.0);
    }

    private double toReal(double ratio) {
        return this.min + (this.max - this.min) * ratio;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (this.editing && this.inputField != null) {
            if (keyEvent.key() == 257) {
                this.applyInputValue();
                this.exitEditMode();
                return true;
            }
            if (keyEvent.key() == 256) {
                this.exitEditMode();
                return true;
            }
            return this.inputField.keyPressed(keyEvent);
        }
        return super.keyPressed(keyEvent);
    }

    @Override
    public boolean charTyped(CharacterEvent characterEvent) {
        if (this.editing && this.inputField != null) {
            return this.inputField.charTyped(characterEvent);
        }
        return false;
    }

    private void applyInputValue() {
        try {
            double v = 0.0;
            if (this.inputField != null) {
                v = Double.parseDouble(this.inputField.getValue());
            }
            v = Mth.clamp(v, this.min, this.max);
            this.targetValue = this.value = this.toRatio(v);
            this.onValueChange.accept(v);
        } catch (NumberFormatException ignored) {
        }
    }

    private void exitEditMode() {
        this.editing = false;
        this.inputField = null;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
        if (!this.active || !this.visible) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (this.editing) {
            if (now - this.lastClickTime < DOUBLE_CLICK_INTERVAL) {
                this.exitEditMode();
            } else {
                this.applyInputValue();
                this.exitEditMode();
            }
            return true;
        }
        if (event.button() == 0 && this.isMouseOver(event.x(), event.y())) {
            if (now - this.lastClickTime < DOUBLE_CLICK_INTERVAL) {
                this.editing = true;
                this.createInputField();
                return true;
            }
            this.lastClickTime = now;
            this.dragging = true;
            this.setValueFromMouse(event.x());
            return true;
        }
        return false;
    }

    private void createInputField() {
        int trackX = this.getTrackX();
        int trackY = this.getTrackY();
        int trackWidth = this.getTrackWidth();
        this.inputField = new EditBox(this.textRenderer, trackX, trackY, trackWidth, 12, Component.empty());
        this.inputField.setMaxLength(16);
        this.inputField.setValue(String.valueOf((int) this.toReal(this.value)));
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (this.dragging && event.button() == 0) {
            this.dragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (this.dragging && event.button() == 0) {
            this.setValueFromMouse(event.x());
            return true;
        }
        return false;
    }

    private void setValueFromMouse(double mouseX) {
        int trackX = this.getTrackX();
        int trackWidth = this.getTrackWidth();
        if (trackWidth <= 0) {
            return;
        }
        double ratio = Mth.clamp((mouseX - trackX) / (double) trackWidth, 0.0, 1.0);
        this.value = ratio;
        this.targetValue = ratio;
        this.onValueChange.accept(this.toReal(ratio));
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (!this.visible) {
            return;
        }
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        this.tooltipState.tick(hovered, this.tooltipHost, mouseX, mouseY);
        if (this.editing) {
            this.renderEditMode(context);
            return;
        }
        this.renderSliderMode(context, mouseX, mouseY, hovered);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    private void renderSliderMode(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered) {
        if (this.externalGetter != null && !this.dragging) {
            double externalValue = this.externalGetter.getAsDouble();
            this.targetValue = this.value = this.toRatio(externalValue);
        }
        Theme theme = ThemeManager.get();
        ControlPart.renderWidgetLineAsBg(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), hovered);
        this.smoothValue += (this.targetValue - this.smoothValue) * SMOOTH_FACTOR;
        this.smoothValue = Mth.clamp(this.smoothValue, 0.0, 1.0);
        Component labelText = this.getMessage();
        double realValue = this.toReal(this.value);
        Component format = this.format.isEmpty() ? Component.literal(String.valueOf((int) realValue)) : Component.translatable(this.format, (int) realValue);
        int textY = this.getY() + (this.getHeight() - 8) / 2;
        int trackX = this.getTrackX();
        int trackY = this.getTrackY();
        int trackWidth = this.getTrackWidth();
        this.renderProgress(context, trackX - 3, trackY - 1, trackX + trackWidth + 3, trackY + SLIDER_HEIGHT + 1);
        int knobWidth = 9;
        int knobRadius = knobWidth / 2;
        int knobCenterX = (int) Mth.lerp(this.smoothValue, trackX + knobRadius, trackX + trackWidth - knobRadius);
        int knobRenderX = knobCenterX - knobRadius;
        int knobY = trackY + 1;
        ControlPart.renderButtonPart(context, knobRenderX, knobY, knobWidth);
        Component text = labelText.copy().append(Component.literal(" - ")).append(format);
        int textX = this.getX() + 4;
        int progressStartX = trackX - 3;
        int textWidth = progressStartX - textX - 4;
        if (textWidth > 0) {
            ControlPart.drawScrollingText(context, this.textRenderer, text, textX, textY, textWidth, theme.textColor);
        }
    }

    private void renderProgress(GuiGraphicsExtractor context, int x, int y, int x2, int y2) {
        ControlPart.drawRectBorder(context, x, y, x2, y2, -14803426);
        int zX = x + 1;
        int zY = y + 1;
        int zX2 = x2;
        int zY2 = y2;
        context.fill(zX, zY, zX2, zY2, -13882322);
        int lX = zX + 6 + 3;
        int lY = zY + 3;
        int lX2 = zX2 - 6 - 1;
        int lY2 = zY2 - 3 - 1;
        context.horizontalLine(lX, lX2, lY, -12698050);
        context.horizontalLine(lX, lX2, lY2, -12698050);
        int pY = lY + 1;
        context.fill(lX, pY, lX2 + 1, lY2, -15000803);
    }

    private void renderEditMode(GuiGraphicsExtractor context) {
        Theme theme = ThemeManager.get();
        ControlPart.renderWidgetLineAsBg(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), true);
        int textY = this.getY() + (this.getHeight() - 8) / 2;
        context.text(this.textRenderer, this.getMessage().getString(), this.getX() + 2, textY, theme.textColor, false);
        if (this.inputField != null) {
            this.inputField.extractRenderState(context, 0, 0, 0.0F);
        }
    }

    private int getTrackX() {
        return this.getX() + this.getWidth() - this.getTrackWidth() - TRACK_PADDING_RIGHT;
    }

    private int getTrackY() {
        return this.getY() + (this.getHeight() - SLIDER_HEIGHT) / 2;
    }

    private int getTrackWidth() {
        return (int) (this.getWidth() * TRACK_RATIO) - TRACK_PADDING_RIGHT;
    }

    @NotNull
    public static SliderWidget of(Component text, int x, int y, int width, int height, double initialValue,
                                  double min, double max, Consumer<Double> onValueChange,
                                  TooltipData tooltip, TooltipHost host) {
        return new SliderWidget(x, y, width, height, text, initialValue, min, max, onValueChange, tooltip, host);
    }
}
