package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntitySalmon extends AbstractGroupFish
{
    public EntitySalmon(@Nonnull World worldIn) {
        super(worldIn);
        setSize(0.7f, 0.4f);
    }

    @Override
    public int getMaxSpawnedInChunk() { return 5; }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return super.getLootTable();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        return super.getHurtSound(damageSourceIn);
    }

    @Nonnull
    @Override
    public SoundEvent getFlopSound() {
        return null;
    }

    @Nonnull
    @Override
    public FishBucketData getBucketData() {
        return null;
    }
}
