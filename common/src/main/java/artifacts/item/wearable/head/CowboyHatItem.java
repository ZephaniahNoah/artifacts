package artifacts.item.wearable.head;

import artifacts.item.wearable.MobEffectItem;
import artifacts.registry.ModGameRules;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class CowboyHatItem extends MobEffectItem {

    public CowboyHatItem() {
        super(MobEffects.MOVEMENT_SPEED, ModGameRules.COWBOY_HAT_SPEED_LEVEL, 40);
    }

    @Override
    protected LivingEntity getTarget(LivingEntity entity) {
        if (entity.getControlledVehicle() instanceof LivingEntity target) {
            return target;
        }
        return null;
    }

    @Override
    protected int getUpdateInterval() {
        return 10;
    }

    @Override
    protected boolean shouldShowParticles() {
        return true;
    }
}
