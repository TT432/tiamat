package io.github.tt432.tiamat;

import io.github.tt432.tiamat.common.entity.TiamatEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * @author TT432
 */
@Mod(Tiamat.MOD_ID)
public class Tiamat {
    public static final String MOD_ID = "tiamat";

    public Tiamat(IEventBus bus) {
        TiamatEntities.REGISTER.register(bus);
    }
}
