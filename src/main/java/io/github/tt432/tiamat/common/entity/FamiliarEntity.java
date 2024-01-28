package io.github.tt432.tiamat.common.entity;

import io.github.tt432.tiamat.common.data.FamiliarData;
import io.github.tt432.tiamat.common.data.TiamatDatapacks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

/**
 * @author TT432
 */
@Slf4j
public class FamiliarEntity extends TamableAnimal {
    protected FamiliarEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.7F)
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.7F)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Getter
    FamiliarData familiarData;
    ResourceLocation dataKey;

    public void setFamiliarData(ResourceLocation dataKey) {
        Level level = level();

        level.registryAccess().registry(TiamatDatapacks.FAMILIAR_DATA).ifPresent(reg -> {
            familiarData = reg.get(dataKey);

            if (familiarData == null) {
                log.error("can't found data for name: " + dataKey);
                return;
            }

            updateForData();
        });
    }

    private void updateForData() {
        switch (familiarData.familiarType()) {
            case FLIGHT:
                this.moveControl = new FlyingMoveControl(this, 20, true);
                this.navigation = new FlyingPathNavigation(this, level());
                break;
            case EQUIPMENT:
                // TODO
                break;
            case GROUND:
            default:
                break;
        }

        this.goalSelector.removeAllGoals(g -> true);

        this.goalSelector.addGoal(6, new FollowOwnerGoal(
                this, 1.0, 3.0F, 2.0F,
                familiarData.familiarType() == FamiliarData.FamiliarType.FLIGHT));
    }

    protected static final EntityDataAccessor<String> FAMILIAR_DATA =
            SynchedEntityData.defineId(FamiliarEntity.class, EntityDataSerializers.STRING);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        entityData.define(FAMILIAR_DATA, "");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        pCompound.putString("dataKey", dataKey.toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("dataKey")) {
            dataKey = new ResourceLocation(pCompound.getString("dataKey"));
            setFamiliarData(dataKey);
        }
    }

    private static final AABB aabb = new AABB(0, 0, 0, 0, 0, 0);

    @Override
    protected AABB getAttackBoundingBox() {
        return aabb;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        // TODO test code
        Player nearestPlayer = level().getNearestPlayer(this, 10);
        if (nearestPlayer != null)
            setOwnerUUID(nearestPlayer.getUUID());
        // TODO


        boolean isClientSide = level().isClientSide;

        String syncDataKey = entityData.get(FAMILIAR_DATA);

        if (!isClientSide && dataKey != null && !dataKey.toString().equals(syncDataKey)) {
            entityData.set(FAMILIAR_DATA, dataKey.toString());
        }

        if (isClientSide && !syncDataKey.isBlank() && (dataKey == null || !syncDataKey.equals(dataKey.toString()))) {
            dataKey = new ResourceLocation(syncDataKey);
            setFamiliarData(dataKey);
        }
    }

    @Override
    public void load(CompoundTag pCompound) {
        super.load(pCompound);

        if (pCompound.contains("DataKey")) {
            dataKey = new ResourceLocation(pCompound.getString("DataKey"));
            setFamiliarData(dataKey);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }
}
