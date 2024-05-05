package artifacts.network;

import artifacts.Artifacts;
import artifacts.ability.ArtifactAbility;
import artifacts.component.AbilityToggles;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ToggleArtifactPacket(ArtifactAbility.Type<?> toggle) implements CustomPacketPayload {

    public static final Type<ToggleArtifactPacket> TYPE = new Type<>(Artifacts.id("toggle_artifacts"));

    public static final StreamCodec<FriendlyByteBuf, ToggleArtifactPacket> CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.map(ModAbilities.REGISTRY::get, ModAbilities.REGISTRY::getId),
            ToggleArtifactPacket::toggle,
            ToggleArtifactPacket::new
    );

    void apply(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            AbilityToggles abilityToggles = PlatformServices.platformHelper.getAbilityToggles(player);
            if (abilityToggles != null) {
                abilityToggles.toggle(toggle, context.getPlayer());
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
