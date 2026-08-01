package team.teampotato.ruok.gui.base.content;

import net.minecraft.network.chat.Component;

public sealed interface DataContent {
    static IntegerContent ofInt(int min, int max, int interval, String format) {
        return new IntegerContent(min, max, interval, format);
    }

    static EnumContent ofEnum(Component... texts) {
        return new EnumContent(texts);
    }

    record IntegerContent(int min, int max, int interval, String format) implements DataContent {
    }

    record EnumContent(Component[] texts) implements DataContent {
    }
}
