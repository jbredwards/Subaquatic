/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.common.entity.ai.EntitySwimMoveHelper;
import git.jbredwards.subaquatic.mod.common.entity.util.FrogData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticDataSerializers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.OptionalInt;

/**
 *
 * @author jbred
 *
 */
public class EntityFrog extends EntityAnimal
{
    @Nonnull private static final DataParameter<OptionalInt> TONGUE_TARGET_ID = EntityDataManager.createKey(EntityFrog.class, SubaquaticDataSerializers.OPTIONAL_INT);
    @Nonnull private static final DataParameter<FrogData> FROG_DATA = EntityDataManager.createKey(EntityFrog.class, SubaquaticDataSerializers.FROG_DATA);

    public EntityFrog(@Nonnull World worldIn) {
        super(worldIn);
        setPathPriority(PathNodeType.WATER, 4);
        setPathPriority(PathNodeType.TRAPDOOR, -1);
        stepHeight = 1;
        moveHelper = new EntitySwimMoveHelper(this, 85, 10, 0.02f, 0.1f, true);
        lookHelper = new EntityLookHelper(this) {
            @Override
            public void onUpdateLook() {
                if(EntityFrog.this.getTongueTarget() == null) entity.rotationPitch = 0;
                if(isLooking) {
                    isLooking = false;
                    final double distX = posX - entity.posX;
                    final double distY = posY - (entity.posY + entity.getEyeHeight());
                    final double distZ = posZ - entity.posZ;

                    final float lookYaw = (float)(MathHelper.atan2(distZ, distX) * 180 / Math.PI) - 90;
                    final float lookPitch = (float)(-(MathHelper.atan2(distY, Math.sqrt(distX * distX + distZ * distZ)) * 180 / Math.PI));

                    entity.rotationYawHead = updateRotation(entity.rotationYawHead, lookYaw, deltaLookYaw);
                    entity.rotationPitch = updateRotation(entity.rotationPitch, lookPitch, deltaLookPitch);
                }

                else entity.rotationYawHead = updateRotation(entity.rotationYawHead, entity.renderYawOffset, 10);
                final float wrapped = MathHelper.wrapDegrees(entity.rotationYawHead - entity.renderYawOffset);
                if(!entity.getNavigator().noPath()){
                    if(wrapped < -75) entity.rotationYawHead = entity.renderYawOffset - 75;
                    if(wrapped > 75) entity.rotationYawHead = entity.renderYawOffset + 75;
                }
            }
        };
    }

    @Override
    protected void entityInit() {
        dataManager.register(TONGUE_TARGET_ID, OptionalInt.empty());
        dataManager.register(FROG_DATA, FrogData.VARIANTS.getFirst());
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull World worldIn) {
        return new PathNavigateSwimmer(this, worldIn) {
            @Nonnull
            @Override
            protected PathFinder getPathFinder() {
                return super.getPathFinder();
            }

            @Override
            protected boolean canNavigate() { return true; }

            @Override
            protected boolean isDirectPathBetweenPoints(@Nonnull Vec3d point1, @Nonnull Vec3d point2, int sizeX, int sizeY, int sizeZ) {
                return isInLiquid() && super.isDirectPathBetweenPoints(point1, point2, sizeX, sizeY, sizeZ);
            }

            @Override
            public boolean canEntityStandOnPos(@Nonnull BlockPos pos) { return world.getBlockState(pos.down()).isFullBlock(); }
        };
    }

    @Override
    public int getVerticalFaceSpeed() { return 35; }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) { return null; }

    @Override
    public boolean isChild() { return false; }

    @Override
    public void setGrowingAge(int age) {}

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {


        return livingdata;
    }

    @Nullable
    public Entity getTongueTarget() {
        final OptionalInt targetId = dataManager.get(TONGUE_TARGET_ID);
        return targetId.isPresent() ? world.getEntityByID(targetId.getAsInt()) : null;
    }

    public void setTongueTarget(@Nullable Entity target) {
        dataManager.set(TONGUE_TARGET_ID, target != null ? OptionalInt.of(target.getEntityId()) : OptionalInt.empty());
    }

    @Nonnull
    public FrogData getFrogData() { return dataManager.get(FROG_DATA); }
    public void setFrogData(@Nonnull FrogData data) { dataManager.set(FROG_DATA, data); }
}
