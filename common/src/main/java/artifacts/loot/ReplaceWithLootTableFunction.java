package artifacts.loot;

import artifacts.Artifacts;
import artifacts.registry.ModLootFunctions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class ReplaceWithLootTableFunction extends LootItemConditionalFunction {

    public static final MapCodec<ReplaceWithLootTableFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance).and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(f -> f.lootTable))
                    .apply(instance, ReplaceWithLootTableFunction::new)
    );

    private final ResourceKey<LootTable> lootTable;

    public ReplaceWithLootTableFunction(List<LootItemCondition> conditions, ResourceKey<LootTable> lootTable) {
        super(conditions);
        this.lootTable = lootTable;
    }

    @Override
    public LootItemFunctionType<ReplaceWithLootTableFunction> getType() {
        return ModLootFunctions.REPLACE_WITH_LOOT_TABLE.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext) {
        if (stack.isEmpty()) {
            return stack;
        }
        LootTable table = lootContext.getLevel().getServer().reloadableRegistries().getLootTable(lootTable);
        ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
        table.getRandomItemsRaw(lootContext, loot::add);
        if (loot.size() > 1) {
            Artifacts.LOGGER.warn("Loot table {} in roll_loot_table function generated more than 1 item", lootTable.toString());
        } else if (loot.size() == 0) {
            Artifacts.LOGGER.warn("Failed to generate any loot from loot table {}", lootTable.toString());
            return ItemStack.EMPTY;
        }
        return loot.get(0);
    }

    public static LootItemConditionalFunction.Builder<?> replaceWithLootTable(ResourceKey<LootTable> lootTable) {
        return simpleBuilder((conditions) -> new ReplaceWithLootTableFunction(conditions, lootTable));
    }
}
