package artifacts.mixin.item.wearable.anglershat;

import artifacts.registry.ModGameRules;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getFishingLuckBonus", at = @At("RETURN"))
    private static int getFishingLuckBonus(int original) {
        return original + ModGameRules.ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS.get();
    }

    @ModifyReturnValue(method = "getFishingSpeedBonus", at = @At("RETURN"))
    private static int getFishingSpeedBonus(int original) {
        // Lure >5 breaks fishing, don't return more than 5 unless original was more than 5
        if (original >= 5) {
            return original;
        }
        return Math.min(5, original + ModGameRules.ANGLERS_HAT_LURE_LEVEL_BONUS.get());
    }
}
