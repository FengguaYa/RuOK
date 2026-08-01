package team.teampotato.ruok.gui.base.options;

import team.teampotato.ruok.gui.base.Base;
import team.teampotato.ruok.gui.base.RuOKGameOptions;
import team.teampotato.ruok.gui.base.options.ruok.OptionManager;

import java.util.ArrayList;
import java.util.List;

public class OptionsManager {
    private static final List<Base<RuOKGameOptions, ?>> RuOKOptions = new ArrayList<>();

    public static void init() {
        OptionManager.register(RuOKOptions);
    }

    public static List<Base<RuOKGameOptions, ?>> getRuOKOptions() {
        return RuOKOptions;
    }

    public static void reload() {
        RuOKOptions.clear();
        OptionsManager.init();
    }

    public static void reloadAll() {
        OptionsManager.reload();
    }

    static {
        OptionsManager.init();
    }
}
