package git.jbredwards.subaquatic.mod.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class PathNavigateFish extends PathNavigateSwimmer
{
    public PathNavigateFish(@Nonnull EntityLiving entity, @Nonnull World worldIn) {
        super(entity, worldIn);
    }

    @Override
    public void onUpdateNavigation() {
        ++totalTicks;
        if(tryUpdatePath) updatePath();

        if(!noPath()) {
            if(canNavigate()) pathFollow();
            else if(currentPath != null && currentPath.getCurrentPathIndex() < currentPath.getCurrentPathLength()) {
                final Vec3d pos = currentPath.getVectorFromIndex(entity, currentPath.getCurrentPathIndex());
                if(MathHelper.floor(entity.posX) == MathHelper.floor(pos.x) && MathHelper.floor(entity.posY) == MathHelper.floor(pos.y) && MathHelper.floor(entity.posZ) == MathHelper.floor(pos.z))
                    currentPath.incrementPathIndex();
            }

            debugPathFinding();
            if(!noPath() && currentPath != null) {
                final Vec3d pos = currentPath.getPosition(entity);
                entity.getMoveHelper().setMoveTo(pos.x, pos.y, pos.z, speed);
            }
        }
    }

    @Override
    protected void pathFollow() {
        if(currentPath != null) {
            double entityWidth = entity.width > 0.75 ? entity.width / 2 : 0.75 - entity.width / 2;
            if(Math.abs(entity.motionX) > 0.2 || Math.abs(entity.motionZ) > 0.2)
                entityWidth *= Math.sqrt(entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ) * 6;

            Vec3d pos = currentPath.getCurrentPos();
            if(Math.abs(entity.posX - (pos.x + 0.5)) < entityWidth && Math.abs(entity.posZ - (pos.z + 0.5)) < entityWidth && Math.abs(entity.posY - pos.y) < entityWidth * 2)
                currentPath.incrementPathIndex();

            final Vec3d entityPos = getEntityPosition();
            for(int j = Math.min(currentPath.getCurrentPathIndex() + 6, currentPath.getCurrentPathLength() - 1); j > currentPath.getCurrentPathIndex(); --j) {
                pos = currentPath.getVectorFromIndex(entity, j);

                if(!(pos.squareDistanceTo(entityPos) > 36) && isDirectPathBetweenPoints(entityPos, pos, 0, 0, 0)) {
                    currentPath.setCurrentPathIndex(j);
                    break;
                }
            }

            checkForStuck(entityPos);
        }
    }

    @Override
    protected void checkForStuck(@Nonnull Vec3d entityPos) {
        if(totalTicks - ticksAtLastPos > 100) {
            if(entityPos.squareDistanceTo(lastPosCheck) < 2.25)
                clearPath();

            ticksAtLastPos = totalTicks;
            lastPosCheck = entityPos;
        }

        if(currentPath != null && !currentPath.isFinished()) {
            final Vec3d pos = currentPath.getCurrentPos();
            if(pos.equals(timeoutCachedNode)) timeoutTimer += System.currentTimeMillis() - lastTimeoutCheck;

            else {
                timeoutCachedNode = pos;
                timeoutLimit = entity.getAIMoveSpeed() > 0 ? entityPos.distanceTo(timeoutCachedNode) / entity.getAIMoveSpeed() * 100 : 0;
            }

            if(timeoutLimit > 0 && timeoutTimer > timeoutLimit * 3) {
                timeoutCachedNode = Vec3d.ZERO;
                timeoutTimer = 0;
                timeoutLimit = 0;
                clearPath();
            }

            lastTimeoutCheck = System.currentTimeMillis();
        }
    }
}
