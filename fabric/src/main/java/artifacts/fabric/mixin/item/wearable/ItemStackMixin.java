package artifacts.fabric.mixin.item.wearable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import artifacts.Artifacts;
import artifacts.item.wearable.ArtifactAttributeModifier;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

@Mixin(value = ItemStack.class, priority = 1500)
public class ItemStackMixin {

	public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = Util.make(new DecimalFormat("#.##"), decimalFormat -> decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));

	@Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip(ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z", ordinal = 4, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void getTooltipLines(Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, List<Component> tooltip) {
		// noinspection ConstantConditions
		ItemStack stack = (ItemStack) (Object) this;
		if (!(stack.getItem() instanceof WearableArtifactItem item)) {
			return;
		}

		List<ArtifactAttributeModifier> attributeModifiers = item.getAttributeModifiers();
		if (attributeModifiers.isEmpty() || item.isCosmetic() || !Artifacts.CONFIG.client.showTooltips) {
			return;
		}

		tooltip.add(Component.translatable("trinkets.tooltip.attributes.all").withStyle(ChatFormatting.GRAY));
		for (ArtifactAttributeModifier modifier : item.getAttributeModifiers()) {
			double amount = modifier.getAmount();

			if (modifier.getOperation() == AttributeModifier.Operation.ADD_VALUE) {
				if (modifier.getAttribute().equals(Attributes.KNOCKBACK_RESISTANCE)) {
					amount *= 10;
				}
			} else {
				amount *= 100;
			}

			Component text = Component.translatable(modifier.getAttribute().getDescriptionId());
			if (amount > 0) {
				tooltip.add(Component.translatable("attribute.modifier.plus." + modifier.getOperation().id(), ATTRIBUTE_MODIFIER_FORMAT.format(amount), text).withStyle(ChatFormatting.BLUE));
			}
		}
	}
}
