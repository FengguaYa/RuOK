package team.teampotato.ruok.mixin.minecraft;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HugeExplosionSeedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.teampotato.ruok.config.RuOK;

@Mixin(HugeExplosionSeedParticle.Provider.class)
public class ExplosionEmitterParticleFactoryMixin {
    @Inject(method = "createParticle*", at = @At("HEAD"), cancellable = true)
    public void onCreateParticle(ParticleOptions particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, RandomSource randomSource, CallbackInfoReturnable<Particle> cir) {
        if (!RuOK.get().RenderTNTExplosions) {
            cir.cancel(); // 取消原方法的执行
            cir.setReturnValue(null);
        }
    }
}
