package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.AbstractEntityBucketHandler;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.EntityBucketHandlerCod;
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
public class EntityCod extends AbstractGroupFish
{
    public static final ResourceLocation LOOT = new ResourceLocation(Subaquatic.MODID, "entities/cod");

    public EntityCod(@Nonnull World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.3f);
    }

    @Nonnull
    @Override
    protected ResourceLocation getLootTable() { return LOOT; }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() { return SubaquaticSounds.ENTITY_COD_DEATH; }

    @Nonnull
    @Override
    protected SoundEvent getFlopSound() { return SubaquaticSounds.ENTITY_COD_FLOP; }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) { return SubaquaticSounds.ENTITY_COD_HURT; }

    @Nonnull
    @Override
    public AbstractEntityBucketHandler createFishBucketHandler() {
        if(getClass() != EntityCod.class)
            throw new IllegalStateException("No bucket handler defined for entity class: " + getClass());

        return new EntityBucketHandlerCod();
    }
}
