package git.jbredwards.subaquatic.mod.common.entity.living;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public abstract class EntityWaterCreature extends EntityCreature implements IAnimals
{
    public EntityWaterCreature(World worldIn) { super(worldIn); }

    @Override
    public boolean canBreatheUnderwater() { return true; }

    @Override
    public boolean getCanSpawnHere() { return true; }

    @Override
    public boolean isNotColliding() { return world.checkNoEntityCollision(getEntityBoundingBox(), this); }

    @Override
    public int getTalkInterval() { return 120; }

    @Override
    protected int getExperiencePoints(@Nonnull EntityPlayer player) { return 1 + world.rand.nextInt(3); }

    @Override
    public void onEntityUpdate() {
        final int air = getAir();
        super.onEntityUpdate();

        if(isEntityAlive() && !isInWater()) {
            setAir(air - 1);
            if(getAir() == -20) {
                setAir(0);
                attackEntityFrom(DamageSource.DROWN, 2);
            }
        }

        else setAir(300);
    }

    @Override
    public boolean isPushedByWater() { return false; }

    @Override
    public boolean canBeLeashedTo(@Nonnull EntityPlayer player) { return false; }
}
