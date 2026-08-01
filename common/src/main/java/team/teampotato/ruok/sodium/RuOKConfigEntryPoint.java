package team.teampotato.ruok.sodium;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.BooleanOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.EnumOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.IntegerOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ModOptionsBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionGroupBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import team.teampotato.ruok.RuOKMod;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.gui.vanilla.mode.BlockBreakParticleType;
import team.teampotato.ruok.gui.vanilla.mode.QualityType;
import team.teampotato.ruok.gui.vanilla.mode.WeatherType;
import team.teampotato.ruok.util.Quality;
import team.teampotato.ruok.util.render.EntityRender;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 将 RuOK 的设置页注册进 Sodium 的视频设置界面（Sodium 0.9 的 ConfigEntryPoint API）。
 * 通过 fabric.mod.json 的 "sodium:config_api_user" 入口点 / neoforge.mods.toml 的
 * [modproperties] 键 "sodium:config_api_user" 被 Sodium 自动发现。
 */
public class RuOKConfigEntryPoint implements ConfigEntryPoint {

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        ModOptionsBuilder modOptions = builder.registerOwnModOptions();
        modOptions.addPage(buildMainPage(builder));
        modOptions.addPage(buildOtherPage(builder));
        modOptions.addPage(buildHudPage(builder));
    }

    private static OptionPageBuilder buildMainPage(ConfigBuilder builder) {
        OptionGroupBuilder group = builder.createOptionGroup().setName(Component.translatable("ruok.setting.normal"));
        group.addOption(boolOption(builder, "on_cull", "ruok.quality.cull.info", "ruok.quality.cull.tooltip",
                () -> RuOK.get().onCull, v -> {
                    RuOK.get().onCull = v;
                    RuOK.save();
                }));
        group.addOption(intOption(builder, "entity_count", "ruok.quality.entity.info", "ruok.quality.entity.tooltip",
                8, 1024, 8, () -> RuOK.get().MaxEntityEntities, v -> {
                    RuOK.get().MaxEntityEntities = v;
                    RuOK.save();
                    EntityRender.reloadRenderEntity();
                }));
        group.addOption(intOption(builder, "entity_distance", "ruok.quality.distance.info", "ruok.quality.distance.tooltip",
                4, 512, 1, () -> RuOK.get().EntitiesDistance, v -> {
                    RuOK.get().EntitiesDistance = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "entity_render", "ruok.quality.entity.render.info", "ruok.quality.entity.render.tooltip",
                () -> RuOK.get().EntityRender, v -> {
                    RuOK.get().EntityRender = v;
                    RuOK.save();
                }));
        group.addOption(enumOption(builder, "quality", "ruok.quality.global.info", "ruok.quality.global.tooltip",
                QualityType.class, EnumSet.allOf(QualityType.class),
                v -> Component.translatable(v.getKey()),
                () -> RuOK.get().qualityModes, v -> {
                    Quality.set(v);
                    RuOK.get().qualityModes = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "run_score", "ruok.quality.runscore.info", "ruok.quality.runscore.tooltip",
                () -> false, v -> {
                    if (v) team.teampotato.ruok.vellamo.Score.runVellamo();
                }));
        return builder.createOptionPage()
                .setName(Component.translatable("ruok.options.pages.ruok.main"))
                .addOptionGroup(group);
    }

    private static OptionPageBuilder buildOtherPage(ConfigBuilder builder) {
        OptionGroupBuilder group = builder.createOptionGroup().setName(Component.translatable("ruok.setting.other"));
        group.addOption(boolOption(builder, "fast_item", "ruok.quality.fastitem.info", "ruok.quality.fastitem.tooltip",
                () -> RuOK.get().FastItemRender, v -> {
                    RuOK.get().FastItemRender = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "display_item", "ruok.quality.displayitem.info", "ruok.quality.displayitem.tooltip",
                () -> RuOK.get().RenderDisplayItem, v -> {
                    RuOK.get().RenderDisplayItem = v;
                    RuOK.save();
                }));
        group.addOption(enumOption(builder, "weather", "ruok.quality.weather.info", "ruok.quality.weather.tooltip",
                WeatherType.class, EnumSet.allOf(WeatherType.class),
                v -> Component.translatable(v.getKey()),
                () -> RuOK.get().RenderWeather, v -> {
                    RuOK.get().RenderWeather = v;
                    RuOK.save();
                    net.minecraft.client.Minecraft.getInstance().levelRenderer.allChanged();
                }));
        group.addOption(boolOption(builder, "item_count", "ruok.quality.itemcount.info", "ruok.quality.itemcount.tooltip",
                () -> RuOK.get().isAlwaysShowItemCount, v -> {
                    RuOK.get().isAlwaysShowItemCount = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "chat_fix", "ruok.quality.chatfix.info", "ruok.quality.chatfix.tooltip",
                () -> RuOK.get().chatFix, v -> {
                    RuOK.get().chatFix = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "tnt_explosions", "ruok.quality.tntexplosions.info", "ruok.quality.tntexplosions.tooltip",
                () -> RuOK.get().RenderTNTExplosions, v -> {
                    RuOK.get().RenderTNTExplosions = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "mood", "ruok.quality.mood.info", "ruok.quality.mood.tooltip",
                () -> RuOK.get().Mood, v -> {
                    RuOK.get().Mood = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "particle", "ruok.options.gui.particle.info", "ruok.options.gui.particle.tooltip",
                () -> RuOK.get().Particle, v -> {
                    RuOK.get().Particle = v;
                    RuOK.save();
                }));
        group.addOption(enumOption(builder, "block_break_particle", "ruok.quality.particle.info", "ruok.quality.particle.tooltip",
                BlockBreakParticleType.class, EnumSet.allOf(BlockBreakParticleType.class),
                v -> Component.translatable(v.getKey()),
                () -> RuOK.get().BlockBreakParticleMode, v -> {
                    RuOK.get().BlockBreakParticleMode = v;
                    RuOK.save();
                }));
        group.addOption(intOption(builder, "particle_distance", "ruok.quality.paricle.info", "ruok.quality.paricle.tooltip",
                1, 512, 1, () -> RuOK.get().MaxParticleDistance, v -> {
                    RuOK.get().MaxParticleDistance = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "fps_monitor", "ruok.quality.fpsmonitor.info", "ruok.quality.fpsmonitor.tooltip",
                () -> RuOK.get().FPSMonitor, v -> {
                    RuOK.get().FPSMonitor = v;
                    RuOK.save();
                }));
        return builder.createOptionPage()
                .setName(Component.translatable("ruok.options.pages.ruok.other"))
                .addOptionGroup(group);
    }

    private static OptionPageBuilder buildHudPage(ConfigBuilder builder) {
        OptionGroupBuilder group = builder.createOptionGroup().setName(Component.translatable("ruok.options.pages.ruok.hud"));
        group.addOption(boolOption(builder, "hud_on", "ruok.options.gui.on.info", "ruok.options.gui.on.tooltip",
                () -> RuOK.get().onGui, v -> {
                    RuOK.get().onGui = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_rxtx", "ruok.options.gui.rxtx.info", "ruok.options.gui.rxtx.tooltip",
                () -> RuOK.get().GuiRXTX, v -> {
                    RuOK.get().GuiRXTX = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_fps", "ruok.options.gui.fps.info", "ruok.options.gui.fps.tooltip",
                () -> RuOK.get().GuiFPS, v -> {
                    RuOK.get().GuiFPS = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_cpu", "ruok.options.gui.cpu.info", "ruok.options.gui.cpu.tooltip",
                () -> RuOK.get().GuiCPU, v -> {
                    RuOK.get().GuiCPU = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_gpu", "ruok.options.gui.gpu.info", "ruok.options.gui.gpu.tooltip",
                () -> RuOK.get().GuiGPU, v -> {
                    RuOK.get().GuiGPU = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_ram", "ruok.options.gui.ram.info", "ruok.options.gui.ram.tooltip",
                () -> RuOK.get().GuiRAM, v -> {
                    RuOK.get().GuiRAM = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_entity_count", "ruok.options.gui.entitycount.info", "ruok.options.gui.entitycount.tooltip",
                () -> RuOK.get().GuiEntityCount, v -> {
                    RuOK.get().GuiEntityCount = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_player_pos", "ruok.options.gui.playerpos.info", "ruok.options.gui.playerpos.tooltip",
                () -> RuOK.get().GuiPlayerPos, v -> {
                    RuOK.get().GuiPlayerPos = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_server", "ruok.options.gui.server.info", "ruok.options.gui.server.tooltip",
                () -> RuOK.get().GuiServer, v -> {
                    RuOK.get().GuiServer = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_camera_target", "ruok.options.gui.cameratarget.info", "ruok.options.gui.cameratarget.tooltip",
                () -> RuOK.get().GuiCameraTarget, v -> {
                    RuOK.get().GuiCameraTarget = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_ram_mode", "ruok.options.gui.ram.mode.info", "ruok.options.gui.ram.mode.tooltip",
                () -> RuOK.get().GuiEasyRamMode, v -> {
                    RuOK.get().GuiEasyRamMode = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_ram_display", "ruok.options.gui.ram.display.info", "ruok.options.gui.ram.display.tooltip",
                () -> RuOK.get().GuiDisplayRamUsage, v -> {
                    RuOK.get().GuiDisplayRamUsage = v;
                    RuOK.save();
                }));
        group.addOption(boolOption(builder, "hud_text_background", "ruok.options.gui.text.textBackground.info", "ruok.options.gui.text.textBackground.tooltip",
                () -> RuOK.get().TextBackground, v -> {
                    RuOK.get().TextBackground = v;
                    RuOK.save();
                }));
        group.addOption(intOption(builder, "hud_x", "ruok.config.hud.button", "ruok.config.hud.tooltip",
                0, 2048, 1, () -> RuOK.get().GuiX, v -> {
                    RuOK.get().GuiX = v;
                    RuOK.save();
                }));
        group.addOption(intOption(builder, "hud_y", "ruok.config.hud.button", "ruok.config.hud.tooltip",
                0, 2048, 1, () -> RuOK.get().GuiY, v -> {
                    RuOK.get().GuiY = v;
                    RuOK.save();
                }));
        return builder.createOptionPage()
                .setName(Component.translatable("ruok.options.pages.ruok.hud"))
                .addOptionGroup(group);
    }

    private static BooleanOptionBuilder boolOption(ConfigBuilder builder, String id, String nameKey, String tooltipKey,
                                                   Supplier<Boolean> getter, Consumer<Boolean> setter) {
        return builder.createBooleanOption(id(id))
                .setName(Component.translatable(nameKey))
                .setTooltip(Component.translatable(tooltipKey))
                .setBinding(setter, getter)
                .setImpact(OptionImpact.LOW);
    }

    private static IntegerOptionBuilder intOption(ConfigBuilder builder, String id, String nameKey, String tooltipKey,
                                                  int min, int max, int step,
                                                  Supplier<Integer> getter, Consumer<Integer> setter) {
        return builder.createIntegerOption(id(id))
                .setName(Component.translatable(nameKey))
                .setTooltip(Component.translatable(tooltipKey))
                .setRange(min, max, step)
                .setBinding(setter, getter)
                .setImpact(OptionImpact.LOW);
    }

    private static <E extends Enum<E>> EnumOptionBuilder<E> enumOption(ConfigBuilder builder, String id, String nameKey,
                                                                       String tooltipKey, Class<E> clazz, Set<E> values,
                                                                       Function<E, Component> nameProvider,
                                                                       Supplier<E> getter, Consumer<E> setter) {
        return builder.<E>createEnumOption(id(id), clazz)
                .setName(Component.translatable(nameKey))
                .setTooltip(Component.translatable(tooltipKey))
                .setAllowedValues(values)
                .setElementNameProvider(nameProvider)
                .setBinding(setter, getter)
                .setImpact(OptionImpact.HIGH);
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(RuOKMod.MOD_ID, path);
    }
}
