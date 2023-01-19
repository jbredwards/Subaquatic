package git.jbredwards.subaquatic.mod.common.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityBoat;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author jbred
 *
 */
public final class EntityBoatContainer extends EntityBoat implements IEntityMultiPart
{
    @Nonnull
    private static final DataParameter<ItemStack> CONTAINER_STACK = EntityDataManager.createKey(EntityBoatContainer.class, DataSerializers.ITEM_STACK);
    public MultiPartContainerPart containerPart;

    public EntityBoatContainer(@Nonnull World worldIn) { super(worldIn); }
    public EntityBoatContainer(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    public static void registerFixer(@Nonnull DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new Filtered(EntityBoatContainer.class) {
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

    @Override
    protected boolean canFitPassenger(@Nonnull Entity passenger) { return getPassengers().size() == 0; }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        final Vec3d offset = (new Vec3d(0.2, 0, 0)).rotateYaw(-rotationYaw * 0.0175f - (float)Math.PI / 2);
        passenger.setPosition(posX + offset.x, posY + (isDead ? 0.01 : getMountedYOffset() + passenger.getYOffset()), posZ + offset.z);
    }

    @Nullable
    @Override
    public Entity changeDimension(int dimensionIn, @Nonnull ITeleporter teleporter) {
        final Entity newEntity = super.changeDimension(dimensionIn, teleporter);
        if(newEntity instanceof EntityBoatContainer) {
            ((EntityBoatContainer)newEntity).containerPart.onDimensionChanged();
            ((EntityBoatContainer)newEntity).containerPart.dimension = newEntity.dimension;
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
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float damage) {
        return attackEntityFrom(source, damage);
    }

    @Nonnull
    public ItemStack getContainerStack() { return dataManager.get(CONTAINER_STACK); }
    public void setContainerStack(@Nonnull ItemStack stack) { dataManager.set(CONTAINER_STACK, stack); }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        final NBTTagCompound containerNBT = new NBTTagCompound();
        containerNBT.setString("ContainerName", containerPart.partName);
        containerNBT.setString("ContainerType", containerPart.getClass().getName());
        containerPart.writeToNBT(containerNBT);

        compound.setTag("ContainerNBT", containerNBT);
        compound.setTag("ContainerStack", getContainerStack().serializeNBT());
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        if(compound.hasKey("ContainerStack", Constants.NBT.TAG_COMPOUND)) {
            setContainerStack(new ItemStack(compound.getCompoundTag("ContainerStack")));
        }

        if(compound.hasKey("ContainerNBT", Constants.NBT.TAG_COMPOUND)) {
            final NBTTagCompound containerNBT = compound.getCompoundTag("ContainerNBT");
            if(containerNBT.hasKey("ContainerType", Constants.NBT.TAG_STRING)) {
                try {
                    containerPart = (MultiPartContainerPart)Class
                            .forName(containerNBT.getString("ContainerType"))
                            .getConstructor(IEntityMultiPart.class, String.class, float.class, float.class)
                            .newInstance(this, containerNBT.getString("ContainerName"), 1, 1);


                    containerPart.deserializeNBT(containerNBT);
                }
                //should never pass
                catch (ClassNotFoundException |
                       ClassCastException |
                       NoSuchMethodException |
                       NullPointerException |
                       InvocationTargetException |
                       InstantiationException |
                       IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
