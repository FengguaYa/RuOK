package team.teampotato.ruok.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.gui.vanilla.mode.BlockBreakParticleType;
import team.teampotato.ruok.gui.vanilla.mode.QualityType;
import team.teampotato.ruok.gui.vanilla.mode.WeatherType;
import team.teampotato.ruok.util.Quality;
import team.teampotato.ruok.util.render.EntityRender;
import team.teampotato.ruok.util.render.ParticleRender;

import java.util.ArrayList;

/**
 * Cloth Config 风格的 RuOK 设置界面（非原版风 UI）。
 */
public class RuOKConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("ruok.options.gui.ruok"))
                .setSavingRunnable(RuOK::save);
        ConfigEntryBuilder e = builder.entryBuilder();

        // 基础设置
        ConfigCategory main = builder.getOrCreateCategory(Component.translatable("ruok.options.pages.ruok.main"));
        main.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.cull.info"), RuOK.get().onCull)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("ruok.quality.cull.tooltip"))
                .setSaveConsumer(v -> RuOK.get().onCull = v)
                .build());
        main.addEntry(e.startIntSlider(Component.translatable("ruok.quality.entity.info"), RuOK.get().MaxEntityEntities, 8, 1024)
                .setDefaultValue(128)
                .setTooltip(Component.translatable("ruok.quality.entity.tooltip"))
                .setSaveConsumer(v -> {
                    RuOK.get().MaxEntityEntities = v;
                    EntityRender.reloadRenderEntity();
                })
                .build());
        main.addEntry(e.startIntSlider(Component.translatable("ruok.quality.distance.info"), RuOK.get().EntitiesDistance, 4, 512)
                .setDefaultValue(64)
                .setTooltip(Component.translatable("ruok.quality.distance.tooltip"))
                .setSaveConsumer(v -> RuOK.get().EntitiesDistance = v)
                .build());
        main.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.entity.render.info"), RuOK.get().EntityRender)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("ruok.quality.entity.render.tooltip"))
                .setSaveConsumer(v -> RuOK.get().EntityRender = v)
                .build());
        main.addEntry(e.startEnumSelector(Component.translatable("ruok.quality.global.info"), QualityType.class, RuOK.get().qualityModes)
                .setDefaultValue(QualityType.NORMAL)
                .setEnumNameProvider(v -> Component.translatable(v.getKey()))
                .setTooltip(Component.translatable("ruok.quality.global.tooltip"))
                .setSaveConsumer(v -> Quality.set(v))
                .build());

        // 其他设置
        ConfigCategory other = builder.getOrCreateCategory(Component.translatable("ruok.options.pages.ruok.other"));
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.fastitem.info"), RuOK.get().FastItemRender)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.quality.fastitem.tooltip"))
                .setSaveConsumer(v -> RuOK.get().FastItemRender = v)
                .build());
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.displayitem.info"), RuOK.get().RenderDisplayItem)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.quality.displayitem.tooltip"))
                .setSaveConsumer(v -> RuOK.get().RenderDisplayItem = v)
                .build());
        other.addEntry(e.startEnumSelector(Component.translatable("ruok.quality.weather.info"), WeatherType.class, RuOK.get().RenderWeather)
                .setDefaultValue(WeatherType.NORMAL)
                .setEnumNameProvider(v -> Component.translatable(v.getKey()))
                .setTooltip(Component.translatable("ruok.quality.weather.tooltip"))
                .setSaveConsumer(v -> RuOK.get().RenderWeather = v)
                .build());
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.itemcount.info"), RuOK.get().isAlwaysShowItemCount)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("ruok.quality.itemcount.tooltip"))
                .setSaveConsumer(v -> RuOK.get().isAlwaysShowItemCount = v)
                .build());
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.chatfix.info"), RuOK.get().chatFix)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.quality.chatfix.tooltip"))
                .setSaveConsumer(v -> RuOK.get().chatFix = v)
                .build());
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.tntexplosions.info"), RuOK.get().RenderTNTExplosions)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("ruok.quality.tntexplosions.tooltip"))
                .setSaveConsumer(v -> RuOK.get().RenderTNTExplosions = v)
                .build());
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.mood.info"), RuOK.get().Mood)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.quality.mood.tooltip"))
                .setSaveConsumer(v -> RuOK.get().Mood = v)
                .build());
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.particle.info"), RuOK.get().Particle)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("ruok.options.gui.particle.tooltip"))
                .setSaveConsumer(v -> RuOK.get().Particle = v)
                .build());
        other.addEntry(e.startEnumSelector(Component.translatable("ruok.quality.particle.info"), BlockBreakParticleType.class, RuOK.get().BlockBreakParticleMode)
                .setDefaultValue(BlockBreakParticleType.HIGH)
                .setEnumNameProvider(v -> Component.translatable(v.getKey()))
                .setTooltip(Component.translatable("ruok.quality.particle.tooltip"))
                .setSaveConsumer(v -> RuOK.get().BlockBreakParticleMode = v)
                .build());
        other.addEntry(e.startIntSlider(Component.translatable("ruok.quality.paricle.info"), RuOK.get().MaxParticleDistance, 1, 512)
                .setDefaultValue(128)
                .setTooltip(Component.translatable("ruok.quality.paricle.tooltip"))
                .setSaveConsumer(v -> RuOK.get().MaxParticleDistance = v)
                .build());
        other.addEntry(e.startBooleanToggle(Component.translatable("ruok.quality.fpsmonitor.info"), RuOK.get().FPSMonitor)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("ruok.quality.fpsmonitor.tooltip"))
                .setSaveConsumer(v -> RuOK.get().FPSMonitor = v)
                .build());

        // HUD 设置
        ConfigCategory hud = builder.getOrCreateCategory(Component.translatable("ruok.options.pages.ruok.hud"));
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.on.info"), RuOK.get().onGui)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.on.tooltip"))
                .setSaveConsumer(v -> RuOK.get().onGui = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.rxtx.info"), RuOK.get().GuiRXTX)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.rxtx.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiRXTX = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.fps.info"), RuOK.get().GuiFPS)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.fps.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiFPS = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.cpu.info"), RuOK.get().GuiCPU)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.cpu.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiCPU = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.gpu.info"), RuOK.get().GuiGPU)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.gpu.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiGPU = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.ram.info"), RuOK.get().GuiRAM)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.ram.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiRAM = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.entitycount.info"), RuOK.get().GuiEntityCount)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.entitycount.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiEntityCount = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.playerpos.info"), RuOK.get().GuiPlayerPos)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.playerpos.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiPlayerPos = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.server.info"), RuOK.get().GuiServer)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.server.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiServer = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.cameratarget.info"), RuOK.get().GuiCameraTarget)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.cameratarget.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiCameraTarget = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.ram.mode.info"), RuOK.get().GuiEasyRamMode)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("ruok.options.gui.ram.mode.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiEasyRamMode = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.ram.display.info"), RuOK.get().GuiDisplayRamUsage)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.ram.display.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiDisplayRamUsage = v)
                .build());
        hud.addEntry(e.startBooleanToggle(Component.translatable("ruok.options.gui.text.textBackground.info"), RuOK.get().TextBackground)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("ruok.options.gui.text.textBackground.tooltip"))
                .setSaveConsumer(v -> RuOK.get().TextBackground = v)
                .build());
        hud.addEntry(e.startIntSlider(Component.translatable("ruok.config.hud.button"), RuOK.get().GuiX, 0, 2048)
                .setDefaultValue(0)
                .setTooltip(Component.translatable("ruok.config.hud.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiX = v)
                .build());
        hud.addEntry(e.startIntSlider(Component.translatable("ruok.config.hud.button"), RuOK.get().GuiY, 0, 2048)
                .setDefaultValue(0)
                .setTooltip(Component.translatable("ruok.config.hud.tooltip"))
                .setSaveConsumer(v -> RuOK.get().GuiY = v)
                .build());

        // 名单设置
        ConfigCategory list = builder.getOrCreateCategory(Component.translatable("ruok.setting.list"));
        list.addEntry(e.startStrList(Component.translatable("ruok.options.gui.white.entity"),
                        new ArrayList<>(RuOK.get().whiteListedEntities))
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Component.translatable("ruok.options.gui.white.entity"))
                .setSaveConsumer(v -> {
                    RuOK.get().whiteListedEntities = new ArrayList<>(v);
                    EntityRender.reloadList();
                })
                .build());
        list.addEntry(e.startStrList(Component.translatable("ruok.options.gui.black.entity"),
                        new ArrayList<>(RuOK.get().blackListedEntities))
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Component.translatable("ruok.options.gui.black.entity"))
                .setSaveConsumer(v -> {
                    RuOK.get().blackListedEntities = new ArrayList<>(v);
                    EntityRender.reloadList();
                })
                .build());
        list.addEntry(e.startStrList(Component.translatable("ruok.options.gui.white.particle"),
                        new ArrayList<>(RuOK.get().WhiteListedParticle))
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Component.translatable("ruok.options.gui.white.particle"))
                .setSaveConsumer(v -> {
                    RuOK.get().WhiteListedParticle = new ArrayList<>(v);
                    ParticleRender.reloadList();
                })
                .build());
        list.addEntry(e.startStrList(Component.translatable("ruok.options.gui.black.particle"),
                        new ArrayList<>(RuOK.get().BlackListedParticle))
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Component.translatable("ruok.options.gui.black.particle"))
                .setSaveConsumer(v -> {
                    RuOK.get().BlackListedParticle = new ArrayList<>(v);
                    ParticleRender.reloadList();
                })
                .build());

        return builder.build();
    }
}
