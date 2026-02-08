/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.entity.bucketable;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.*;

/**
 * Utility class.
 *
 * @since 1.3.0
 * @author jbred
 *
 */
public final class BucketableEntityRegistry
{
    /**
     * Holds all items that can store {@link BucketableEntityHandler BucketableEntityHandlers}.
     * <br>
     * The value integers in this map represent the size (in pixels) of the corresponding bucket's width.
     * This can be used to prevent large fish from being held within thin buckets.
     *
     * @since 1.3.0
     */
    @Nonnull
    public static final Object2IntMap<Item> BUCKET_REGISTRY = new Object2IntOpenHashMap<>();

    /**
     * Holds all active {@link BucketableEntityHandler BucketableEntityHandlers}.
     * <br>
     * Use {@link BucketableEntityRegistry.Builder} to create and register {@link BucketableEntityHandler BucketableEntityHandlers}.
     * <br>
     * The type parameter for each {@link BucketableEntityHandler} value in this map matches its corresponding entity class key.
     * @since 1.3.0
     */
    @Nonnull
    public static final Map<Class<? extends Entity>, BucketableEntityHandler<?>> REGISTRY = new Object2ObjectOpenHashMap<>();

    /**
     * The base NBT compound that holds all bucketable data.
     * @since 1.3.0
     */
    @Nonnull
    public static final String NBT_ROOT = IEntityBucket.CAPABILITY_ID.toString();

    /**
     * This NBT tag holds the entity registry ID string, stored in {@link BucketableEntityRegistry#NBT_ROOT}.
     * @since 1.3.0
     */
    @Nonnull
    public static final String NBT_ENTITY_ID = "Entity";

    /**
     * This NBT tag holds the {@link Entity#serializeNBT()} compound, stored in {@link BucketableEntityRegistry#NBT_ROOT}.
     * @since 1.3.0
     */
    @Nonnull
    public static final String NBT_ENTITY_TAGS = "FishNBT";

