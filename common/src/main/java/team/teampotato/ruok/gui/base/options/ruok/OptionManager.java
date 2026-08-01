package team.teampotato.ruok.gui.base.options.ruok;

import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.gui.base.Base;
import team.teampotato.ruok.gui.base.BaseBuilder;
import team.teampotato.ruok.gui.base.Group;
import team.teampotato.ruok.gui.base.RuOKGameOptions;
import team.teampotato.ruok.gui.base.content.DataContent;
import team.teampotato.ruok.gui.vanilla.mode.BlockBreakParticleType;
import team.teampotato.ruok.gui.vanilla.mode.QualityType;
import team.teampotato.ruok.gui.vanilla.mode.WeatherType;
import team.teampotato.ruok.util.Quality;
import team.teampotato.ruok.util.ToastUtil;
import team.teampotato.ruok.util.render.EntityRender;
import team.teampotato.ruok.vellamo.Score;

import java.util.List;

public class OptionManager {
    public static void register(List<Base<RuOKGameOptions, ?>> options) {
        if (!options.isEmpty()) {
            return;
        }

        // 基础设置
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.cull").group(Group.MAIN)
                .setter((str, bool) -> {
                    RuOK.get().onCull = bool;
                    RuOK.save();
                }).getter(get -> RuOK.get().onCull).build());
        options.add(BaseBuilder.create(Integer.class).key("ruok.quality.entity").group(Group.MAIN)
                .setter((str, integer) -> {
                    RuOK.get().MaxEntityEntities = integer;
                    RuOK.save();
                    EntityRender.reloadRenderEntity();
                }).getter(opts -> RuOK.get().MaxEntityEntities)
                .setContent(DataContent.ofInt(8, 1024, 8, "ruok.quality.entity.options")).build());
        options.add(BaseBuilder.create(Integer.class).key("ruok.quality.distance").group(Group.MAIN)
                .setter((str, integer) -> {
                    RuOK.get().EntitiesDistance = integer;
                    RuOK.save();
                }).getter(opts -> RuOK.get().EntitiesDistance)
                .setContent(DataContent.ofInt(4, 512, 1, "ruok.quality.block.options")).build());
        options.add(BaseBuilder.create(Integer.class).key("ruok.quality.paricle").group(Group.MAIN)
                .setter((str, integer) -> {
                    RuOK.get().MaxParticleDistance = integer;
                    RuOK.save();
                }).getter(opts -> RuOK.get().MaxParticleDistance)
                .setContent(DataContent.ofInt(1, 512, 1, "ruok.quality.block.options")).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.entity.render").group(Group.MAIN)
                .setter((opts, val) -> {
                    RuOK.get().EntityRender = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().EntityRender).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.runscore").group(Group.MAIN)
                .setter((str, bool) -> {
                    if (bool) {
                        Score.runVellamo();
                    }
                }).getter(get -> false).build());

        // 其他设置
        options.add(BaseBuilder.create(QualityType.class).key("ruok.quality.global").group(Group.OTHER)
                .setter((o, v) -> {
                    Quality.set(v);
                    RuOK.get().qualityModes = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().qualityModes).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.fastitem").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().FastItemRender = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().FastItemRender).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.displayitem").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().RenderDisplayItem = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().RenderDisplayItem).build());
        options.add(BaseBuilder.create(WeatherType.class).key("ruok.quality.weather").group(Group.OTHER)
                .setter((opts, value) -> {
                    RuOK.get().RenderWeather = value;
                    RuOK.save();
                    net.minecraft.client.Minecraft.getInstance().levelRenderer.allChanged();
                }).getter(o -> RuOK.get().RenderWeather).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.itemcount").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().isAlwaysShowItemCount = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().isAlwaysShowItemCount).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.chatfix").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().chatFix = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().chatFix).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.tntexplosions").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().RenderTNTExplosions = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().RenderTNTExplosions).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.mood").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().Mood = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().Mood).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.particle").group(Group.OTHER)
                .setter((opts, val) -> {
                    RuOK.get().Particle = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().Particle).build());
        options.add(BaseBuilder.create(BlockBreakParticleType.class).key("ruok.quality.particle").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().BlockBreakParticleMode = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().BlockBreakParticleMode).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.quality.fpsmonitor").group(Group.OTHER)
                .setter((o, v) -> {
                    RuOK.get().FPSMonitor = v;
                    RuOK.save();
                }).getter(o -> RuOK.get().FPSMonitor).build());

        // HUD 设置
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.on").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().onGui = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().onGui).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.rxtx").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiRXTX = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiRXTX).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.fps").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiFPS = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiFPS).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.cpu").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiCPU = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiCPU).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.gpu").group(Group.HUD)
                .setter((opts, value) -> {
                    if (team.teampotato.ruok.util.ModLoadState.isVulkanMod()) {
                        ToastUtil.send("ruok.options.warn.vulkan.title", "ruok.options.warn.vulkan.hud.info");
                        if (RuOK.get().GuiGPU) {
                            RuOK.get().GuiGPU = false;
                        }
                    } else {
                        RuOK.get().GuiGPU = value;
                        RuOK.save();
                    }
                }).getter(opts -> RuOK.get().GuiGPU).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.ram").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiRAM = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiRAM).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.entitycount").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiEntityCount = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiEntityCount).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.playerpos").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiPlayerPos = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiPlayerPos).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.server").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiServer = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiServer).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.cameratarget").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiCameraTarget = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiCameraTarget).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.ram.mode").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiEasyRamMode = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiEasyRamMode).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.ram.display").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().GuiDisplayRamUsage = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().GuiDisplayRamUsage).build());
        options.add(BaseBuilder.create(Boolean.class).key("ruok.options.gui.text.textBackground").group(Group.HUD)
                .setter((opts, val) -> {
                    RuOK.get().TextBackground = val;
                    RuOK.save();
                }).getter(opts -> RuOK.get().TextBackground).build());
    }
}
