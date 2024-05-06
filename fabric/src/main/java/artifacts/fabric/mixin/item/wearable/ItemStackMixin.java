package artifacts.fabric.mixin.item.wearable;

import artifacts.Artifacts;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = ItemStack.class, priority = 1500)
public class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip(ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z",
            ordinal = 4,
            shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void getTooltipLines(Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, List<Component> tooltip) {
        //noinspection ConstantConditions
        ItemStack stack = (ItemStack) (Object) this;

        if (!Artifacts.CONFIG.client.showTooltips || !AbilityHelper.hasAbility(ModAbilities.ATTRIBUTE_MODIFIER.get(), stack)) {
            return;
        }

        tooltip.add(Component.translatable("trinkets.tooltip.attributes.all").withStyle(ChatFormatting.GRAY));
        AbilityHelper.getAbilities(ModAbilities.ATTRIBUTE_MODIFIER.get(), stack).forEach(ability -> {
            double amount = ability.amount().get();

            if (ability.operation() == AttributeModifier.Operation.ADD_VALUE) {
                if (ability.attribute().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                    amount *= 10;
                }
            } else {
                amount *= 100;
            }

            Component text = Component.translatable(ability.attribute().getDescriptionId());
            if (amount > 0) {
                tooltip.add(Component.translatable("attribute.modifier.plus." + ability.operation().toValue(),
                        ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(amount), text).withStyle(ChatFormatting.BLUE));
            }
        });
    }
}
