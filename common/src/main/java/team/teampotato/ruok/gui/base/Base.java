package team.teampotato.ruok.gui.base;

import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Base<S, T> {
    public String key;
    public BiConsumer<S, T> biConsumer;
    public Function<S, T> function;
    public Group group;
    public int min;
    public int max;
    public int interVal;
    public String format;
    public Component[] texts;
    public Class<T> implClass;

    public Class<T> getImplClass() {
        return this.implClass;
    }

    @SuppressWarnings("unchecked")
    public <Z extends Enum<Z>> Class<Z> getEnumClass() {
        if (!Enum.class.isAssignableFrom(this.implClass)) {
            throw new IllegalStateException("Not an enum type");
        }
        return (Class<Z>) this.implClass;
    }
}
