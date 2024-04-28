package artifacts.neoforge;

import artifacts.Artifacts;
import artifacts.config.ModConfig;
import artifacts.neoforge.curio.WearableArtifactCurio;
import artifacts.neoforge.event.ArtifactEventsForge;
import artifacts.neoforge.event.SwimEventsForge;
import artifacts.neoforge.registry.ModAttachmentTypes;
import artifacts.neoforge.registry.ModItemsForge;
import artifacts.neoforge.registry.ModLootModifiers;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModItems;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(Artifacts.MOD_ID)
public class ArtifactsForge {

    public ArtifactsForge(IEventBus modBus) {
        Artifacts.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new ArtifactsForgeClient(modBus);
        }

        ModLootModifiers.LOOT_MODIFIERS.register(modBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modBus);

        modBus.addListener(this::onCommonSetup);
        modBus.addListener(this::registerCapabilities);

        registerConfig();
        ArtifactEventsForge.register();
        SwimEventsForge.register();
    }

    private void registerConfig() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                )
        );
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModItems.ITEMS.forEach(entry -> {
            if (entry.get() instanceof WearableArtifactItem item) {
                CuriosApi.registerCurio(item, new WearableArtifactCurio(item));
            }
        });
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModItemsForge::register);
    }
}
