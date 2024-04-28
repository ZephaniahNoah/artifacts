package artifacts.forge.curio;

import artifacts.item.wearable.WearableArtifactItem;
import artifacts.util.DamageSourceHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class WearableArtifactCurio implements ICurioItem {

    private final WearableArtifactItem item;

    public WearableArtifactCurio(WearableArtifactItem item) {
        this.item = item;
    }

    @Override
    public final void curioTick(SlotContext slotContext, ItemStack stack) {
        item.wornTick(slotContext.entity(), stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack originalStack, ItemStack stack) {
        item.onEquip(slotContext.entity(), stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        item.onUnequip(slotContext.entity(), stack);
    }

    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        if (DamageSourceHelper.shouldDestroyWornItemsOnDeath(slotContext.entity())) {
            return ICurio.DropRule.DESTROY;
        }
        return ICurioItem.super.getDropRule(slotContext, source, lootingLevel, recentlyHit, stack);
    }

    @Override
    public final ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(item.getEquipSound(), 1, 1);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return item.getFoodProperties(stack, slotContext.entity()) == null;
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, @Nullable LootContext lootContext, ItemStack stack) {
        return item.getFortuneLevel();
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        return item.getLootingLevel();
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return item.makesPiglinsNeutral();
    }

    @Override
    public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        return item.canWalkOnPowderedSnow();
    }
}
