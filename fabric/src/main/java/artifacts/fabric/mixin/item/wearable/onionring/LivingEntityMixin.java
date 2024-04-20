package artifacts.fabric.mixin.item.wearable.onionring;

import artifacts.item.wearable.hands.OnionRingItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    protected ItemStack useItem;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "completeUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;triggerItemUseEffects(Lnet/minecraft/world/item/ItemStack;I)V"))
    protected void completeUsingItem(CallbackInfo ci) {
        FoodProperties properties = useItem.getItem().getFoodProperties();
        System.out.println(properties);
        if (properties != null) {
            OnionRingItem.applyMiningSpeedBuff((LivingEntity) (Object) this, properties);
        }
    }
}
