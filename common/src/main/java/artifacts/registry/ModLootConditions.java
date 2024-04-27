package artifacts.registry;

import artifacts.Artifacts;
import artifacts.loot.ArtifactRarityRandomChance;
import artifacts.loot.ConfigValueChance;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.Supplier;

public class ModLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Artifacts.MOD_ID, Registries.LOOT_CONDITION_TYPE);

    public static final RegistrySupplier<LootItemConditionType> CONFIGURABLE_ARTIFACT_CHANCE = register("configurable_random_chance", ArtifactRarityRandomChance.Serializer::new);
    public static final RegistrySupplier<LootItemConditionType> CONFIG_VALUE_CHANCE = register("config_value_chance", ConfigValueChance.Serializer::new);

    private static RegistrySupplier<LootItemConditionType> register(String name, Supplier<Serializer<? extends LootItemCondition>> serializer) {
        return RegistrySupplier.of(LOOT_CONDITIONS.register(name, () -> new LootItemConditionType(serializer.get())));
    }
}
