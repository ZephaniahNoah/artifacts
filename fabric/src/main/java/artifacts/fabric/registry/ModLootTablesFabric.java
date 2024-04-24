package artifacts.fabric.registry;

import artifacts.Artifacts;
import artifacts.registry.ModLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

public class ModLootTablesFabric {

    public static void onLootTableLoad(ResourceLocation id, LootTable.Builder supplier) {
        if (ModLootTables.INJECTED_LOOT_TABLES.contains(id)) {
            supplier.withPool(LootPool.lootPool().add(getInjectEntry(id.getPath())));
        }
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        ResourceLocation table = Artifacts.id("inject/" + name);
        return LootTableReference.lootTableReference(table).setWeight(1);
    }
}
