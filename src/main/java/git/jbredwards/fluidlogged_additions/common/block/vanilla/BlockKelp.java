package git.jbredwards.fluidlogged_additions.common.block.vanilla;

import git.jbredwards.fluidlogged_additions.client.util.IModelProperties;
import git.jbredwards.fluidlogged_additions.common.block.BlockFluidloggedPlant;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("NullableProblems")
public class BlockKelp extends BlockFluidloggedPlant implements IModelProperties.IModelBlockProperties
{
    public static final PropertyBool GROWN = PropertyBool.create("grown");
    public static final AxisAlignedBB[] AABB = { new AxisAlignedBB(0, 0, 0, 1, (10.0f / 16), 1), FULL_BLOCK_AABB };

    public BlockKelp(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
        setRenderLayer(BlockRenderLayer.CUTOUT);

        setDefaultState(getDefaultState().withProperty(GROWN, false));
    }

    public BlockKelp(Fluid fluid, Material material) {
        super(fluid, material);
        setRenderLayer(BlockRenderLayer.CUTOUT);

        setDefaultState(getDefaultState().withProperty(GROWN, false));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(LEVEL, GROWN).add(FLUID_RENDER_PROPS.toArray(new IUnlistedProperty[0])).build();
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(GROWN, meta != 0);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(GROWN) ? 1 : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if(worldIn.getBlockState(pos.up()).getBlock() == this) return state.withProperty(GROWN, true);
        else return state.withProperty(GROWN, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB[state.getValue(GROWN) ? 1 : 0];
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !state.getActualState(worldIn, pos).getValue(GROWN) && canPlaceBlockAt(worldIn, pos.up());
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        worldIn.setBlockState(pos.up(), getDefaultState());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setupModel(@Nonnull Block block) {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
    }
}
