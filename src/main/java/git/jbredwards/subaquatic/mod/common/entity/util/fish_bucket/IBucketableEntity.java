/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket;

import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public interface IBucketableEntity
{
    boolean canBucket();

    boolean isFromBucket();
    void setFromBucket(boolean fromBucket);

    @Nonnull
    AbstractEntityBucketHandler createFishBucketHandler();
    default void postSetHandlerEntityNBT(@Nonnull AbstractEntityBucketHandler handler) {}
    default boolean tryCaptureEntity(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        final ItemStack held = player.getHeldItem(hand);
        final ItemStack fishBucket = tryCaptureToStack(held);

        if(!fishBucket.isEmpty()) {
            if(!player.isCreative() && held.getCount() == 1) player.setHeldItem(hand, fishBucket);
            else {
                ItemHandlerHelper.giveItemToPlayer(player, fishBucket);
                if(!player.isCreative()) held.shrink(1);
            }

            ((Entity)this).playSound(getBucketFillSound(), 1, 1);
            ((Entity)this).setDead();
            return true;
        }

        return false;
    }

    @Nonnull
    default ItemStack tryCaptureToStack(@Nonnull ItemStack stack) {
        final Entity entity = (Entity)this;
        if(entity.isEntityAlive() && IEntityBucket.canStackHoldEntity(stack) && canBucket()) {
            final ItemStack fishBucket = ItemHandlerHelper.copyStackWithSize(stack, 1);
            final IEntityBucket cap = IEntityBucket.get(fishBucket);
            if(cap != null && cap.getHandler() == null) {
                if(entity.hasCustomName()) fishBucket.setStackDisplayName(entity.getCustomNameTag());
                final AbstractEntityBucketHandler handler = createFishBucketHandler();
                handler.entityNbt = entity.serializeNBT();

                postSetHandlerEntityNBT(handler);
                cap.setHandler(handler);

                return fishBucket;
            }
        }

        return ItemStack.EMPTY;
    }

    default void onCreatedByBucket(@Nonnull ItemStack bucket, @Nonnull AbstractEntityBucketHandler handler) {}
    //called by bucket items when they're used (vanilla & forge call this through asm)
    static void placeCapturedEntity(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull ItemStack bucket, @Nullable AbstractEntityBucketHandler data) {
        if(data != null && !world.isRemote) {
            final Entity fishEntity = EntityList.createEntityFromNBT(data.entityNbt, world);
            if(fishEntity != null) {
                final double yOffset = ((ItemMonsterPlacer)Items.SPAWN_EGG).getYOffset(world, pos);
                fishEntity.setPosition(pos.getX() + 0.5, pos.getY() + yOffset, pos.getZ() + 0.5);
                fishEntity.setUniqueId(MathHelper.getRandomUUID());

                if(bucket.hasDisplayName()) fishEntity.setCustomNameTag(bucket.getDisplayName());
                if(fishEntity instanceof IBucketableEntity) {
                    ((IBucketableEntity)fishEntity).setFromBucket(true);
                    ((IBucketableEntity)fishEntity).onCreatedByBucket(bucket, data);
                }

                world.spawnEntity(fishEntity);
            }
        }
    }

    @Nonnull
    default SoundEvent getBucketFillSound() { return SubaquaticSounds.BUCKET_FILL_FISH; }
}
