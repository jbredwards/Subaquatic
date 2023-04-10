package git.jbredwards.subaquatic.mod.common.entity.item;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartContainerPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.walkers.Filtered;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public abstract class AbstractBoatContainer extends EntityBoat implements IEntityMultiPart
{
    @Nonnull
    private static final DataParameter<ItemStack> CONTAINER_STACK = EntityDataManager.createKey(AbstractBoatContainer.class, DataSerializers.ITEM_STACK);
    public final MultiPartContainerPart containerPart = createContainerPart();

    public AbstractBoatContainer(@Nonnull World worldIn) { super(worldIn); }
    public AbstractBoatContainer(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    public static void registerFixer(@Nonnull DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new Filtered(AbstractBoatContainer.class) {
            @Nonnull
            @Override
            public NBTTagCompound filteredProcess(@Nonnull IDataFixer fixer, @Nonnull NBTTagCompound compound, int versionIn) {
                return fixer.process(FixTypes.ENTITY, compound.getCompoundTag("ContainerNBT"), versionIn);
            }
        });
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CONTAINER_STACK, ItemStack.EMPTY);
    }

    @Nonnull
    protected abstract MultiPartContainerPart createContainerPart();

    @Override
    protected boolean canFitPassenger(@Nonnull Entity passenger) { return getPassengers().size() == 0; }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        final Vec3d offset = new Vec3d(0.2, 0, 0).rotateYaw(-rotationYaw * 0.0175f - (float)Math.PI / 2);
        passenger.setPosition(posX + offset.x, posY + (isDead ? 0.01 : getMountedYOffset() + passenger.getYOffset()), posZ + offset.z);

        //handle smooth rotation
        passenger.rotationYaw += deltaRotation;
        passenger.setRotationYawHead(passenger.getRotationYawHead() + deltaRotation);
        applyYawToEntity(passenger);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        containerPart.onUpdate();

        final Vec3d offset = containerPart.getContainerOffset(true);
        containerPart.setLocationAndAngles(posX + offset.x, posY + offset.y, posZ + offset.z, rotationYaw, rotationPitch);
    }

    @Nullable
    @Override
    public Entity changeDimension(int dimensionIn, @Nonnull ITeleporter teleporter) {
        final Entity newEntity = super.changeDimension(dimensionIn, teleporter);
        if(newEntity instanceof AbstractBoatContainer) {
            ((AbstractBoatContainer)newEntity).containerPart.onDimensionChanged();
            ((AbstractBoatContainer)newEntity).containerPart.dimension = newEntity.dimension;
            return newEntity;
        }

        return null;
    }

    @Nonnull
    @Override
    public Entity[] getParts() { return new Entity[] { containerPart }; }

    @Nonnull
    @Override
    public World getWorld() { return world; }

    @Override
    public void applyEntityCollision(@Nonnull Entity entityIn) {
        if(entityIn != containerPart) super.applyEntityCollision(entityIn);
    }

    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float damage) {
        return attackEntityFrom(source, damage);
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if(isDead || world.isRemote || isEntityInvulnerable(source)) return false;
        else if(isPassenger(source.getTrueSource())) return false;

        final float damageTaken = getDamageTaken() + amount * 10;
        setForwardDirection(-getForwardDirection());
        setDamageTaken(damageTaken);
        setTimeSinceHit(10);
        markVelocityChanged();

        final boolean isCreative = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)source.getTrueSource()).isCreative();
        if(isCreative || damageTaken > 40) {
            if(!isCreative && world.getGameRules().getBoolean("doEntityDrops")) entityDropItem(getContainerStack(), 0);
            setDead();
        }

        return true;
    }

    @Override
    public void setDead() {
        super.setDead();
        containerPart.setDead();
    }

    @Nonnull
    @Override
    public ItemStack getPickedResult(@Nonnull RayTraceResult target) { return getContainerStack(); }

    @Nonnull
    public ItemStack getContainerStack() { return dataManager.get(CONTAINER_STACK); }
    public void setContainerStack(@Nonnull ItemStack stack) { dataManager.set(CONTAINER_STACK, stack); }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        final NBTTagCompound containerNBT = new NBTTagCompound();
        containerNBT.setString("id", String.format("%s:multipart_%s", Subaquatic.MODID, containerPart.getFixType()));

        compound.setTag("ContainerNBT", containerPart.writeToNBT(containerNBT));
        compound.setTag("ContainerStack", getContainerStack().serializeNBT());
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        if(compound.hasKey("ContainerStack", Constants.NBT.TAG_COMPOUND))
            setContainerStack(new ItemStack(compound.getCompoundTag("ContainerStack")));

        if(compound.hasKey("ContainerNBT", Constants.NBT.TAG_COMPOUND))
            containerPart.deserializeNBT(compound.getCompoundTag("ContainerNBT"));
    }
}
