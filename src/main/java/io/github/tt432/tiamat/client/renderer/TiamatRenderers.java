package io.github.tt432.tiamat.client.renderer;

import io.github.tt432.tiamat.common.entity.TiamatEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TiamatRenderers {
    @SubscribeEvent
    public static void onEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TiamatEntities.FAMILIAR.get(), FamiliarEntityRenderer::new);
    }
}
