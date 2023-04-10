package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntityAIFishSwim;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntityFishMoveHelper;
import git.jbredwards.subaquatic.mod.common.entity.ai.PathNavigateFish;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.IBucketableEntity;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public abstract class AbstractFish extends EntityWaterCreature implements IBucketableEntity
{
    @Nonnull
    private static final DataParameter<Boolean> FROM_BUCKET = EntityDataManager.createKey(AbstractFish.class, DataSerializers.BOOLEAN);

    public AbstractFish(World worldIn) {
        super(worldIn);
        moveHelper = new EntityFishMoveHelper(this);
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull World worldIn) { return new PathNavigateFish(this, worldIn); }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(FROM_BUCKET, false);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAIPanic(this, 1.25));
        tasks.addTask(2, new EntityAIAvoidEntity<>(this, EntityPlayer.class, EntitySelectors.NOT_SPECTATING, 8, 1.6, 1.4));
        tasks.addTask(4, new EntityAIFishSwim(this, 1, 10));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3);
    }

    @Override
    public float getEyeHeight() { return height * 0.65f; }

    @Override
    public boolean isNoDespawnRequired() { return isFromBucket() || super.isNoDespawnRequired(); }

    @Override
    protected boolean canDespawn() { return !isFromBucket() && !hasCustomName(); }

    @Override
    public int getMaxSpawnedInChunk() { return 8; }

    @Override
    public boolean getCanSpawnHere() {
        final BlockPos pos = new BlockPos(this);
        return super.getCanSpawnHere()
                && FluidloggedUtils.getFluidOrReal(world, pos).getMaterial() == Material.WATER
                && FluidloggedUtils.getFluidOrReal(world, pos.up()).getMaterial() == Material.WATER;
    }

    @Override
    public boolean isFromBucket() { return dataManager.get(FROM_BUCKET); }

    @Override
    public void setFromBucket(boolean fromBucket) { dataManager.set(FROM_BUCKET, fromBucket); }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("FromBucket", isFromBucket());
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(isServerWorld() && isInWater()) {
            moveRelative(strafe, vertical, forward, 0.01f);
            move(MoverType.SELF, motionX, motionY, motionZ);

            motionX *= 0.9;
            motionY *= 0.9;
            motionZ *= 0.9;

            if(getAttackTarget() == null) motionY -= 0.005;
        }

        else super.travel(strafe, vertical, forward);
    }

    @Override
    public void onLivingUpdate() {
        if(!isDead && !isInWater() && onGround && collidedVertically) {
            motionX += 0.05 * (rand.nextFloat() * 2 - 1);
            motionY += 0.4;
            motionZ += 0.05 * (rand.nextFloat() * 2 - 1);

            onGround = false;
            isAirBorne = true;
            playSound(getFlopSound(), getSoundVolume(), getSoundPitch());
        }

        super.onLivingUpdate();
    }

    @Override
    protected boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        return tryCaptureEntity(player, hand) || super.processInteract(player, hand);
    }

    @Nonnull
    @Override
    protected SoundEvent getSwimSound() { return SubaquaticSounds.ENTITY_FISH_SWIM; }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return null; }

    @Nonnull
    protected abstract SoundEvent getFlopSound();
    public abstract boolean hasNoGroup();

    @Override
    public boolean canBucket() { return true; }
}
