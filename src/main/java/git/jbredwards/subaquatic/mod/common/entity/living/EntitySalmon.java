package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

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

    @Nonnull
    @Override
    protected ResourceLocation getLootTable() { return new ResourceLocation(Subaquatic.MODID, "entities/salmon"); }

    @Nonnull
    @Override
    protected SoundEvent getAmbientSound() { return SubaquaticSounds.ENTITY_SALMON_AMBIENT; }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() { return SubaquaticSounds.ENTITY_SALMON_DEATH; }

    @Nonnull
    @Override
    protected SoundEvent getFlopSound() { return SubaquaticSounds.ENTITY_SALMON_FLOP; }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) { return SubaquaticSounds.ENTITY_SALMON_HURT; }

    @Nonnull
    @Override
    public FishBucketData getBucketData() {
        return null;
    }
}
