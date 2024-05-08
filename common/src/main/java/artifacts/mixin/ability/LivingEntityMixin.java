package artifacts.mixin.ability;

import artifacts.event.ArtifactEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
        throw new UnsupportedOperationException();
    }

    @Shadow
    protected abstract ItemStack getLastArmorItem(EquipmentSlot slot);

    @Shadow
    protected abstract ItemStack getItemBySlot(EquipmentSlot slot);

    // TODO fix this
    @Inject(method = "handleEquipmentChanges", at = @At("HEAD"))
    private void handleEquipmentChanges(Map<EquipmentSlot, ItemStack> map, CallbackInfo info) {
        for (EquipmentSlot slot : map.keySet()) {
            if (!slot.isArmor()) {
                continue;
            }
            ItemStack oldStack = getLastArmorItem(slot);
            ItemStack newStack = getItemBySlot(slot);

            ArtifactEvents.onItemChanged((LivingEntity) (Object) this, oldStack, newStack);
        }
    }
}
