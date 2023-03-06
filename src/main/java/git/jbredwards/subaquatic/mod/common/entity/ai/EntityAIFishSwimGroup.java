package git.jbredwards.subaquatic.mod.common.entity.ai;

import git.jbredwards.subaquatic.mod.common.entity.living.AbstractGroupFish;
import net.minecraft.entity.ai.EntityAIBase;

import javax.annotation.Nonnull;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public class EntityAIFishSwimGroup extends EntityAIBase
{
    @Nonnull
    protected final AbstractGroupFish fish;
    protected int navigateTimer;
    protected int followDelay;

    public EntityAIFishSwimGroup(@Nonnull AbstractGroupFish fishIn) {
        fish = fishIn;
        followDelay = getInitialFollowDelay();
    }

    protected int getInitialFollowDelay() { return 200 + fish.getRNG().nextInt(200) % 20; }

    @Override
    public boolean shouldExecute() {
        if(fish.isGroupLeader()) return false;
        else if(fish.hasGroupLeader()) return true;
        else if(followDelay > 0) {
            followDelay--;
            return false;
        }

        followDelay = getInitialFollowDelay();
        final List<AbstractGroupFish> nearbyFish = fish.world.getEntitiesWithinAABB(fish.getClass(), fish.getEntityBoundingBox().grow(8),
                entity -> entity.canAddToGroup() || entity.hasNoGroup());

        final AbstractGroupFish groupLeader = nearbyFish.stream().filter(AbstractGroupFish::canAddToGroup).findAny().orElse(fish);
        groupLeader.addFishToGroup(nearbyFish.stream().filter(AbstractGroupFish::hasNoGroup));
        return fish.hasGroupLeader();
    }

    @Override
    public boolean shouldContinueExecuting() { return fish.hasGroupLeader() && fish.isWithinLeader(); }

    @Override
    public void startExecuting() { navigateTimer = 0; }

    @Override
    public void resetTask() { fish.separateFromGroupLeader(); }

    @Override
    public void updateTask() {
        if(--navigateTimer <= 0) {
            navigateTimer = -Math.floorDiv(10, 2);
            fish.followLeader();
        }
    }
}
