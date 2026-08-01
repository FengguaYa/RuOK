package team.teampotato.ruok.gui.aui.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.util.render.ParticleRender;

import java.util.ArrayList;
import java.util.List;

public class ParticleConfigScreen extends ConfigListScreen {

    public ParticleConfigScreen(Screen parent) {
        super(parent, Component.translatable("ruok.options.gui.title.particle"));
    }

    @Override
    protected List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        BuiltInRegistries.PARTICLE_TYPE.keySet().forEach(key -> ids.add(key.toString()));
        return ids;
    }

    @Override
    protected Component getDisplayName(String id) {
        Identifier identifier = Identifier.tryParse(id);
        if (identifier == null) {
            return Component.literal(id);
        }
        return BuiltInRegistries.PARTICLE_TYPE.getOptional(identifier)
                .map(type -> (Component) Component.translatable(identifier.toLanguageKey()))
                .orElse(Component.literal(id));
    }

    @Override
    protected String getSearchLabelKey() {
        return "ruok.options.gui.search.particle";
    }

    @Override
    protected List<String> getList(Mode mode) {
        return mode == Mode.WHITELIST ? RuOK.get().WhiteListedParticle : RuOK.get().BlackListedParticle;
    }

    @Override
    protected void saveList(Mode mode, List<String> list) {
        if (mode == Mode.WHITELIST) {
            RuOK.get().WhiteListedParticle = list;
        } else {
            RuOK.get().BlackListedParticle = list;
        }
    }

    @Override
    protected void reloadCaches() {
        ParticleRender.reloadList();
    }
}
