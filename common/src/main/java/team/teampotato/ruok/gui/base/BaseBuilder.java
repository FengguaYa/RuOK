package team.teampotato.ruok.gui.base;

import org.jetbrains.annotations.NotNull;
import team.teampotato.ruok.gui.base.content.DataContent;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BaseBuilder<T, V> {
    private final Base<V, T> base = new Base<>();
    private final Class<T> typeClass;

    private BaseBuilder(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    public static <T> StepKey<T, RuOKGameOptions> create(Class<T> typeClass) {
        return new BuilderSteps<>(typeClass);
    }

    private static class BuilderSteps<T, V>
            implements StepKey<T, V>, StepGroup<T, V>, StepSetter<T, V>, StepGetter<T, V>, OptionalSteps<T, V> {
        private final BaseBuilder<T, V> builder;

        private BuilderSteps(Class<T> typeClass) {
            this.builder = new BaseBuilder<>(typeClass);
        }

        @Override
        public StepGroup<T, V> key(String key) {
            this.builder.base.key = key;
            return this;
        }

        @Override
        public StepSetter<T, V> group(Group group) {
            this.builder.base.group = group;
            return this;
        }

        @Override
        public StepGetter<T, V> setter(BiConsumer<V, T> setter) {
            this.builder.base.biConsumer = setter;
            return this;
        }

        @Override
        public OptionalSteps<T, V> getter(Function<V, T> getter) {
            this.builder.base.function = getter;
            return this;
        }

        @Override
        public OptionalSteps<T, V> setContent(@NotNull DataContent content) {
            if (content instanceof DataContent.IntegerContent ic) {
                this.builder.base.min = ic.min();
                this.builder.base.max = ic.max();
                this.builder.base.interVal = ic.interval();
                this.builder.base.format = ic.format();
            } else if (content instanceof DataContent.EnumContent ec) {
                this.builder.base.texts = ec.texts();
            } else {
                throw new IllegalArgumentException("Unsupported DataContent type: " + content.getClass());
            }
            return this;
        }

        @Override
        public Base<V, T> build() {
            this.builder.base.implClass = this.builder.typeClass;
            return this.builder.base;
        }
    }

    public interface OptionalSteps<T, V> {
        OptionalSteps<T, V> setContent(@NotNull DataContent content);

        Base<V, T> build();
    }

    public interface StepGetter<T, V> {
        OptionalSteps<T, V> getter(Function<V, T> getter);
    }

    public interface StepSetter<T, V> {
        StepGetter<T, V> setter(BiConsumer<V, T> setter);
    }

    public interface StepGroup<T, V> {
        StepSetter<T, V> group(Group group);
    }

    public interface StepKey<T, V> {
        StepGroup<T, V> key(String key);
    }
}