    /**
     * Utility method to capture an entity using its {@link BucketableEntityHandler}, and store it in the player's held item.
     *
     * @return True if the target entity was captured, or false otherwise.
     * @throws NullPointerException If any parameters are null.
     * @since 1.3.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean tryCaptureBucketableEntity(@Nonnull final EntityPlayer player, @Nonnull final EnumHand hand, @Nonnull final Entity target) {
        if(target.isEntityAlive()) {
            @Nullable final BucketableEntityHandler handler = REGISTRY.get(target.getClass());
            if(handler != null && handler.bucketable(target)) {
                @Nonnull final ItemStack held = player.getHeldItem(hand);
                if(IEntityBucket.isBucketableHolder(held, handler)) {
                    if(player.isServerWorld() && player.canPlayerEdit(target.getPosition().up(), EnumFacing.UP, held)) {
                        // Don't modify the actual held stack.
                        @Nonnull final ItemStack heldCopy = ItemHandlerHelper.copyStackWithSize(held, 1);

                        // Save entity's custom name to the item.
                        if(target.hasCustomName()) heldCopy.setStackDisplayName(target.getCustomNameTag());
                        else heldCopy.clearCustomName();

                        // Write entity to the item nbt.
                        @Nonnull final NBTTagCompound compound = heldCopy.getOrCreateSubCompound(NBT_ROOT);
                        compound.setString(NBT_ENTITY_ID, EntityList.getKey(target).toString());
                        compound.setTag(NBT_ENTITY_TAGS, target.serializeNBT());
                        handler.write(target, compound);

                        // Give player new item.
                        if(!player.isCreative() && held.getCount() == 1) player.setHeldItem(hand, heldCopy);
                        else {
                            ItemHandlerHelper.giveItemToPlayer(player, heldCopy);
                            if(!player.isCreative()) held.shrink(1);
                        }

                        // Remove entity from world and play capture fx.
                        target.playSound(handler.sound(target), 1, 1);
                        target.setDead();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Utility method to place an entity from the bucket using its {@link BucketableEntityHandler}.
     *
     * @return True if the target entity was placed, or false otherwise.
     * @throws NullPointerException If world, pos, or bucket are null.
     * @since 1.3.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean tryPlaceBucketedEntity(@Nullable final EntityPlayer player, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final ItemStack bucket) {
        @Nullable final NBTTagCompound compound = bucket.getSubCompound(NBT_ROOT);
        if(compound != null) {
            @Nullable final BucketableEntityHandler handler = getHandler(compound);
            if(handler != null) {
                @Nonnull final Vec3d entityPos = new Vec3d(pos.getX() + 0.5, pos.getY() + ((ItemMonsterPlacer)Items.SPAWN_EGG).getYOffset(world, pos), pos.getZ() + 0.5);

                // Prepare entity nbt.
                @Nonnull final NBTTagCompound entityNBT = compound.getCompoundTag(NBT_ENTITY_TAGS).copy();
                @Nonnull final NBTTagList posNBT = new NBTTagList();
                posNBT.appendTag(new NBTTagDouble(entityPos.x));
                posNBT.appendTag(new NBTTagDouble(entityPos.y));
                posNBT.appendTag(new NBTTagDouble(entityPos.z));
                entityNBT.setTag("Pos", posNBT);
                entityNBT.setUniqueId("UUID", MathHelper.getRandomUUID());
                entityNBT.setString("id", compound.getString(NBT_ENTITY_ID));

                // Ensure entity is valid.
                @Nullable final Entity entity = EntityList.createEntityFromNBT(entityNBT, world);
                if(entity == null) return false;
                @Nullable final IBucketableEntity bucketable = entity.getCapability(IBucketableEntity.CAPABILITY, null);
                if(bucketable == null) return false;

                // Prepare entity.
                else if(!world.isRemote) {
                    final boolean shrink = player == null || !player.isCreative();
                    if(entity instanceof EntityLiving) ((EntityLiving)entity).enablePersistence();
                    entity.setPosition(entityPos.x, entityPos.y, entityPos.z);
                    bucketable.setFromBucket(true);
                    handler.read(entity, compound);

                    // Save item's custom name to the entity.
                    if(bucket.hasDisplayName()) {
                        entity.setCustomNameTag(bucket.getDisplayName());
                        if(shrink) bucket.clearCustomName();
                    }

                    // Add entity to world and remove from item.
                    if(shrink) {
                        bucket.removeSubCompound(NBT_ROOT);
                        if(bucket.hasTagCompound() && bucket.getTagCompound().isEmpty()) bucket.setTagCompound(null);
                    }

                    world.spawnEntity(entity);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * @return An {@link BucketableEntityRegistry#NBT_ROOT} tag populated with the provided entityId.
     * @throws NullPointerException If entityId is null.
     * @since 1.3.0
     */
    @Nonnull
    public static NBTTagCompound createRootTag(@Nonnull final ResourceLocation entityId) {
        @Nonnull final NBTTagCompound root = new NBTTagCompound();
        root.setString(NBT_ENTITY_ID, entityId.toString());
        return root;
    }

    /**
     * @return The active {@link BucketableEntityHandler} for the bucket, or null if none.
     * @throws NullPointerException If bucket is null.
     * @since 1.3.0
     */
    @Nullable
    public static BucketableEntityHandler<?> getHandler(@Nonnull final ItemStack bucket) {
        @Nullable final NBTTagCompound nbt = bucket.getSubCompound(NBT_ROOT);
        return nbt != null ? getHandler(nbt) : null;
    }

    /**
     * @return The active {@link BucketableEntityHandler} for a bucket's {@link BucketableEntityRegistry#NBT_ROOT} tag, or null if none.
     * @throws NullPointerException If nbtRoot is null.
     * @since 1.3.0
     */
    @Nullable
    public static BucketableEntityHandler<?> getHandler(@Nonnull final NBTTagCompound nbtRoot) {
        return REGISTRY.get(EntityList.getClass(new ResourceLocation(nbtRoot.getString(NBT_ENTITY_ID))));
    }

