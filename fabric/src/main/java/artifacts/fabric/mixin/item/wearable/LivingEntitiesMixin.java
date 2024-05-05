package artifacts.fabric.mixin.item.wearable;

import artifacts.ability.AbsorbDamageAbility;
import artifacts.ability.ApplyFireResistanceAfterFireDamageAbility;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {LivingEntity.class, Player.class})
public abstract class LivingEntitiesMixin extends Entity {

    public LivingEntitiesMixin(EntityType<?> type, Level world) {
        super(type, world);
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "actuallyHurt", allow = 1, at = @At(value = "JUMP", opcode = Opcodes.IFNE))
    private void onEntityDamaged(DamageSource source, float amount, CallbackInfo info) {
        if (!this.isInvulnerableTo(source)) {
            AbsorbDamageAbility.onLivingDamage((LivingEntity) (Object) this, source, amount);
            ApplyFireResistanceAfterFireDamageAbility.onLivingDamage((LivingEntity) (Object) this, source, amount);
        }
    }
}
