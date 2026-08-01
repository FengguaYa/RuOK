package team.teampotato.ruok.gui.aui.data;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;
import team.teampotato.ruok.gui.aui.widget.controls.OptionFactory;
import team.teampotato.ruok.gui.base.Base;
import team.teampotato.ruok.gui.base.BaseUtil;
import team.teampotato.ruok.gui.base.Group;
import team.teampotato.ruok.gui.base.RuOKGameOptions;
import team.teampotato.ruok.gui.base.options.OptionsManager;
import team.teampotato.ruok.gui.vanilla.mode.BlockBreakParticleType;
import team.teampotato.ruok.gui.vanilla.mode.QualityType;
import team.teampotato.ruok.gui.vanilla.mode.WeatherType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class OptionData {
    private static final RuOKGameOptions gameOptions = new RuOKGameOptions();

    public static List<AbstractWidget> getMain(TooltipHost host) {
        return build(Group.MAIN, host);
    }

    public static List<AbstractWidget> getOther(TooltipHost host) {
        return build(Group.OTHER, host);
    }

    public static List<AbstractWidget> getHUD(TooltipHost host) {
        return build(Group.HUD, host);
    }

    private static List<AbstractWidget> build(Group group, TooltipHost host) {
        List<AbstractWidget> draws = new ArrayList<>();
        OptionsManager.getRuOKOptions().forEach(opt -> {
            if (opt.group != group) {
                return;
            }
            if (BaseUtil.isBoolean(opt)) {
                draws.add(getDrawableOfBoolean(opt, host));
            } else if (BaseUtil.isInteger(opt)) {
                draws.add(getDrawableOfInteger(opt, host));
            } else if (BaseUtil.isEnum(opt)) {
                draws.add(getDrawableOfEnum(opt, host));
            }
        });
        return draws;
    }

    @SuppressWarnings("unchecked")
    private static AbstractWidget getDrawableOfBoolean(Base<RuOKGameOptions, ?> base, TooltipHost host) {
        Base<RuOKGameOptions, Boolean> bool = (Base<RuOKGameOptions, Boolean>) base;
        Component text = Component.translatable(bool.key + ".info");
        TooltipData data = new TooltipData(Component.translatable(base.key + ".tooltip"), new ArrayList<>());
        return OptionFactory.ofBoolean(text, 0, 0, 100, 20,
                () -> bool.function.apply(gameOptions),
                v -> bool.biConsumer.accept(gameOptions, v),
                data, host);
    }

    @SuppressWarnings("unchecked")
    private static AbstractWidget getDrawableOfInteger(Base<RuOKGameOptions, ?> base, TooltipHost host) {
        Base<RuOKGameOptions, Integer> integer = (Base<RuOKGameOptions, Integer>) base;
        Component text = Component.translatable(integer.key + ".info");
        TooltipData data = new TooltipData(Component.translatable(base.key + ".tooltip"), new ArrayList<>());
        return OptionFactory.ofInteger(text, 0, 0, 100, 20,
                () -> integer.function.apply(gameOptions),
                v -> integer.biConsumer.accept(gameOptions, v),
                integer.min, integer.max, data, integer.format, host);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static AbstractWidget getDrawableOfEnum(Base<RuOKGameOptions, ?> base, TooltipHost host) {
        Base<RuOKGameOptions, Enum> enumBase = (Base<RuOKGameOptions, Enum>) base;
        Class<Enum> enumClass = (Class<Enum>) enumBase.getEnumClass();
        List<? extends Enum> values = Arrays.asList(enumClass.getEnumConstants());
        Component text = Component.translatable(base.key + ".info");
        TooltipData data = new TooltipData(Component.translatable(base.key + ".tooltip"), new ArrayList<>());
        return OptionFactory.ofEnum(text, 0, 0, 100, 20, enumClass,
                OptionData::enumName,
                () -> enumBase.function.apply(gameOptions),
                v -> enumBase.biConsumer.accept(gameOptions, v),
                data, host);
    }

    private static Component enumName(Enum<?> v) {
        if (v instanceof QualityType q) {
            return Component.translatable(q.getKey());
        }
        if (v instanceof WeatherType w) {
            return Component.translatable(w.getKey());
        }
        if (v instanceof BlockBreakParticleType b) {
            return Component.translatable(b.getKey());
        }
        return Component.literal(v.name());
    }
}
