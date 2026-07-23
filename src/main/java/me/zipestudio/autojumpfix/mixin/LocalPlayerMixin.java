package me.zipestudio.autojumpfix.mixin;

import me.zipestudio.autojumpfix.backend.AJFAction;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Shadow
    private int autoJumpTime;

    @Shadow protected abstract boolean canAutoJump();

    @Inject(method = "updateAutoJump", at = @At("HEAD"), cancellable = true)
    private void onAutoJump(float dx, float dz, CallbackInfo ci) {

        if (!this.canAutoJump()) return;
        float blockJumpFactor = ((EntityInvoker) this).autojumpfix$getBlockJumpFactor();
        this.autoJumpTime = AJFAction.autojumpPlayer((LocalPlayer) (Object) this, dx, dz, blockJumpFactor) ? 1 : 0;
        ci.cancel();
    }

}