package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.common.entity.ai.EntitySwimLookHelper;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntitySwimMoveHelper;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.AbstractEntityBucketHandler;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.EntityBucketHandlerTadpole;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityTadpole extends AbstractFish
{
    protected int age;
    public EntityTadpole(@Nonnull World worldIn) {
        super(worldIn);
        moveHelper = new EntitySwimMoveHelper(this, 85, 10, 0.02f, 0.1f, true);
        lookHelper = new EntitySwimLookHelper(this, 10);
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull World worldIn) { return new PathNavigateSwimmer(this, worldIn); }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        //if(!world.isRemote) setAgeAndGrow(age + 1);
    }

    @Nonnull
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Nonnull
    @Override
    public SoundEvent getBucketFillSound() {
        return super.getBucketFillSound();
    }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Nonnull
    @Override
    protected SoundEvent getFlopSound() {
        return null;
    }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        return super.getHurtSound(damageSourceIn);
    }

    @Nonnull
    @Override
    public AbstractEntityBucketHandler createFishBucketHandler() {
        if(getClass() != EntityTadpole.class)
            throw new IllegalStateException("No bucket handler defined for entity class: " + getClass());

        return new EntityBucketHandlerTadpole();
    }
}
