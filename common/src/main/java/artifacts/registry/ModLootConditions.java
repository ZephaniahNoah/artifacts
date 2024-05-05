package artifacts.registry;

import artifacts.Artifacts;
import artifacts.loot.ArtifactRarityAdjustedChance;
import artifacts.loot.ConfigValueChance;
import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ModLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Artifacts.MOD_ID, Registries.LOOT_CONDITION_TYPE);

    public static final RegistrySupplier<LootItemConditionType> ARTIFACT_RARITY_ADJUSTED_CHANCE = register("artifact_rarity_adjusted_chance", ArtifactRarityAdjustedChance.CODEC);
    public static final RegistrySupplier<LootItemConditionType> CONFIG_VALUE_CHANCE = register("config_value_chance", ConfigValueChance.CODEC);

    private static RegistrySupplier<LootItemConditionType> register(String name, MapCodec<? extends LootItemCondition> codec) {
        return RegistrySupplier.of(LOOT_CONDITIONS.register(name, () -> new LootItemConditionType(codec)));
    }
}
