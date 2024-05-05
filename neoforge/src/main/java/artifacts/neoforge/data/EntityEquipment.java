package artifacts.neoforge.data;

import artifacts.loot.ConfigValueChance;
import artifacts.registry.ModItems;
import artifacts.registry.ModLootTables;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.HashSet;
import java.util.Set;

public class EntityEquipment {

    private final LootTables lootTables;
    private final Set<EntityType<?>> entityTypes = new HashSet<>();

    public EntityEquipment(LootTables lootTables) {
        this.lootTables = lootTables;
    }

    public void addLootTables() {
        entityTypes.clear();

        addItems(EntityType.ZOMBIE,
                ModItems.COWBOY_HAT.get(),
                ModItems.BUNNY_HOPPERS.get(),
                ModItems.SCARF_OF_INVISIBILITY.get()
        );
        addItems(EntityType.HUSK,
                ModItems.VAMPIRIC_GLOVE.get(),
                ModItems.THORN_PENDANT.get()
        );
        addItems(EntityType.DROWNED,
                ModItems.SNORKEL.get(),
                ModItems.FLIPPERS.get()
        );
        addEquipment(EntityType.SKELETON, LootPool.lootPool()
                .add(item(ModItems.NIGHT_VISION_GOGGLES.get()))
                .add(LootTables.drinkingHat(1))
                .add(item(ModItems.FLAME_PENDANT.get()))
        );
        addItems(EntityType.STRAY,
                ModItems.SNOWSHOES.get(),
                ModItems.STEADFAST_SPIKES.get()
        );
        addItems(EntityType.WITHER_SKELETON,
                ModItems.FIRE_GAUNTLET.get(),
                ModItems.ANTIDOTE_VESSEL.get()
        );
        addItems(EntityType.PIGLIN,
                ModItems.GOLDEN_HOOK.get(),
                ModItems.UNIVERSAL_ATTRACTOR.get(),
                ModItems.OBSIDIAN_SKULL.get()
        );
        addItems(EntityType.ZOMBIFIED_PIGLIN,
                ModItems.GOLDEN_HOOK.get(),
                ModItems.UNIVERSAL_ATTRACTOR.get(),
                ModItems.OBSIDIAN_SKULL.get()
        );
        addItems(EntityType.PIGLIN_BRUTE,
                ModItems.ONION_RING.get()
        );

        if (!entityTypes.equals(ModLootTables.ENTITY_EQUIPMENT.keySet())) {
            throw new IllegalStateException(Sets.symmetricDifference(entityTypes, ModLootTables.ENTITY_EQUIPMENT.keySet()).toString());
        }
    }

    public void addItems(EntityType<?> entityType, Item... items) {
        LootPool.Builder pool = LootPool.lootPool();
        for (Item item : items) {
            pool.add(item(item));
        }
        addEquipment(entityType, pool);
    }

    protected static LootPoolSingletonContainer.Builder<?> item(Item item) {
        return LootItem.lootTableItem(item).setWeight(1);
    }

    public void addEquipment(EntityType<?> entityType, LootPool.Builder pool) {
        entityTypes.add(entityType);
        LootTable.Builder builder = LootTable.lootTable();
        builder.withPool(pool.when(ConfigValueChance.entityEquipmentChance()));
        lootTables.addLootTable(ModLootTables.entityEquipmentLootTable(entityType).location().getPath(), builder, LootContextParamSets.ALL_PARAMS);
    }
}
