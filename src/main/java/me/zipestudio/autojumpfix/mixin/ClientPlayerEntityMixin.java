package me.zipestudio.autojumpfix.mixin;

import me.zipestudio.autojumpfix.backend.AJFAction;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow
    protected abstract boolean shouldAutoJump();


    //? if <1.21 {
    /*@Shadow
    private int ticksToNextAutojump;
    *///?} else {
    @Shadow private int ticksToNextAutoJump;
    //?}

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void autoJump(float dx, float dz) {
        if (this.shouldAutoJump()) {

            //? if <1.21 {
            /*this.ticksToNextAutojump =
            *///?} else {
            this.ticksToNextAutoJump =
            //?}

            AJFAction.autojumpPlayer((ClientPlayerEntity) (Object) this, dx, dz) ? 1 : 0;
        }
    }

}