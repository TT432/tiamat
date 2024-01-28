package io.github.tt432.tiamat.common.data;

import io.github.tt432.tiamat.Tiamat;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TiamatDatapacks {
    public static final ResourceKey<Registry<FamiliarData>> FAMILIAR_DATA =
            ResourceKey.createRegistryKey(new ResourceLocation(Tiamat.MOD_ID, "data"));

    @SubscribeEvent
    public static void onEvent(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(FAMILIAR_DATA, FamiliarData.CODEC, FamiliarData.CODEC);
    }
}
