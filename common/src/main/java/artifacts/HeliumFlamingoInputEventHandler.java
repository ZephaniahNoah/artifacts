package artifacts;

import artifacts.component.SwimData;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModKeyMappings;
import artifacts.util.AbilityHelper;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class HeliumFlamingoInputEventHandler {

    private static boolean wasSprintKeyDown;
    private static boolean wasSprintingOnGround;
    private static boolean hasTouchedGround;

    public static void register() {
        ClientTickEvent.CLIENT_POST.register(HeliumFlamingoInputEventHandler::onClientTick);
    }

    private static void onClientTick(Minecraft instance) {
        LocalPlayer player = instance.player;
        if (player != null && player.input != null) {
            handleHeliumFlamingoInput(player);
        }
    }

    private static void handleHeliumFlamingoInput(Player player) {
        if (!AbilityHelper.hasAbility(ModAbilities.SWIM_IN_AIR, player)) {
            return;
        }

        boolean isSprintKeyDown = ModKeyMappings.getHeliumFlamingoKey().isDown();

        SwimData swimData = PlatformServices.platformHelper.getSwimData(player);
        if (swimData == null) {
            return;
        }

        if (!swimData.isSwimming()) {
            if (player.onGround()) {
                hasTouchedGround = true;
            } else if (canActivateHeliumFlamingo(swimData, player, isSprintKeyDown)) {
                swimData.setSwimming(player, true);
                swimData.syncSwimming();
                hasTouchedGround = false;
            }
        } else if (player.getAbilities().flying) {
            swimData.setSwimming(player, false);
            swimData.syncSwimming();
            hasTouchedGround = true;
        }

        wasSprintKeyDown = isSprintKeyDown;
        if (!isSprintKeyDown) {
            wasSprintingOnGround = false;
        } else if (player.onGround()) {
            wasSprintingOnGround = true;
        }
    }

    private static boolean canActivateHeliumFlamingo(SwimData swimData, Player player, boolean isSprintKeyDown) {
        if (swimData.isSwimming()
                || swimData.getSwimTime() < 0
                || !AbilityHelper.hasAbility(ModAbilities.SWIM_IN_AIR, player)) {
            return false;
        }
        if (player.isSwimming()) {
            return true;
        }
        return isSprintKeyDown
                && !wasSprintKeyDown
                && !wasSprintingOnGround
                && hasTouchedGround
                && !player.onGround()
                && (!player.isInWater() || AbilityHelper.hasAbility(ModAbilities.SINKING, player))
                && !player.isFallFlying()
                && !player.getAbilities().flying
                && !player.isPassenger();
    }
}
