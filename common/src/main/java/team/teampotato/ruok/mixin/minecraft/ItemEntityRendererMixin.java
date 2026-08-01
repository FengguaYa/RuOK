package team.teampotato.ruok.mixin.minecraft;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.util.Render;

import java.util.Optional;


@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {

    @ModifyVariable(
            method = "submitMultipleFromCount(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ItemClusterRenderState;Lnet/minecraft/util/RandomSource;)V",
            at = @At("HEAD"),
            ordinal = 0
    )
    private static int onRenderItemCount(int count) {
        if (RuOK.get().FastItemRender) return 1;
        return count;
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
            at = @At("RETURN")
    )
    private void onExtractRenderState(ItemEntity itemEntity, ItemEntityRenderState renderState, float partialTick, CallbackInfo ci) {
        if (RuOK.get().FastItemRender) {
            renderState.bobOffset = 0.0F;
        }
        if (RuOK.get().RenderDisplayItem) {
            Optional<Pair<Component, String>> itemCountText = Render.getTotalCountForDisplay(itemEntity);
            itemCountText.ifPresent(pair -> renderState.nameTag = pair.getFirst());
        }
    }
}
