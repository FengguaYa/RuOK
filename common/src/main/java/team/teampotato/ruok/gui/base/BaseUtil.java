package team.teampotato.ruok.gui.base;

public class BaseUtil {

    public static boolean isBoolean(Base<?, ?> base) {
        return base != null && Boolean.class.equals(base.getImplClass());
    }

    public static boolean isInteger(Base<?, ?> base) {
        return base != null && Integer.class.equals(base.getImplClass());
    }

    public static boolean isEnum(Base<?, ?> base) {
        return base != null && base.getImplClass() != null && base.getImplClass().isEnum();
    }
}
