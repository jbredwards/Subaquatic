package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
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
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityTropicalFish.class, DataSerializers.VARINT);
    public EntityTropicalFish(@Nonnull World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.4f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(VARIANT, 0);
    }

    public int getVariant() { return dataManager.get(VARIANT); }
    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }
    public int getRandomVariant() {
        //generate random variant
        if(rand.nextFloat() < 0.1) return rand.nextInt(2) | rand.nextInt(6) << 8 | rand.nextInt(15) << 16 | rand.nextInt(15) << 24;
        //get from config
        return SubaquaticTropicalFishConfig.DEFAULT_TYPES.isEmpty() ? 0 : SubaquaticTropicalFishConfig.DEFAULT_TYPES
                .get(rand.nextInt(SubaquaticTropicalFishConfig.DEFAULT_TYPES.size()));
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", getVariant());
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
                    EnumDyeColor.byMetadata(variantData.getInteger("SecondaryColor"))
            ).serialize());
        }

        else setVariant(compound.getInteger("Variant"));
    }

    @Override
    public void buildFishBucketData(@Nonnull FishBucketData data) {
        data.entity = SubaquaticEntities.TROPICAL_FISH;
        data.fishNbt = serializeNBT();
    }

    @Override
    public void onCreatedByBucket(@Nonnull ItemStack bucket, @Nonnull FishBucketData data) {
        if(!data.fishNbt.hasKey("Variant")) setVariant(getRandomVariant());
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
        int tropicalFishData;

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
        public final int data;
        public GroupData(@Nonnull AbstractGroupFish fishIn, int dataIn) {
            super(fishIn);
            data = dataIn;
        }
    }
}
