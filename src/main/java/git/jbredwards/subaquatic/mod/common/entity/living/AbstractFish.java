package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntityAIFishSwim;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntityFishMoveHelper;
import git.jbredwards.subaquatic.mod.common.entity.ai.PathNavigateFish;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public abstract class AbstractFish extends EntityWaterCreature
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
        tasks.addTask(4, new EntityAIFishSwim(this, 1, 40));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3);
    }

    @Override
    public float getEyeHeight() { return height * 0.5f; }

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

    public boolean isFromBucket() { return dataManager.get(FROM_BUCKET); }
    public void setFromBucket(boolean isFromBucket) { dataManager.set(FROM_BUCKET, isFromBucket); }

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
        if(!isInWater() && onGround && collidedVertically) {
            motionY += 0.4;
            motionX += 0.05 * (rand.nextFloat() * 2 - 1);
            motionZ += 0.05 * (rand.nextFloat() * 2 - 1);

            onGround = false;
            isAirBorne = true;
            playSound(getFlopSound(), getSoundVolume(), getSoundPitch());
        }

        super.onLivingUpdate();
    }

    @Override
    protected boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        final ItemStack held = player.getHeldItem(hand);
        if(isEntityAlive() && IFishBucket.canStackHoldFish(held)) {
            final ItemStack fishBucket = ItemHandlerHelper.copyStackWithSize(held, 1);
            final IFishBucket cap = IFishBucket.get(fishBucket);
            if(cap != null && cap.getData() == FishBucketData.EMPTY) {
                cap.setData(getBucketData());

                if(hasCustomName()) fishBucket.setStackDisplayName(getCustomNameTag());
                ItemHandlerHelper.giveItemToPlayer(player, fishBucket);

                playSound(SubaquaticSounds.BUCKET_FILL_FISH, 1, 1);
                if(!player.isCreative()) held.shrink(1);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    @Nonnull
    @Override
    protected SoundEvent getSwimSound() { return SubaquaticSounds.ENTITY_FISH_SWIM; }

    @Nonnull
    protected abstract SoundEvent getFlopSound();

    @Nonnull
    public abstract FishBucketData getBucketData();

    public abstract boolean hasNoGroup();
}
