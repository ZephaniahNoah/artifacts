package artifacts.ability;

import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public record RemoveBadEffectsAbility(BooleanValue enabled, IntegerValue maxEffectDuration) implements ArtifactAbility {

    public static final MapCodec<RemoveBadEffectsAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanValue.field(ModGameRules.ANTIDOTE_VESSEL_ENABLED).forGetter(RemoveBadEffectsAbility::enabled),
            IntegerValue.field(ModGameRules.ANTIDOTE_VESSEL_MAX_EFFECT_DURATION).forGetter(RemoveBadEffectsAbility::maxEffectDuration)
    ).apply(instance, RemoveBadEffectsAbility::new));

    public static final StreamCodec<ByteBuf, RemoveBadEffectsAbility> STREAM_CODEC = StreamCodec.composite(
            BooleanValue.defaultStreamCodec(ModGameRules.ANTIDOTE_VESSEL_ENABLED),
            RemoveBadEffectsAbility::enabled,
            IntegerValue.defaultStreamCodec(ModGameRules.ANTIDOTE_VESSEL_MAX_EFFECT_DURATION),
            RemoveBadEffectsAbility::maxEffectDuration,
            RemoveBadEffectsAbility::new
    );

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.REMOVE_BAD_EFFECTS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled().get();
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        if (!isActive) {
            return;
        }
        Map<Holder<MobEffect>, MobEffectInstance> effects = new HashMap<>();

        int maxEffectDuration = maxEffectDuration().get();
        entity.getActiveEffectsMap().forEach((effect, instance) -> {
            if (ModTags.isInTag(effect.value(), ModTags.ANTIDOTE_VESSEL_CANCELLABLE) && instance.getDuration() > maxEffectDuration) {
                effects.put(effect, instance);
            }
        });

        effects.forEach((effect, instance) -> {
            entity.removeEffectNoUpdate(effect);
            if (maxEffectDuration > 0) {
                entity.addEffect(new MobEffectInstance(effect, maxEffectDuration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon()));
            }
        });
    }
}
