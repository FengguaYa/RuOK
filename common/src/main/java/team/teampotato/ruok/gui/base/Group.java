package team.teampotato.ruok.gui.base;

import net.minecraft.network.chat.Component;

public enum Group {
    MAIN("ruok.setting.normal"),
    OTHER("ruok.setting.other"),
    HUD("ruok.setting.hud"),
    AR("ruok.setting.ar"),
    ENTITY("ruok.setting.entity"),
    PARTICLE("ruok.setting.particle");

    private final String translationKey;

    Group(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component getText() {
        return Component.translatable(this.translationKey);
    }

    public String getTranslationKey() {
        return this.translationKey;
    }
}