    /**
     * To register a Builder to {@link BucketableEntityRegistry#REGISTRY}, use {@link Builder#register() Builder.register()}.
     * @return A utility class for easily constructing new {@link BucketableEntityHandler BucketableEntityHandlers}.
     * @throws NullPointerException If entityClass is null.
     * @since 1.3.0
     */
    @Nonnull
    public static <T extends Entity> Builder<T> builder(@Nonnull final Class<T> entityClass) { return new Builder<>(entityClass); }
    public static final class Builder<T extends Entity>
    {
        @Nullable private Predicate<FluidStack> breathable;
        @Nullable private Predicate<T> bucketable;
        @Nullable private IntSupplier bucketSize;
        @Nullable private BiConsumer<T, NBTTagCompound> read;
        @Nullable private BiConsumer<T, NBTTagCompound> write;
        @Nullable private Function<T, SoundEvent> sound;
        @Nullable private Supplier<List<NBTTagCompound>> subtypes;
        @Nullable private BiFunction<NBTTagCompound, Boolean, List<String>> tooltip;
        @Nullable private BiFunction<NBTTagCompound, Integer, OptionalInt> overlayColor;
        @Nullable private Supplier<ModelResourceLocation> overlayModel;
        
        @Nonnull private final Class<T> entityClass;
        @Nonnull private final ResourceLocation entityId;

        private boolean frozen = false;
        public Builder(@Nonnull final Class<T> entityClassIn) {
            entityId = Objects.requireNonNull(EntityList.getKey(entityClass = Objects.requireNonNull(entityClassIn)), "Entity is unregistered: " + entityClassIn.getName());
            breathable(Material.WATER, true).bucketable(entity -> true).read((entity, compound) -> {}).write((entity, compound) -> {}).overlayColor(OptionalInt.empty())
            .tooltip((compound, advanced) -> {
                @Nullable final String name = EntityList.getTranslationName(entityId);
                return Collections.singletonList(I18n.format("tooltip.subaquatic.fish_bucket", I18n.format("entity." + (name != null ? name : "generic") + ".name")));
            })
            .subtypes(entityId -> Collections.singletonList(createRootTag(entityId)));
        }

        /**
         * Setter for {@link BucketableEntityHandler#breathable}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> breathable(@Nonnull final Predicate<FluidStack> breathableIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            breathable = breathableIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#breathable}, using a constant and the blacklist.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> breathable(@Nonnull final FluidStack breathableIn, final boolean checkBlacklist) {
            breathable(breathableIn::isFluidEqual);
            if(checkBlacklist) breathable = breathable.and(fluidStack -> !SubaquaticConfigHandler.FISH_BUCKET_FLUID_BLACKLIST.contains(fluidStack.getFluid()));
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#breathable}, using a constant and the blacklist.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> breathable(@Nonnull final Material breathableIn, final boolean checkBlacklist) {
            breathable(fluidStack -> FluidState.of(fluidStack.getFluid()).getMaterial() == breathableIn);
            if(checkBlacklist) breathable = breathable.and(fluidStack -> !SubaquaticConfigHandler.FISH_BUCKET_FLUID_BLACKLIST.contains(fluidStack.getFluid()));
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#bucketable}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> bucketable(@Nonnull final Predicate<T> bucketableIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            bucketable = bucketableIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#bucketSize}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> bucketSize(@Nonnull final IntSupplier bucketSizeIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            bucketSize = bucketSizeIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#bucketSize}, using a constant.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> bucketSize(final int bucketSizeIn) {
            return bucketSize(() -> bucketSizeIn);
        }

        /**
         * Setter for {@link BucketableEntityHandler#read}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> read(@Nonnull final BiConsumer<T, NBTTagCompound> readIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            read = readIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#write}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> write(@Nonnull final BiConsumer<T, NBTTagCompound> writeIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            write = writeIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#sound}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> sound(@Nonnull final Function<T, SoundEvent> soundIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            sound = soundIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#sound}, using a constant.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> sound(@Nonnull final SoundEvent soundIn) {
            return sound(entity -> soundIn);
        }

        /**
         * Setter for {@link BucketableEntityHandler#subtypes}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> subtypes(@Nonnull final Supplier<List<NBTTagCompound>> subtypesIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            subtypes = subtypesIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#subtypes}, using the entity id.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> subtypes(@Nonnull final Function<ResourceLocation, List<NBTTagCompound>> subtypesIn) {
            return subtypes(() -> subtypesIn.apply(entityId));
        }

        /**
         * Setter for {@link BucketableEntityHandler#tooltip}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> tooltip(@Nonnull final BiFunction<NBTTagCompound, Boolean, List<String>> tooltipIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            tooltip = tooltipIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#overlayColor}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> overlayColor(@Nonnull final BiFunction<NBTTagCompound, Integer, OptionalInt> overlayColorIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            overlayColor = overlayColorIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#overlayColor}, using a constant.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> overlayColor(@Nonnull final OptionalInt overlayColorIn) {
            return overlayColor((compound, tintIndex) -> overlayColorIn);
        }

        /**
         * Setter for {@link BucketableEntityHandler#overlayModel}.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> overlayModel(@Nonnull final Supplier<ModelResourceLocation> overlayModelIn) {
            if(frozen) throw new IllegalStateException("Cannot modify finalized builder.");
            overlayModel = overlayModelIn;
            return this;
        }

        /**
         * Setter for {@link BucketableEntityHandler#overlayModel}, using a constant.
         * @since 1.3.0
         */
        @Nonnull
        public Builder<T> overlayModel(@Nonnull final ModelResourceLocation overlayModelIn) {
            return overlayModel(() -> overlayModelIn);
        }

