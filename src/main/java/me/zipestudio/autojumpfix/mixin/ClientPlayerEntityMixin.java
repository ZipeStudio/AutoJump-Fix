package me.zipestudio.autojumpfix.mixin;

import me.zipestudio.autojumpfix.backend.AJFAction;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow
    protected abstract boolean shouldAutoJump();


    //? if <1.21.2 {
    /*@Shadow private int ticksToNextAutojump;
     *///?} else {
    @Shadow
    private int ticksToNextAutoJump;
    //?}

    @Inject(method = "autoJump", at = @At("HEAD"), cancellable = true)
    private void onAutoJump(float dx, float dz, CallbackInfo ci) {

        if (!this.shouldAutoJump()) return;

        //? if <1.21.2 {
        /*this.ticksToNextAutojump =
         *///?} else {
        this.ticksToNextAutoJump =
        //?}

        AJFAction.autojumpPlayer((ClientPlayerEntity) (Object) this, dx, dz) ? 1 : 0;
        ci.cancel();
    }

}