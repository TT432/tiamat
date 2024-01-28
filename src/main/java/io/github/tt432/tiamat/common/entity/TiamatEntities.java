package io.github.tt432.tiamat.common.entity;

import io.github.tt432.tiamat.Tiamat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author TT432
 */
public class TiamatEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER =
            DeferredRegister.create(Registries.ENTITY_TYPE, Tiamat.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<FamiliarEntity>> FAMILIAR =
            REGISTER.register("familiar", () ->
                    EntityType.Builder
                            .of(FamiliarEntity::new, MobCategory.MISC)
                            .build("familiar"));
}