        /**
         * Finalizes this builder and adds it to {@link BucketableEntityRegistry#REGISTRY}.
         * @since 1.3.0
         */
        public void register() {
            frozen = true;
            Objects.requireNonNull(breathable, "breathable cannot be null, and must be set!");
            Objects.requireNonNull(bucketable, "bucketable cannot be null, and must be set!");
            Objects.requireNonNull(bucketSize, "bucketSize cannot be null, and must be set!");
            Objects.requireNonNull(read, "read cannot be null, and must be set!");
            Objects.requireNonNull(write, "write cannot be null, and must be set!");
            Objects.requireNonNull(sound, "sound cannot be null, and must be set!");
            Objects.requireNonNull(subtypes, "subtypes cannot be null, and must be set!");
            Objects.requireNonNull(tooltip, "tooltip cannot be null, and must be set!");
            Objects.requireNonNull(overlayColor, "overlayColors cannot be null, and must be set!");
            Objects.requireNonNull(overlayModel, "overlayModel cannot be null, and must be set!");
            final int creationId = REGISTRY.values().stream().mapToInt(BucketableEntityHandler::creationId).max().orElse(-1) + 1;
            REGISTRY.put(entityClass, new BucketableEntityHandler<T>() {
                @Override
                public boolean breathable(@Nonnull final FluidStack fluidStack) {
                    return breathable.test(fluidStack);
                }

                @Override
                public boolean bucketable(@Nonnull final T entity) {
                    return bucketable.test(entity);
                }

                @Override
                public int bucketSize() {
                    return bucketSize.getAsInt();
                }

                @Override
                public int creationId() {
                    return creationId;
                }

                @Override
                public void read(@Nonnull final T entity, @Nonnull final NBTTagCompound compound) {
                    read.accept(entity, compound);
                }

                @Override
                public void write(@Nonnull final T entity, @Nonnull final NBTTagCompound compound) {
                    write.accept(entity, compound);
                }

                @Nonnull
                @Override
                public SoundEvent sound(@Nonnull final T entity) {
                    return sound.apply(entity);
                }

                @Nonnull
                @Override
                public List<NBTTagCompound> subtypes() {
                    return subtypes.get();
                }

                @Nonnull
                @SideOnly(Side.CLIENT)
                @Override
                public List<String> tooltip(@Nonnull final NBTTagCompound compound, final boolean advanced) {
                    return tooltip.apply(compound, advanced);
                }

                @Nonnull
                @SideOnly(Side.CLIENT)
                @Override
                public OptionalInt overlayColor(@Nonnull final NBTTagCompound compound, final int tintIndex) {
                    return overlayColor.apply(compound, tintIndex);
                }

                @Nonnull
                @SideOnly(Side.CLIENT)
                @Override
                public ModelResourceLocation overlayModel() {
                    return overlayModel.get();
                }
            });
        }
    }
}
