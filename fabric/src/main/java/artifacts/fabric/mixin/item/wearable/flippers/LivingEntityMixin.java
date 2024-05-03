package artifacts.fabric.mixin.item.wearable.flippers;

import artifacts.ability.SwimSpeedAbility;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyArg(method = "jumpInLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"), index = 1)
    private double increaseSwimUpSpeed(double y) {
        return getIncreasedSwimSpeed(y);
    }

    @ModifyArg(method = "travel", allow = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInLava()Z")
            )
    )
    private float increaseSwimSpeed(float speed) {
        return (float) getIncreasedSwimSpeed(speed);
    }

    @Unique
    private double getIncreasedSwimSpeed(double speed) {
        // noinspection ConstantConditions
        double swimSpeedBonus = AbilityHelper.sumDouble(
                ModAbilities.SWIM_SPEED.get(), (LivingEntity) (Object) this, SwimSpeedAbility::getSwimSpeedBonus, false
        );
        return speed * (1 + swimSpeedBonus);
    }
}
