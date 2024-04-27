package artifacts.loot;

import artifacts.Artifacts;
import artifacts.registry.ModLootConditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ArtifactRarityRandomChance(float defaultProbability) implements LootItemCondition {

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.CONFIGURABLE_ARTIFACT_CHANCE.get();
    }

    @Override
    public boolean test(LootContext context) {
        if (Artifacts.CONFIG.common.getArtifactRarity() > 9999) {
            return false;
        }
        float r = (float) Artifacts.CONFIG.common.getArtifactRarity();
        float p = defaultProbability;
        float adjustedProbability = p / (p + r - r * p);
        return context.getRandom().nextFloat() < adjustedProbability;
    }

    public static LootItemCondition.Builder configurableRandomChance(float probability) {
        return () -> new ArtifactRarityRandomChance(probability);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ArtifactRarityRandomChance> {

        @Override
        public void serialize(JsonObject object, ArtifactRarityRandomChance condition, JsonSerializationContext context) {
            object.addProperty("default_probability", condition.defaultProbability);
        }

        @Override
        public ArtifactRarityRandomChance deserialize(JsonObject object, JsonDeserializationContext context) {
            return new ArtifactRarityRandomChance(GsonHelper.getAsFloat(object, "default_probability"));
        }
    }
}
