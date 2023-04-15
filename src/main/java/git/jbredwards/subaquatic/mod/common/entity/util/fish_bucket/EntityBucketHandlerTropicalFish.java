package git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket;

import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public class EntityBucketHandlerTropicalFish extends AbstractEntityBucketHandler
{
    @Nullable
    public TropicalFishData fishData;

    @Override
    protected void writeToNBT(@Nonnull NBTTagCompound nbt) {
        if(fishData != null) nbt.setInteger("Variant", fishData.serialize());
    }

    @Override
    protected void readFromNBT(@Nonnull NBTTagCompound nbt) {
        if(nbt.hasKey("Variant", Constants.NBT.TAG_INT)) fishData = TropicalFishData.deserialize(nbt.getInteger("Variant"));
    }

    @Nonnull
    @Override
    public EntityEntry getEntityEntry() { return SubaquaticEntities.TROPICAL_FISH; }

    @Nonnull
    @Override
    public List<ResourceLocation> getSprites() {
        return ImmutableList.of(
                new ResourceLocation(Subaquatic.MODID, "items/fish_bucket_overlay_tropical_fish_1"),
                new ResourceLocation(Subaquatic.MODID, "items/fish_bucket_overlay_tropical_fish_2"),
                new ResourceLocation(Subaquatic.MODID, "items/fish_bucket_overlay_tropical_fish_3")
        );
    }

    @Nonnull
    @Override
    public List<AbstractEntityBucketHandler> getSubTypes() {
        final List<AbstractEntityBucketHandler> subTypes = new LinkedList<>();
        SubaquaticTropicalFishConfig.DEFAULT_TYPES.forEach(data -> {
            final EntityBucketHandlerTropicalFish handler = new EntityBucketHandlerTropicalFish();
            handler.fishData = data;
            subTypes.add(handler);
        });

        return subTypes;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleTooltip(@Nonnull List<String> tooltip, @Nonnull ItemStack bucket, @Nonnull ITooltipFlag flag) {
        if(fishData != null) {
            if(fishData.hasTranslation(I18n::hasKey)) tooltip.add(1, fishData.getTranslatedName(I18n::format));
            else {
                final List<String> fallback = new LinkedList<>();
                fallback.add(fishData.getTranslatedShape(I18n::format));
                fallback.add(fishData.getTranslatedColor(I18n::format));

                tooltip.addAll(1, fallback);
            }
        }

        else tooltip.add(1, I18n.format("tooltip.subaquatic.fish_bucket.tropical_fish.missing"));
        super.handleTooltip(tooltip, bucket, flag);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(@Nonnull ItemStack bucket, int tintIndex) {
        switch(tintIndex) {
            case 3: {
                if(fishData != null) return fishData.primaryColor.getColorValue();

                final long systemTime = Minecraft.getSystemTime() / 20;
                final float progress = (systemTime % 25 + Minecraft.getMinecraft().getRenderPartialTicks()) / 25f;

                final float[] startRGB = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata((int)(systemTime / 25 % EnumDyeColor.values().length)));
                final float[] endRGB = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata((int)((systemTime / 25 + 1) % EnumDyeColor.values().length)));

                return ((int)((startRGB[0] * (1 - progress) + endRGB[0] * progress) * 255) & 0xFF) << 16
                        | ((int)((startRGB[1] * (1 - progress) + endRGB[1] * progress) * 255) & 0xFF) << 8
                        | (int)((startRGB[2] * (1 - progress) + endRGB[2] * progress) * 255) & 0xFF;
            }

            case 4: {
                if(fishData != null) return fishData.secondaryColor.getColorValue();

                final long systemTime = Minecraft.getSystemTime() / 20;
                final float progress = (systemTime % 20 + Minecraft.getMinecraft().getRenderPartialTicks()) / 20f;

                final float[] startRGB = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage((int)(systemTime / 20 % EnumDyeColor.values().length)));
                final float[] endRGB = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage((int)((systemTime / 20 + 1) % EnumDyeColor.values().length)));

                return ((int)((startRGB[0] * (1 - progress) + endRGB[0] * progress) * 255) & 0xFF) << 16
                        | ((int)((startRGB[1] * (1 - progress) + endRGB[1] * progress) * 255) & 0xFF) << 8
                        | (int)((startRGB[2] * (1 - progress) + endRGB[2] * progress) * 255) & 0xFF;
            }

            default: return -1;
        }
    }
}
