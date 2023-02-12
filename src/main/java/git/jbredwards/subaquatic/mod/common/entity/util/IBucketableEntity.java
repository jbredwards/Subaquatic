package git.jbredwards.subaquatic.mod.common.entity.util;

import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public interface IBucketableEntity
{
    @Nonnull
    Entity getAsEntity();
    boolean canBucket();

    boolean isFromBucket();
    void setFromBucket(boolean fromBucket);

    void buildFishBucketData(@Nonnull FishBucketData data);
    default void onCreatedByBucket(@Nonnull ItemStack bucket, @Nonnull FishBucketData data) {}

    default boolean tryCaptureEntity(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        final ItemStack held = player.getHeldItem(hand);
        final Entity entity = getAsEntity();

        if(entity.isEntityAlive() && IFishBucket.canStackHoldFish(held) && canBucket()) {
            final ItemStack fishBucket = ItemHandlerHelper.copyStackWithSize(held, 1);
            final IFishBucket cap = IFishBucket.get(fishBucket);
            if(cap != null && cap.getData() == FishBucketData.EMPTY) {
                if(entity.hasCustomName()) fishBucket.setStackDisplayName(entity.getCustomNameTag());
                final FishBucketData data = new FishBucketData();
                buildFishBucketData(data);
                cap.setData(data);

                if(!player.isCreative() && held.getCount() == 1) player.setHeldItem(hand, fishBucket);
                else {
                    ItemHandlerHelper.giveItemToPlayer(player, fishBucket);
                    if(!player.isCreative()) held.shrink(1);
                }

                entity.playSound(SubaquaticSounds.BUCKET_FILL_FISH, 1, 1);
                entity.setDead();

                return true;
            }
        }

        return false;
    }

    static void placeCapturedEntity(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull ItemStack bucket, @Nonnull FishBucketData data) {
        if(data != FishBucketData.EMPTY) {
            final Entity fishEntity = EntityList.createEntityFromNBT(data.fishNbt, world);
            if(fishEntity != null) {
                if(!world.isRemote) {
                    fishEntity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
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
    }
}
