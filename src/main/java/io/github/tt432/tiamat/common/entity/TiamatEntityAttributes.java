package io.github.tt432.tiamat.common.entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TiamatEntityAttributes {
    @SubscribeEvent
    public static void onEvent(EntityAttributeCreationEvent event) {
        event.put(TiamatEntities.FAMILIAR.get(), FamiliarEntity.createAttributes().build());
    }
}
