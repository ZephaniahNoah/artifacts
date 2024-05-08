package artifacts.fabric.mixin.item.wearable;

import artifacts.fabric.client.CosmeticsHelper;
import artifacts.item.WearableArtifactItem;
import artifacts.util.AbilityHelper;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WearableArtifactItem.class)
public abstract class WearableArtifactItemMixin extends Item {

    public WearableArtifactItemMixin(Properties properties) {
        super(properties);
        throw new UnsupportedOperationException();
    }

    @Shadow
    public abstract SoundEvent getEquipSound();

    @Shadow
    public abstract float getEquipSoundPitch();

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!stack.has(DataComponents.FOOD) && TrinketItem.equipItem(user, stack)) {
            user.playSound(getEquipSound(), 1, getEquipSoundPitch());

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return super.use(level, user, hand);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack holdingStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (!AbilityHelper.isCosmetic(slotStack) && clickAction == ClickAction.SECONDARY && holdingStack.isEmpty()) {
            CosmeticsHelper.toggleCosmetics(slotStack);
            return true;
        }

        return super.overrideOtherStackedOnMe(slotStack, holdingStack, slot, clickAction, player, slotAccess);
    }
}
