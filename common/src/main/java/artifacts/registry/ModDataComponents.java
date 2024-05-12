package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.ArtifactAbility;
import dev.architectury.registry.registries.DeferredRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.List;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Artifacts.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<List<ArtifactAbility>>> ABILITIES = RegistrySupplier.of(DATA_COMPONENT_TYPES.register("abilities", () ->
            DataComponentType.<List<ArtifactAbility>>builder()
                    .persistent(ArtifactAbility.CODEC.listOf())
                    .networkSynchronized(ByteBufCodecs.<ByteBuf, ArtifactAbility>list().apply(ArtifactAbility.STREAM_CODEC))
                    .cacheEncoding()
                    .build()
    ));
    private static boolean REGISTERED = false;

    // On Fabric 1.20.6 it appears that the init method is not called early enough.
    // This means we'll have to call this multiple times when needed. Kinda hacky but it works.
    public static void register() {
        if (!REGISTERED) {
            DATA_COMPONENT_TYPES.register();
            REGISTERED = true;
        }
    }

}
