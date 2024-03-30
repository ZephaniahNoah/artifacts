package artifacts.item.wearable.head;

import artifacts.item.wearable.MobEffectItem;
import artifacts.registry.ModGameRules;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class SnorkelItem extends MobEffectItem {

    public SnorkelItem() {
        super(MobEffects.WATER_BREATHING, ModGameRules.SNORKEL_ENABLED);
    }

    @Override
    protected void addEffectsTooltip(ItemStack stack, List<MutableComponent> tooltip) {
        if (ModGameRules.SNORKEL_WATER_BREATHING_DURATION.get() == 0) {
            tooltip.add(tooltipLine("infinite"));
        } else {
            tooltip.add(tooltipLine("limited"));
        }
    }

    @Override
    protected int getDuration(LivingEntity entity) {
        int duration = ModGameRules.SNORKEL_WATER_BREATHING_DURATION.get();

        if (duration == 0) {
            return 40;
        } else if (entity instanceof Player && entity.getItemBySlot(EquipmentSlot.HEAD).is(Items.TURTLE_HELMET) && !entity.isEyeInFluid(FluidTags.WATER)) {
            duration += 200;
        }
        return duration + 19;
    }

    @Override
    protected boolean shouldShowIcon() {
        return ModGameRules.SNORKEL_WATER_BREATHING_DURATION.get() > 0;
    }

    @Override
    public boolean isEffectActive(LivingEntity entity) {
        if (ModGameRules.SNORKEL_WATER_BREATHING_DURATION.get() > 0 && entity.isEyeInFluid(FluidTags.WATER)) {
            return false;
        }
        return super.isEffectActive(entity);
    }
}
