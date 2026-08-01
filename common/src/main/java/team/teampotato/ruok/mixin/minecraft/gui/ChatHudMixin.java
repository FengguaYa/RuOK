package team.teampotato.ruok.mixin.minecraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.teampotato.ruok.config.RuOK;
import team.teampotato.ruok.util.ChatFix;

@Mixin(ChatComponent.class)
public class ChatHudMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void offsetStart(GuiGraphicsExtractor context, Font font, int i, int j, int k, ChatComponent.DisplayMode displayMode, boolean bl, CallbackInfo ci) {
        if (RuOK.get().chatFix && this.minecraft != null) {
            int offset = ChatFix.getOffset(this.minecraft);
            if (offset != 0) context.pose().translate(0.0F, -offset);
        }
    }

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void offsetEnd(GuiGraphicsExtractor context, Font font, int i, int j, int k, ChatComponent.DisplayMode displayMode, boolean bl, CallbackInfo ci) {
        if (RuOK.get().chatFix && this.minecraft != null) {
            int offset = ChatFix.getOffset(this.minecraft);
            if (offset != 0) context.pose().translate(0.0F, offset);
        }
    }
}
