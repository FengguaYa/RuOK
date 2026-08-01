package team.teampotato.ruok.mixin.minecraft;

import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.teampotato.ruok.config.RuOK;


@Mixin(BiomeAmbientSoundsHandler.class)
public abstract class BiomeEffectSoundPlayerMixin {
    @Shadow
    private float moodiness;

    @Inject(method = "tick", at = @At("HEAD"))
    private void disableMoodSound(CallbackInfo ci) {
        if (!RuOK.get().Mood) {
            // 禁用氛围(MOOD)声音逻辑
            this.moodiness = 0.0F;
        }
    }
}
