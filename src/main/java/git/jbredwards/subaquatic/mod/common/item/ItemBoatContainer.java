package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticChestBoatConfig;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public abstract class ItemBoatContainer extends ItemBoat
{
    public ItemBoatContainer() {
        super(EntityBoat.Type.OAK);
        registerDispenserBehavior();
    }

    protected void registerDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorDefaultDispenseItem() {
            @Nonnull
            @Override
            protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
                final EnumFacing dispenserFacing = source.getBlockState().getValue(BlockDispenser.FACING);
                final double x = source.getX() + dispenserFacing.getXOffset() * 1.125;
                final double y = source.getY() + dispenserFacing.getYOffset() * 1.125;
                final double z = source.getZ() + dispenserFacing.getZOffset() * 1.125;

                final BlockPos boatPos = source.getBlockPos().offset(dispenserFacing);
                final Material material = source.getWorld().getBlockState(boatPos).getMaterial();
                double yOffset = 0;

                if(material == Material.WATER) yOffset = 1;
                else if(material != Material.AIR || source.getWorld().getBlockState(boatPos.down()).getMaterial() != Material.WATER)
                    return super.dispenseStack(source, stack);

                final EntityBoatContainer boat = new EntityBoatContainer(source.getWorld(), x, y + yOffset, z);
                boat.setContainerStack(ItemHandlerHelper.copyStackWithSize(stack, 1));
                boat.rotationYaw = dispenserFacing.getHorizontalAngle();

                buildBoatContainerPart(boat, stack);
                if(stack.hasDisplayName()) boat.containerPart.setCustomNameTag(stack.getDisplayName());
                source.getWorld().spawnEntity(boat);

                stack.shrink(1);
                return stack;
            }
        });
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        final IBoatType cap = IBoatType.get(stack);
        if(cap != null) {
            final Item boat = cap.getType().getKey();
            //if a defined special case exists in .lang file, use that instead of auto generating a name
            final String specialCase = String.format("%s.type.%s.name", stack.getTranslationKey(), boat.getTranslationKey());
            if(I18n.canTranslate(specialCase)) return I18n.translateToLocal(specialCase);
            //auto generate a name
            return boat.getItemStackDisplayName(new ItemStack(boat)).replaceFirst(
                    I18n.translateToLocal(getRegexTarget()),
                    I18n.translateToLocal(getRegexReplacement()));
        }

        //should never pass
        return super.getItemStackDisplayName(stack);
    }

    @Nonnull
    public abstract String getRegexTarget();

    @Nonnull
    public abstract String getRegexReplacement();

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) SubaquaticChestBoatConfig.TYPES.forEach(type -> {
            final ItemStack stack = new ItemStack(this);
            final IBoatType cap = IBoatType.get(stack);
            if(cap != null) {
                cap.setType(type);
                items.add(stack);
            }
        });
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public abstract void buildBoatContainerPart(@Nonnull EntityBoatContainer boat, @Nonnull ItemStack boatStack);
}
