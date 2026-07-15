package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class BlockDriedKelp extends BlockRotatedPillar
{
    @Nonnull public static final PropertyBool DRIED = PropertyBool.create("dried");

    public BlockDriedKelp(@Nonnull final Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockDriedKelp(@Nonnull final Material materialIn, @Nonnull final MapColor color) {
        super(materialIn, color);
        setDefaultState(getDefaultState().withProperty(AXIS, EnumFacing.Axis.Y).withProperty(DRIED, true));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, DRIED);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return super.getStateFromMeta(meta).withProperty(DRIED, (meta & 1) == 0);
    }

    @Override
    public int getMetaFromState(@Nonnull final IBlockState state) {
        return super.getMetaFromState(state) | (state.getValue(DRIED) ? 0 : 1);
    }

    @Override
    public int damageDropped(@Nonnull final IBlockState state) {
        return state.getValue(DRIED) ? 0 : 1;
    }

    @Nonnull
    @Override
    protected ItemStack getSilkTouchDrop(@Nonnull final IBlockState state) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Nonnull
    @Override
    public SoundType getSoundType(@Nonnull final IBlockState state, @Nonnull final World world, @Nonnull final BlockPos pos, @Nullable final Entity entity) {
        return state.getValue(DRIED) ? super.getSoundType(state, world, pos, entity) : SubaquaticBlocks.KELP.getSoundType();
    }

    @Nonnull
    @Override
    public MapColor getMapColor(@Nonnull final IBlockState state, @Nonnull final IBlockAccess worldIn, @Nonnull final BlockPos pos) {
        return state.getValue(DRIED) ? super.getMapColor(state, worldIn, pos) : MapColor.LIME;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, @Nonnull final EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(DRIED, (meta & 1) == 0);
    }
}
