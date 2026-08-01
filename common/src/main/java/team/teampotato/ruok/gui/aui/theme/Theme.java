package team.teampotato.ruok.gui.aui.theme;

public enum Theme {
    NORMAL(0x222222, 0x333333, 0x55FF55, 0xFFFFFF, 0, 0x444444),
    DARK(0x111111, 0x222222, 0xAA00FF, 0xDDDDDD, 0, 0x333333),
    LIGHT(0xDDDDDD, 0xEEEEEE, 0x77DD77, 0x000000, 0xAAAAAA, 0xCCCCCC),
    TEST(0x2A2A2A, 0x3A3A3A, 0x55FF55, 0xFFFFFF, 0, 0x444444);

    public final int background;
    public final int hoverBackground;
    public final int accent;
    public final int textColor;
    public final int border;
    public final int secondary;

    Theme(int background, int hoverBackground, int accent, int text, int border, int secondary) {
        this.background = background;
        this.hoverBackground = hoverBackground;
        this.accent = accent;
        this.textColor = text;
        this.border = border;
        this.secondary = secondary;
    }
}
