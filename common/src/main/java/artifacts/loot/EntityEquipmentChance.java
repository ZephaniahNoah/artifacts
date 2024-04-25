package artifacts.loot;

import artifacts.Artifacts;
import artifacts.registry.ModLootConditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class EntityEquipmentChance implements LootItemCondition {

    private static final EntityEquipmentChance INSTANCE = new EntityEquipmentChance();

    private EntityEquipmentChance() {

    }

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.ENTITY_EQUIPMENT_CHANCE.get();
    }

    @Override
    public boolean test(LootContext context) {
        return context.getRandom().nextDouble() < Artifacts.CONFIG.common.getEntityEquipmentChance();
    }

    public static LootItemCondition.Builder entityEquipmentChance() {
        return () -> INSTANCE;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EntityEquipmentChance> {

        @Override
        public void serialize(JsonObject object, EntityEquipmentChance condition, JsonSerializationContext context) {

        }

        @Override
        public EntityEquipmentChance deserialize(JsonObject object, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
