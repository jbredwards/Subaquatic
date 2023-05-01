package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.AbstractEntityBucketHandler;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.EntityBucketHandlerTropicalFish;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticDataSerializers;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityTropicalFish extends AbstractGroupFish
{
    @Nonnull
    private static final DataParameter<TropicalFishData> VARIANT = EntityDataManager.createKey(EntityTropicalFish.class, SubaquaticDataSerializers.TROPICAL_FISH_DATA);
    public EntityTropicalFish(@Nonnull World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.4f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(VARIANT, TropicalFishData.DEFAULT);
    }

    @Nonnull
    public TropicalFishData getVariant() { return dataManager.get(VARIANT); }
    public void setVariant(@Nonnull TropicalFishData variant) { dataManager.set(VARIANT, variant); }

    @Nonnull
    public TropicalFishData getRandomVariant() {
        //generate random variant
        if(rand.nextFloat() < 0.1) return TropicalFishData.deserialize(rand.nextInt(2) | rand.nextInt(6) << 8 | rand.nextInt(15) << 16 | rand.nextInt(15) << 24);
        //get from config
        return SubaquaticTropicalFishConfig.DEFAULT_TYPES.isEmpty() ? TropicalFishData.DEFAULT
                : SubaquaticTropicalFishConfig.DEFAULT_TYPES.get(rand.nextInt(SubaquaticTropicalFishConfig.DEFAULT_TYPES.size()));
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", getVariant().serialize());
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        //used to debug fish types
        if(compound.hasKey("VariantData", Constants.NBT.TAG_COMPOUND)) {
            final NBTTagCompound variantData = compound.getCompoundTag("VariantData");
            setVariant(new TropicalFishData(
                    variantData.getInteger("PrimaryShape"),
                    EnumDyeColor.byMetadata(variantData.getInteger("PrimaryColor")),
                    variantData.getInteger("SecondaryShape"),
                    EnumDyeColor.byMetadata(variantData.getInteger("SecondaryColor"))));
        }

        else setVariant(TropicalFishData.deserialize(compound.getInteger("Variant")));
    }

    @Nonnull
    @Override
    public AbstractEntityBucketHandler createFishBucketHandler() {
        if(getClass() != EntityTropicalFish.class)
            throw new IllegalStateException("No bucket handler defined for entity class: " + getClass());

        return new EntityBucketHandlerTropicalFish();
    }

    @Override
    public void postSetHandlerEntityNBT(@Nonnull AbstractEntityBucketHandler handler) {
        ((EntityBucketHandlerTropicalFish)handler).fishData = getVariant();
    }

    @Override
    public void onCreatedByBucket(@Nonnull ItemStack bucket, @Nonnull AbstractEntityBucketHandler handler) {
        final TropicalFishData bucketData = ((EntityBucketHandlerTropicalFish)handler).fishData;
        setVariant(bucketData == null ? getRandomVariant() : bucketData);
    }

    @Nonnull
    @Override
    protected ResourceLocation getLootTable() { return new ResourceLocation(Subaquatic.MODID, "entities/tropical_fish"); }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() { return SubaquaticSounds.ENTITY_TROPICAL_FISH_DEATH; }

    @Nonnull
    @Override
    protected SoundEvent getFlopSound() { return SubaquaticSounds.ENTITY_TROPICAL_FISH_FLOP; }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) { return SubaquaticSounds.ENTITY_TROPICAL_FISH_HURT; }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        TropicalFishData tropicalFishData;

        if(livingdata instanceof GroupData) tropicalFishData = ((GroupData)livingdata).data;
        else {
            tropicalFishData = getRandomVariant();
            livingdata = new GroupData(this, tropicalFishData);
        }

        setVariant(tropicalFishData);
        return livingdata;
    }

    public static class GroupData extends AbstractGroupFish.GroupData
    {
        @Nonnull
        public final TropicalFishData data;
        public GroupData(@Nonnull AbstractGroupFish fishIn, @Nonnull TropicalFishData dataIn) {
            super(fishIn);
            data = dataIn;
        }
    }
}
