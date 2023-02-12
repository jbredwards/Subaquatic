package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntityAIPuff;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityPufferfish extends AbstractFish
{
    @Nonnull
    private static final DataParameter<Integer> PUFF_STATE = EntityDataManager.createKey(EntityPufferfish.class, DataSerializers.VARINT);
    protected float originalWidth = -1;
    protected float originalHeight;
    public int deflateTimer;
    public int puffTimer;

    public EntityPufferfish(@Nonnull World worldIn) {
        super(worldIn);
        setSize(0.7f, 0.7f);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(1, new EntityAIPuff(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(PUFF_STATE, 0);
    }

    public int getPuffState() { return dataManager.get(PUFF_STATE); }
    public void setPuffState(int puffState) {
        dataManager.set(PUFF_STATE, puffState);
        onPuffStateChanged(puffState);
    }

    protected void onPuffStateChanged(int puffState) {
        float size = 1;
        if(puffState == 1) size = 0.7f;
        else if(puffState == 0) size = 0.5f;

        updateSize(size);
    }

    @Override
    protected void setSize(float width, float height) {
        final boolean initialized = originalWidth > 0;
        originalWidth = width;
        originalHeight = height;

        if(!initialized) updateSize(1);
    }

    protected void updateSize(float size) { super.setSize(originalWidth * size, originalHeight * size); }
    public boolean canAttackEntity(@Nonnull EntityLivingBase entity) {
        return (!(entity instanceof EntityWaterCreature || entity instanceof EntityWaterMob || entity instanceof EntityGuardian)
                || ((EntityLiving)entity).getAttackTarget() == this)
                && EntitySelectors.CAN_AI_TARGET.apply(entity);
    }

    @Override
    public void notifyDataManagerChange(@Nonnull DataParameter<?> key) {
        if(key == PUFF_STATE) onPuffStateChanged(getPuffState());
        super.notifyDataManagerChange(key);
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("PuffState", getPuffState());
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setPuffState(compound.getInteger("PuffState"));
    }

    @Override
    public void onUpdate() {
        if(!world.isRemote && isEntityAlive()) {
            final int puffState = getPuffState();
            if(puffTimer > 0) {
                puffTimer++;
                if(puffState == 0) {
                    playSound(SubaquaticSounds.ENTITY_PUFFERFISH_INFLATE, getSoundVolume(), getSoundPitch());
                    setPuffState(1);
                }

                else if(puffTimer > 40 && puffState == 1) {
                    playSound(SubaquaticSounds.ENTITY_PUFFERFISH_INFLATE, getSoundVolume(), getSoundPitch());
                    setPuffState(2);
                }
            }

            else if(puffState != 0) {
                ++deflateTimer;
                if(deflateTimer > 60 && puffState == 2) {
                    playSound(SubaquaticSounds.ENTITY_PUFFERFISH_DEFLATE, getSoundVolume(), getSoundPitch());
                    setPuffState(1);
                }

                else if(deflateTimer > 100 && puffState == 1) {
                    playSound(SubaquaticSounds.ENTITY_PUFFERFISH_DEFLATE, getSoundVolume(), getSoundPitch());
                    setPuffState(0);
                }
            }
        }

        super.onUpdate();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        final int puffState = getPuffState();
        if(puffState > 0) {
            for(EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(0.3), this::canAttackEntity)) {
                if(entity.isEntityAlive()) attack(entity, puffState);
            }
        }
    }

    protected void attack(@Nonnull EntityLivingBase entity, int puffState) {
        if(entity.attackEntityFrom(DamageSource.causeMobDamage(this), puffState + 1)) {
            entity.addPotionEffect(new PotionEffect(MobEffects.POISON, puffState * 60));
            playSound(SubaquaticSounds.ENTITY_PUFFERFISH_STING, 1, 1);
        }
    }

    @Override
    public void onCollideWithPlayer(@Nonnull EntityPlayer player) {
        if(player instanceof EntityPlayerMP && world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            final int puffState = getPuffState();
            if(puffState > 0 && player.attackEntityFrom(DamageSource.causeMobDamage(this), puffState + 1)) {
                player.addPotionEffect(new PotionEffect(MobEffects.POISON, puffState * 60));
                ((EntityPlayerMP)player).connection.sendPacket(new SPacketSoundEffect(SubaquaticSounds.ENTITY_PUFFERFISH_STING, SoundCategory.NEUTRAL, player.posX, player.posY, player.posZ, 1, 1));
            }
        }
    }

    @Override
    public void buildFishBucketData(@Nonnull FishBucketData data) {
        data.entity = SubaquaticEntities.PUFFERFISH;
        data.fishNbt = serializeNBT();
        data.fishNbt.setInteger("PuffState", 0);
    }

    @Nonnull
    @Override
    protected ResourceLocation getLootTable() { return new ResourceLocation(Subaquatic.MODID, "entities/pufferfish"); }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() { return SubaquaticSounds.ENTITY_PUFFERFISH_DEATH; }

    @Nonnull
    @Override
    protected SoundEvent getFlopSound() { return SubaquaticSounds.ENTITY_PUFFERFISH_FLOP; }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) { return SubaquaticSounds.ENTITY_PUFFERFISH_HURT; }

    @Override
    public boolean hasNoGroup() { return true; }
}
