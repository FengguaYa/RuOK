package team.teampotato.ruok.gui.aui.theme;

public class ThemeManager {
    private static Theme current = Theme.NORMAL;

    public static Theme get() {
        return current;
    }

    public static void set(Theme theme) {
        current = theme;
    }
}
