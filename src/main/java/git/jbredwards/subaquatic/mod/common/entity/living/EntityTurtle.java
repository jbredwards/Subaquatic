/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.living;

import com.google.common.base.Predicate;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.block.BlockTurtleEgg;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.entity.ai.task.*;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntityTurtleMoveHelper;
import git.jbredwards.subaquatic.mod.common.entity.ai.pathfinding.PathNavigateTurtle;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticLootTables;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityTurtle extends EntityAnimal implements IAnimals {
    @SuppressWarnings("Guava")
    @Nonnull public static final Predicate<EntityTurtle> CAN_AI_TARGET = turtle -> !turtle.isInWater() && turtle.isChild() && !turtle.isInvisible();
    @Nonnull protected static final DataParameter<Byte> TURTLE_FLAGS = EntityDataManager.createKey(EntityTurtle.class, DataSerializers.BYTE);

    @Nonnull public BlockPos homePos = BlockPos.ORIGIN;
    @Nonnull public BlockPos travelPos = BlockPos.ORIGIN;

    public int sandDiggingCounter;
    protected float turtleNextStepDistance;

    public EntityTurtle(@Nonnull final World worldIn) {
        super(worldIn);
        setSize(1.2f, 0.4f);
        moveHelper = new EntityTurtleMoveHelper(this);
        spawnableBlock = Blocks.SAND;
        stepHeight = 1;

        setPathPriority(PathNodeType.WATER, 0);
        setPathPriority(PathNodeType.DOOR_IRON_CLOSED, -1);
        setPathPriority(PathNodeType.DOOR_WOOD_CLOSED, -1);
        setPathPriority(PathNodeType.DOOR_OPEN, -1);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(TURTLE_FLAGS, (byte)0);
    }

    public byte getTurtleFlags() { return dataManager.get(TURTLE_FLAGS); }
    public void setTurtleFlags(final byte flags) { dataManager.set(TURTLE_FLAGS, flags); }

    public boolean hasEgg() { return (getTurtleFlags() & 1) != 0; }
    public void setHasEgg(final boolean flag) { setTurtleFlags((byte)(flag ? getTurtleFlags() | 1 : getTurtleFlags() &~ 1)); }

    public boolean isGoingHome() { return (getTurtleFlags() & 2) != 0; }
    public void setGoingHome(final boolean flag) { setTurtleFlags((byte)(flag ? getTurtleFlags() | 2 : getTurtleFlags() &~ 2)); }

    public boolean isTravelling() { return (getTurtleFlags() & 4) != 0; }
    public void setTravelling(final boolean flag) { setTurtleFlags((byte)(flag ? getTurtleFlags() | 4 : getTurtleFlags() &~ 4)); }

    public boolean isDiggingSand() { return (getTurtleFlags() & 8) != 0; }
    public void setDiggingSand(final boolean flag) { setTurtleFlags((byte)(flag ? getTurtleFlags() | 8 : getTurtleFlags() &~ 8)); }

    @Override
    public void readEntityFromNBT(@Nonnull final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("HomePos", Constants.NBT.TAG_COMPOUND)) homePos = NBTUtil.getPosFromTag(compound.getCompoundTag("HomePos"));
        if(compound.hasKey("TravelPos", Constants.NBT.TAG_COMPOUND)) travelPos = NBTUtil.getPosFromTag(compound.getCompoundTag("TravelPos"));
        if(compound.hasKey("TurtleFlags", Constants.NBT.TAG_ANY_NUMERIC)) setTurtleFlags(compound.getByte("TurtleFlags"));
    }

    @Override
    public void writeEntityToNBT(@Nonnull final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("HomePos", NBTUtil.createPosTag(homePos));
        compound.setTag("TravelPos", NBTUtil.createPosTag(travelPos));
        compound.setByte("TurtleFlags", getTurtleFlags());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(homePos.equals(BlockPos.ORIGIN)) homePos = new BlockPos(this);
    }

    @Override
    public boolean getCanSpawnHere() {
        return (int)posY < world.getSeaLevel() + 4 && super.getCanSpawnHere();
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAITurtlePanic(this, 1.2));
        tasks.addTask(1, new EntityAITurtleMate(this, 1));
        tasks.addTask(1, new EntityAITurtleLayEgg(this, 1));
        tasks.addTask(2, new EntityAISwimTempt(this, 1.1));
        tasks.addTask(3, new EntityAITurtleWanderToWater(this, 1));
        tasks.addTask(4, new EntityAITurtleGoHome(this, 1));
        tasks.addTask(7, new EntityAITurtleTravel(this, 1));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8));
        tasks.addTask(9, new EntityAITurtleWanderToLand(this, 1, 100));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public boolean isPushedByWater() { return false; }

    @Override
    public boolean canBreatheUnderwater() { return true; }

    @Override
    public int getTalkInterval() { return 200; }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return !isInWater() && onGround && !isChild() ? SubaquaticSounds.ENTITY_TURTLE_AMBIENT : null;
    }

    @Nonnull
    @Override
    protected SoundEvent getSwimSound() {
        return SubaquaticSounds.ENTITY_TURTLE_SWIM;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return isChild() ? SubaquaticSounds.ENTITY_TURTLE_BABY_DEATH : SubaquaticSounds.ENTITY_TURTLE_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull final DamageSource damageSourceIn) {
        return isChild() ? SubaquaticSounds.ENTITY_TURTLE_BABY_HURT : SubaquaticSounds.ENTITY_TURTLE_HURT;
    }

    @Override
    protected void playStepSound(@Nonnull final BlockPos pos, @Nonnull final Block blockIn) {
        if(shouldPlayStepSound()) playSound(isChild() ? SubaquaticSounds.ENTITY_TURTLE_BABY_STEP : SubaquaticSounds.ENTITY_TURTLE_STEP, 0.15f, 1);
    }

    protected boolean shouldPlayStepSound() {
        nextStepDistance = (int)distanceWalkedOnStepModified - 1;
        if(distanceWalkedOnStepModified > turtleNextStepDistance) {
            turtleNextStepDistance = distanceWalkedOnStepModified + 0.15f;
            return true;
        }

        return false;
    }

    @Override
    public boolean processInteract(@Nonnull final EntityPlayer player, @Nonnull final EnumHand hand) {
        if(hasEgg()) {
            @Nonnull final ItemStack stack = player.getHeldItem(hand);
            if(!stack.isEmpty() && isBreedingItem(stack)) return false;
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isEntityAlive() && isDiggingSand() && sandDiggingCounter > 0 && sandDiggingCounter % 5 == 0) {
            @Nonnull final BlockPos pos = new BlockPos(this);
            @Nonnull final IBlockState below = world.getBlockState(pos.down());

            if(isSand(below)) world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, Block.getStateId(below));
        }
    }

    @Override
    protected void onGrowingAdult() {
        super.onGrowingAdult();
        if(canDropLoot() && world.getGameRules().getBoolean("doMobLoot")) entityDropItem(new ItemStack(SubaquaticItems.MATERIAL, 1, 1), 0);
    }

    @Override
    public void travel(final float strafe, final float vertical, final float forward) {
        if(isInWater() && isServerWorld()) {
            moveRelative(strafe, vertical, forward, 0.1f);
            move(MoverType.SELF, motionX, motionY, motionZ);

            motionX *= 0.9;
            motionY *= 0.9;
            motionZ *= 0.9;

            if(getAttackTarget() == null && (!isGoingHome() || !isWithinHomePos(400))) motionY -= 0.005;
            // if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ))
            // {
            //     this.motionY = 0.30000001192092896D;
            // }
        }

        else super.travel(strafe, vertical, forward);
    }

    @Override
    public float getBlockPathWeight(@Nonnull final BlockPos pos) {
        return !isGoingHome() && FluidloggedUtils.getFluidState(world, pos).getMaterial() == Material.WATER ? 10 : super.getBlockPathWeight(pos);
    }

    @Override
    public void setScaleForAge(final boolean child) {
        setScale(child ? 0.3f : 1);
    }

    @Override
    public boolean canBeLeashedTo(@Nonnull final EntityPlayer player) {
        return SubaquaticConfigHandler.Common.Entity.leashableTurtles;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return SubaquaticLootTables.ENTITIES_TURTLE;
    }

    @Override
    public void onStruckByLightning(@Nonnull final EntityLightningBolt lightningBolt) {
        damageEntity(DamageSource.LIGHTNING_BOLT, Float.MAX_VALUE);
    }

    @Override
    public boolean isBreedingItem(@Nonnull final ItemStack stack) {
        return ArrayUtils.contains(OreDictionary.getOreIDs(stack), OreDictionary.getOreID("cropSeagrass"));
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull final World worldIn) {
        return new PathNavigateTurtle(this, worldIn);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull final EntityAgeable ageable) {
        return new EntityTurtle(world);
    }

    public boolean isWithinHomePos(final double distance) {
        return getDistanceSqToCenter(homePos) <= distance;
    }

    public boolean isSand(@Nonnull final IBlockState state) {
        return BlockTurtleEgg.isSand(state);
    }
}
