package team.teampotato.ruok.mixin.minecraft.gui;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.gui.vanilla.RuOKScreens;
import team.teampotato.ruok.gui.vanilla.screen.ListScreen;
import team.teampotato.ruok.util.ModLoadState;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;visitWidgets(Ljava/util/function/Consumer;)V"))
    private void onInit(CallbackInfo ci, @Local GridLayout.RowHelper adder) {
        Button button;
        if (ModLoadState.isSodium() && !RuOK.get().UseVanillaGui) {
            // 如果玩家加载了Sodium模块，且不希望使用VanillaGui，显示ListScreen
            button = Button.builder(
                    Component.translatable("ruok.options.entity.list"),
                    (b) -> this.minecraft.setScreen(new ListScreen(Component.translatable("ruok.setting.list"), this, this.minecraft.options))
            ).build();
        } else {
            // 默认或Sodium加载且希望使用RuOK界面，显示RuOKScreens
            button = Button.builder(
                    Component.translatable("ruok.options.gui.ruok"),
                    (b) -> this.minecraft.setScreen(new RuOKScreens(this, this.minecraft.options))
            ).build();
        }
        adder.addChild(button);
    }

}
