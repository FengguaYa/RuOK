package team.teampotato.ruok.gui.aui.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.util.render.EntityRender;

import java.util.ArrayList;
import java.util.List;

public class EntitiesConfigScreen extends ConfigListScreen {

    public EntitiesConfigScreen(Screen parent) {
        super(parent, Component.translatable("ruok.options.gui.title.entity"));
    }

    @Override
    protected List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        BuiltInRegistries.ENTITY_TYPE.keySet().forEach(key -> ids.add(key.toString()));
        return ids;
    }

    @Override
    protected Component getDisplayName(String id) {
        Identifier identifier = Identifier.tryParse(id);
        if (identifier == null) {
            return Component.literal(id);
        }
        return BuiltInRegistries.ENTITY_TYPE.getOptional(identifier)
                .map(type -> (Component) Component.translatable(type.getDescriptionId()))
                .orElse(Component.literal(id));
    }

    @Override
    protected String getSearchLabelKey() {
        return "ruok.options.gui.search.entity";
    }

    @Override
    protected List<String> getList(Mode mode) {
        return mode == Mode.WHITELIST ? RuOK.get().whiteListedEntities : RuOK.get().blackListedEntities;
    }

    @Override
    protected void saveList(Mode mode, List<String> list) {
        if (mode == Mode.WHITELIST) {
            RuOK.get().whiteListedEntities = list;
        } else {
            RuOK.get().blackListedEntities = list;
        }
    }

    @Override
    protected void reloadCaches() {
        EntityRender.reloadList();
    }
}
