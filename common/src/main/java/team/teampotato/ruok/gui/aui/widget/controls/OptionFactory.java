package team.teampotato.ruok.gui.aui.widget.controls;

import net.minecraft.network.chat.Component;
import team.teampotato.ruok.gui.aui.TooltipHost;
import team.teampotato.ruok.gui.aui.tooltip.TooltipData;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class OptionFactory {

    public static SliderWidget ofDouble(Component text, int x, int y, int width, int height,
                                        DoubleSupplier getter, DoubleConsumer setter, double min, double max,
                                        TooltipData tooltip, TooltipHost host) {
        return new SliderWidget(x, y, width, height, text, getter.getAsDouble(), min, max, setter::accept, tooltip, host);
    }

    public static SliderWidget ofInteger(Component text, int x, int y, int width, int height,
                                         IntSupplier getter, IntConsumer setter, int min, int max,
                                         TooltipData tooltip, String format, TooltipHost host) {
        return new SliderWidget(x, y, width, height, text, format, getter.getAsInt(), min, max,
                value -> setter.accept((int) Math.round(value)), getter::getAsInt, tooltip, host);
    }

    public static BooleanWidget ofBoolean(Component text, int x, int y, int width, int height,
                                          BooleanSupplier getter, Consumer<Boolean> setter,
                                          TooltipData tooltip, TooltipHost host) {
        return new BooleanWidget(x, y, width, height, text, getter.getAsBoolean(), setter, getter, tooltip, host);
    }

    public static <E extends Enum<E>> EnumWidget<E> ofEnum(Component text, int x, int y, int width, int height,
                                                           Class<E> enumClass, Function<E, Component> nameProvider,
                                                           Supplier<E> getter, Consumer<E> setter,
                                                           TooltipData tooltip, TooltipHost host) {
        return new EnumWidget<>(x, y, width, height, text, enumClass, nameProvider, getter, setter, tooltip, host);
    }
}
