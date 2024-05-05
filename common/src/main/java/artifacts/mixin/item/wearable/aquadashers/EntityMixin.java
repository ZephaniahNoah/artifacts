package artifacts.mixin.item.wearable.aquadashers;

import artifacts.component.SwimData;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModSoundEvents;
import artifacts.util.AbilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private Level level;

    @Shadow @Final protected RandomSource random;

    @Shadow private EntityDimensions dimensions;

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Shadow public abstract Vec3 getDeltaMovement();

    @SuppressWarnings("deprecation")
    @Inject(method = "playStepSound", at = @At("HEAD"))
    private void playWaterStepSound(BlockPos pos, BlockState blockState, CallbackInfo callbackInfo) {
        if (blockState.liquid() && isRunningWithAquaDashers()) {
            //noinspection ConstantConditions
            ((LivingEntity) (Object) this).playSound(ModSoundEvents.WATER_STEP.get(), 0.15F, 1);
        }
    }

    @Inject(method = "spawnSprintParticle", at = @At("HEAD"))
    private void spawnWaterSprintParticle(CallbackInfo callbackInfo) {
        if (isRunningWithAquaDashers()) {
            BlockPos pos = new BlockPos(Mth.floor(getX()), Mth.floor(getY() - 0.2), Mth.floor(getZ()));
            BlockState blockstate = level.getBlockState(pos);
            if (blockstate.getRenderShape() == RenderShape.INVISIBLE) {
                ParticleOptions particle;
                Vec3 motion = getDeltaMovement().multiply(-4, 0, -4);
                if (blockstate.getFluidState().is(FluidTags.LAVA)) {
                    motion = motion.add(0, 1, 0);
                    if (random.nextInt(3) == 0) {
                        particle = ParticleTypes.LAVA;
                    } else {
                        particle = ParticleTypes.FLAME;
                        motion = motion.multiply(0.2, 0.03, 0.2);
                    }
                } else if (!blockstate.getFluidState().isEmpty()) {
                    if (random.nextInt(3) == 0) {
                        particle = ParticleTypes.BUBBLE;
                    } else {
                        particle = ParticleTypes.SPLASH;
                        motion = motion.add(0, 1.5, 0);
                    }
                } else {
                    return;
                }
                level.addParticle(particle, getX() + (random.nextDouble() - 0.5) * dimensions.width(), getY(), getZ() + (random.nextDouble() - 0.5) * dimensions.width(), motion.x, motion.y, motion.z);
            }
        }
    }

    @SuppressWarnings({"ConstantConditions", "UnreachableCode"})
    @Unique
    private boolean isRunningWithAquaDashers() {
        if (!((Object) this instanceof LivingEntity entity)) {
            return false;
        }
        SwimData swimData = PlatformServices.platformHelper.getSwimData(entity);
        return swimData != null
                && AbilityHelper.hasAbilityActive(ModAbilities.SPRINT_ON_WATER.get(), entity)
                && entity.isSprinting()
                && !swimData.isWet();
    }
}
