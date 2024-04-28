package artifacts.forge.client;

import artifacts.client.UmbrellaArmPoseHelper;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;

public class UmbrellaArmPoseHandler {

    public static void setup() {
        NeoForge.EVENT_BUS.addListener(UmbrellaArmPoseHandler::onLivingRender);
    }

    public static void onLivingRender(RenderLivingEvent.Pre<?, ?> event) {
        UmbrellaArmPoseHelper.setUmbrellaArmPose(event.getRenderer().getModel(), event.getEntity());
    }
}
