package artifacts.fabric.mixin.item.wearable;

import artifacts.item.wearable.WearableArtifactItem;
import artifacts.platform.PlatformServices;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {

    @Inject(method = "isWearingGold", at = @At("RETURN"), cancellable = true)
    private static void isWearingGold(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && PlatformServices.platformHelper.isEquippedBy(entity, stack ->
                stack.getItem() instanceof WearableArtifactItem item && item.makesPiglinsNeutral()
        )) {
            cir.setReturnValue(true);
        }
    }
}
