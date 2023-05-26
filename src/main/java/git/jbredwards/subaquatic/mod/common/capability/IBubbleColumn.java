package git.jbredwards.subaquatic.mod.common.capability;

import com.google.common.collect.ImmutableMap;
import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity.PluginEntityBoat;
import git.jbredwards.subaquatic.mod.common.block.BlockBubbleColumn;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("ConstantConditions")
public interface IBubbleColumn extends INBTSerializable<NBTBase>
{
    @CapabilityInject(IBubbleColumn.class)
    @Nonnull Capability<IBubbleColumn> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "bubble_column");

    void onCollide(@Nonnull Entity entity, @Nonnull BlockBubbleColumn column);
    void onCollideTop(@Nonnull Entity entity, @Nonnull BlockBubbleColumn column);

    @Nullable
    static IBubbleColumn get(@Nullable ICapabilityProvider provider) {
        return provider.hasCapability(CAPABILITY, null) ? provider.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attach(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        final IBubbleColumn handler = getHandlerForClass(event.getObject().getClass()).get();
        if(handler != null) event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY, handler));
    }

    /**
     * Custom bubble column handlers for each entity should be registered here.
     * All subclasses are handled automatically as needed.
     */
    @Nonnull
    Map<Class<?>, Supplier<IBubbleColumn>> BUBBLE_COLUMN_FACTORY = new HashMap<>(ImmutableMap.of(Entity.class, Impl::new, EntityBoat.class, Boat::new, EntityPlayer.class, Player::new, FakePlayer.class, () -> null));
    static Supplier<IBubbleColumn> getHandlerForClass(@Nonnull Class<?> clazzIn) {
        if(clazzIn == Object.class) throw new IllegalArgumentException("Class has no bubble column handler, this should never happen!");
        return BUBBLE_COLUMN_FACTORY.computeIfAbsent(clazzIn, clazz -> getHandlerForClass(clazz.getSuperclass()));
    }

    class Impl implements IBubbleColumn
    {
        @Override
        public void onCollide(@Nonnull Entity entity, @Nonnull BlockBubbleColumn column) {
            if(entity.isPushedByWater()) {
                if(column.isDown) entity.motionY = Math.max(-0.3, entity.motionY - 0.03);
                else entity.motionY = Math.min(0.7, entity.motionY + 0.06);
            }
        }

        @Override
        public void onCollideTop(@Nonnull Entity entity, @Nonnull BlockBubbleColumn column) {
            if(entity.isPushedByWater()) {
                if(column.isDown) entity.motionY = Math.max(-0.9, entity.motionY - 0.03);
                else entity.motionY = Math.min(1.8, entity.motionY + 0.1);
            }
        }

        @Nonnull
        @Override
        public NBTBase serializeNBT() { return new NBTTagByte((byte)0); }

        @Override
        public void deserializeNBT(@Nonnull NBTBase nbt) {}
    }

    @Mod.EventBusSubscriber(modid = Subaquatic.MODID)
    class Boat extends Impl
    {
        protected boolean isRocking;
        protected boolean isDown;
        protected float rockingIntensity;
        protected float rockingAnglePrev;
        protected float rockingAngle;

        public int getRockingTicks(@Nonnull Entity entity) {
            return entity.getDataManager().get(PluginEntityBoat.Hooks.ROCKING_TICKS);
        }

        public void setRockingTicks(@Nonnull Entity entity, int rockingTicks) {
            entity.getDataManager().set(PluginEntityBoat.Hooks.ROCKING_TICKS, rockingTicks);
        }

        public float getRenderRockingAngle(float partialTicks) {
            return rockingAnglePrev + (rockingAngle - rockingAnglePrev) * partialTicks;
        }

        @Override
        public void onCollideTop(@Nonnull Entity entity, @Nonnull BlockBubbleColumn column) {
            if(!entity.world.isRemote) {
                isRocking = true;
                isDown = column.isDown;
                if(getRockingTicks(entity) == 0) setRockingTicks(entity, 60);
            }

            entity.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, entity.posX + Math.random(), entity.posY + 0.7, entity.posZ + Math.random(), 0, 0, 0);
            if(entity.world.rand.nextInt(20) == 0) entity.world.playSound(entity.posX, entity.posY, entity.posZ, entity.getSplashSound(), entity.getSoundCategory(), 1, 0.8f + 0.4f * entity.world.rand.nextFloat(), false);
        }

        public void updateRocking(@Nonnull EntityBoat boat) {
            //clientside rocking handler
            if(boat.world.isRemote) {
                final int rockingTicks = getRockingTicks(boat);
                if(rockingTicks > 0) rockingIntensity += 0.005;
                else rockingIntensity -= 0.1;

                rockingIntensity = MathHelper.clamp(rockingIntensity, 0, 1);
                rockingAnglePrev = rockingAngle;
                rockingAngle = 10 * (float)Math.sin(0.5 * boat.world.getTotalWorldTime()) * rockingIntensity;
            }
            //serverside rocking handler
            else {
                if(isRocking) {
                    int rockingTicks = getRockingTicks(boat);
                    if(rockingTicks > 0) {
                        setRockingTicks(boat, --rockingTicks);
                        if(rockingTicks == 0) {
                            setRockingTicks(boat, 0);
                            if(isDown) {
                                boat.motionY -= 0.7;
                                boat.removePassengers();
                            }

                            //vanilla behavior commented out below, causes buggy behavior with upward bubble columns
                            //else boat.motionY = boat.getRecursivePassengersByType(EntityPlayer.class).isEmpty() ? 0.6 : 2.7;
                        }
                    }

                    isRocking = false;
                }

                else setRockingTicks(boat, 0);
            }
        }

        @SubscribeEvent
        static void registerRockingTicks(@Nonnull EntityEvent.EntityConstructing event) {
            if(event.getEntity() instanceof EntityBoat) event.getEntity().getDataManager().register(PluginEntityBoat.Hooks.ROCKING_TICKS, 0);
        }
    }

    @Mod.EventBusSubscriber(modid = Subaquatic.MODID, value = Side.CLIENT)
    class Player extends Impl
    {
        protected boolean wasInBubbleColumn;
        public void updateWasInBubbleColumn(@Nonnull EntityPlayer player) {
            final BlockBubbleColumn column = getFirstBubbleColumnWithin(player.world, player.getEntityBoundingBox());
            if(column != null) {
                if(!wasInBubbleColumn && !player.firstUpdate && !player.isSpectator()) player.world.playSound(player.posX, player.posY, player.posZ, column.getInsideSound(), player.getSoundCategory(), 1, 1, false);
                wasInBubbleColumn = true;
            }

            else wasInBubbleColumn = false;
        }

        @Nullable
        protected BlockBubbleColumn getFirstBubbleColumnWithin(@Nonnull World world, @Nonnull AxisAlignedBB bb) {
            final int minX = MathHelper.floor(bb.minX);
            final int minY = MathHelper.floor(bb.minY);
            final int minZ = MathHelper.floor(bb.minZ);
            final int maxX = MathHelper.ceil(bb.maxX) - 1;
            final int maxY = MathHelper.ceil(bb.maxY) - 1;
            final int maxZ = MathHelper.ceil(bb.maxZ) - 1;

            if(world.isAreaLoaded(minX, minY, minZ, maxX, maxY, maxZ, true)) {
                for(final BlockPos pos : BlockPos.getAllInBoxMutable(minX, minY, minZ, maxX, maxY, maxZ)) {
                    final Block block = world.getBlockState(pos).getBlock();
                    if(block instanceof BlockBubbleColumn) return (BlockBubbleColumn)block;
                }
            }

            return null;
        }

        @SubscribeEvent
        static void onPlayerUpdate(@Nonnull TickEvent.PlayerTickEvent event) {
            if(event.phase == TickEvent.Phase.START) {
                final @Nullable IBubbleColumn cap = IBubbleColumn.get(event.player);
                if(cap instanceof Player) ((Player)cap).updateWasInBubbleColumn(event.player);
            }
        }
    }

    enum Storage implements Capability.IStorage<IBubbleColumn>
    {
        INSTANCE;

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nonnull Capability<IBubbleColumn> capability, @Nonnull IBubbleColumn instance, @Nullable EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(@Nonnull Capability<IBubbleColumn> capability, @Nonnull IBubbleColumn instance, @Nullable EnumFacing side, @Nonnull NBTBase nbt) {
            instance.deserializeNBT(nbt);
        }
    }
}
