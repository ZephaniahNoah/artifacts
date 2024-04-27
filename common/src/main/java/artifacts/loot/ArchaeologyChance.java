package artifacts.loot;

import artifacts.Artifacts;
import artifacts.registry.ModLootConditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ArchaeologyChance implements LootItemCondition {

    private static final ArchaeologyChance INSTANCE = new ArchaeologyChance();

    private ArchaeologyChance() {

    }

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.ARCHAEOLOGY_CHANCE.get();
    }

    @Override
    public boolean test(LootContext context) {
        return context.getRandom().nextDouble() < Artifacts.CONFIG.common.getArchaeologyChance();
    }

    public static LootItemCondition.Builder archaeologyChance() {
        return () -> INSTANCE;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ArchaeologyChance> {

        @Override
        public void serialize(JsonObject object, ArchaeologyChance condition, JsonSerializationContext context) {

        }

        @Override
        public ArchaeologyChance deserialize(JsonObject object, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
