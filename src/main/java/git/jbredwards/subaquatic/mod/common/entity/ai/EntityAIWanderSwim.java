package git.jbredwards.subaquatic.mod.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityAIWanderSwim extends EntityAIWander
{
    public EntityAIWanderSwim(@Nonnull EntityCreature creatureIn, double speedIn) { super(creatureIn, speedIn); }
    public EntityAIWanderSwim(@Nonnull EntityCreature creatureIn, double speedIn, int chance) {
        super(creatureIn, speedIn, chance);
    }

    @Nullable
    @Override
    protected Vec3d getPosition() {
        @Nullable Vec3d pos = RandomPositionGenerator.findRandomTarget(entity, 10, 7);
        for(int i = 0; pos != null && !RandomPositionGenerator.isWaterDestination(new BlockPos(pos), entity) && i < 10; i++)
            pos = RandomPositionGenerator.findRandomTarget(entity, 10, 7);

        return pos;
    }
}
