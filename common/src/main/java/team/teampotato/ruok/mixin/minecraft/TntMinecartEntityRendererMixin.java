package team.teampotato.ruok.mixin.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartTntRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.teampotato.ruok.config.RuOK;


@Mixin(value = TntMinecartRenderer.class, priority = 1200)
public class TntMinecartEntityRendererMixin {

    @Inject(method = "submitMinecartContents(Lnet/minecraft/client/renderer/entity/state/MinecartTntRenderState;Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("HEAD"), cancellable = true)
    private void onTntExplosionsRender(MinecartTntRenderState renderState, BlockModelRenderState blockModelRenderState, PoseStack poseStack, SubmitNodeCollector collector, int i, CallbackInfo ci) {
        if (!RuOK.get().RenderTNTExplosions) ci.cancel();
    }

    @Inject(method = "submitWhiteSolidBlock(Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IZI)V", at = @At("HEAD"), cancellable = true)
    private static void onRenderFlashingBlock(BlockModelRenderState blockModelRenderState, PoseStack poseStack, SubmitNodeCollector collector, int i, boolean bl, int j, CallbackInfo ci) {
        if (!RuOK.get().RenderTNTExplosions) ci.cancel();
    }
}
