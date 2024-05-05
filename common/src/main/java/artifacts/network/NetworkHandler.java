package artifacts.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class NetworkHandler {

    public static void register() {
        registerS2C(BooleanGameRuleChangedPacket.TYPE, BooleanGameRuleChangedPacket.CODEC, BooleanGameRuleChangedPacket::apply);
        registerS2C(IntegerGameRuleChangedPacket.TYPE, IntegerGameRuleChangedPacket.CODEC, IntegerGameRuleChangedPacket::apply);
        registerS2C(ChorusTotemUsedPacket.TYPE, ChorusTotemUsedPacket.CODEC, ChorusTotemUsedPacket::apply);
        registerS2C(PlaySoundAtPlayerPacket.TYPE, PlaySoundAtPlayerPacket.CODEC, PlaySoundAtPlayerPacket::apply);
        registerS2C(SyncArtifactTogglesPacket.TYPE, SyncArtifactTogglesPacket.CODEC, SyncArtifactTogglesPacket::apply);

        registerC2S(DoubleJumpPacket.TYPE, DoubleJumpPacket.CODEC, DoubleJumpPacket::apply);
        registerC2S(SwimPacket.TYPE, SwimPacket.CODEC, SwimPacket::apply);
        registerC2S(ToggleArtifactPacket.TYPE, ToggleArtifactPacket.CODEC, ToggleArtifactPacket::apply);
    }

    private static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), type, codec, receiver);
    }

    private static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.s2c(), type, codec, receiver);
        } else {
            NetworkManager.registerS2CPayloadType(type, codec);
        }
    }
}
