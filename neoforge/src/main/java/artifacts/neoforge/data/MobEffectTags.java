package artifacts.neoforge.data;

import artifacts.Artifacts;
import artifacts.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class MobEffectTags extends IntrinsicHolderTagsProvider<MobEffect> {

    public MobEffectTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput,
                Registries.MOB_EFFECT,
                lookupProvider,
                effect -> BuiltInRegistries.MOB_EFFECT.getResourceKey(effect).orElseThrow(),
                Artifacts.MOD_ID,
                existingFileHelper
        );
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.ANTIDOTE_VESSEL_CANCELLABLE).add(
                MobEffects.BLINDNESS.value(),
                MobEffects.CONFUSION.value(),
                MobEffects.DIG_SLOWDOWN.value(),
                MobEffects.HUNGER.value(),
                MobEffects.LEVITATION.value(),
                MobEffects.MOVEMENT_SLOWDOWN.value(),
                MobEffects.POISON.value(),
                MobEffects.WEAKNESS.value(),
                MobEffects.WITHER.value()
        );
    }

    @Override
    public String getName() {
        return "Mob Effect Tags";
    }
}
