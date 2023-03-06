package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.common.entity.ai.EntityAIFishSwimGroup;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 *
 * @author jbred
 *
 */
public abstract class AbstractGroupFish extends AbstractFish
{
    protected AbstractGroupFish groupLeader;
    protected int groupSize = 1;

    public AbstractGroupFish(@Nonnull World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(5, new EntityAIFishSwimGroup(this));
    }

    @Override
    public int getMaxSpawnedInChunk() { return getMaxGroupSize(); }
    public int getMaxGroupSize() { return super.getMaxSpawnedInChunk(); }

    @Override
    public boolean hasNoGroup() { return !hasGroupLeader(); }
    public boolean hasGroupLeader() { return groupLeader != null && groupLeader.isEntityAlive(); }

    public void addToGroupLeader(@Nonnull AbstractGroupFish leader) {
        groupLeader = leader;
        leader.groupSize++;
    }

    public void separateFromGroupLeader() {
        groupLeader.groupSize--;
        groupLeader = null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(isGroupLeader() && !world.isRemote && rand.nextInt(200) == 1)
            if(world.getEntitiesWithinAABB(getClass(), getEntityBoundingBox().grow(8)).size() <= 1)
                groupSize = 1;
    }

    public boolean isGroupLeader() { return groupSize > 1; }
    public boolean canAddToGroup() { return isGroupLeader() && groupSize < getMaxGroupSize(); }

    public boolean isWithinLeader() { return getDistanceSq(groupLeader) <= 121; }
    public void followLeader() { if(hasGroupLeader()) navigator.tryMoveToEntityLiving(groupLeader, 1); }

    public void addFishToGroup(@Nonnull Stream<AbstractGroupFish> stream) {
        stream.limit(getMaxGroupSize() - groupSize).filter(entity -> entity != this).forEach(entity -> entity.addToGroupLeader(this));
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        super.onInitialSpawn(difficulty, livingdata);
        if(!(livingdata instanceof GroupData)) return new GroupData(this);

        addToGroupLeader(((GroupData)livingdata).groupLeader);
        return livingdata;
    }

    public static class GroupData implements IEntityLivingData
    {
        @Nonnull
        public final AbstractGroupFish groupLeader;
        public GroupData(@Nonnull AbstractGroupFish fishIn) { groupLeader = fishIn; }
    }
}
