package artifacts.item;

import artifacts.Artifacts;
import artifacts.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public abstract class ArtifactItem extends Item {

    @SuppressWarnings("UnstableApiUsage")
    public ArtifactItem(Properties properties) {
        super(properties.arch$tab(ModItems.CREATIVE_TAB.supplier()).stacksTo(1).rarity(Rarity.RARE).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltipList, TooltipFlag tooltipFlag) {
        if (Artifacts.CONFIG.client.showTooltips) {
            List<MutableComponent> tooltip = new ArrayList<>();
            addTooltip(tooltip);
            tooltip.forEach(line -> tooltipList.add(line.withStyle(ChatFormatting.GRAY)));
        }
    }

    protected void addTooltip(List<MutableComponent> tooltip) {
        if (isCosmetic()) {
            tooltip.add(Component.translatable("%s.tooltip.cosmetic".formatted(Artifacts.MOD_ID)).withStyle(ChatFormatting.ITALIC));
        } else {
            addEffectsTooltip(tooltip);
        }
    }

    protected void addEffectsTooltip(List<MutableComponent> tooltip) {
        tooltip.add(Component.translatable("%s.tooltip.item.%s".formatted(Artifacts.MOD_ID, getTooltipItemName())));
    }

    protected MutableComponent tooltipLine(String lineId, Object... args) {
        return Component.translatable("%s.tooltip.item.%s.%s".formatted(Artifacts.MOD_ID, getTooltipItemName(), lineId), args);
    }

    protected String getTooltipItemName() {
        return BuiltInRegistries.ITEM.getKey(this).getPath();
    }

    public abstract boolean isCosmetic();
}
