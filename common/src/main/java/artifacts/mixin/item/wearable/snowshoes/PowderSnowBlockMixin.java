package artifacts.mixin.item.wearable.snowshoes;

import artifacts.registry.ModGameRules;
import artifacts.registry.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void canEntityWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity
                && ModGameRules.SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW.get()
                && ModItems.SNOWSHOES.get().isEquippedBy(livingEntity)
        ) {
            cir.setReturnValue(true);
        }
    }
}
