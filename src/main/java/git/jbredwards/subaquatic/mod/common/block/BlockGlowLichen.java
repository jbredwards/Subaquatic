package git.jbredwards.subaquatic.mod.common.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.item.util.IBlockCluster;
import git.jbredwards.subaquatic.mod.common.tileentity.TileEntityGlowLichen;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author jbred
 *
 */
public class BlockGlowLichen extends Block implements IBlockCluster, IFluidloggable, IGrowable, IShearable, ITileEntityProvider
{
    @Nonnull
    public static final PropertyBool DOWN = PropertyBool.create("down"), UP = PropertyBool.create("up"), NORTH = PropertyBool.create("north"), SOUTH = PropertyBool.create("south"), WEST = PropertyBool.create("west"), EAST = PropertyBool.create("east");

    @Nonnull
    public static final List<Pair<AxisAlignedBB, EnumFacing>> BOXES = ImmutableList.of(
            Pair.of(new AxisAlignedBB(0,      0,      0,      1,      0.0625, 1),      EnumFacing.DOWN),
            Pair.of(new AxisAlignedBB(0,      0.9375, 0,      1,      1,      1),      EnumFacing.UP),
            Pair.of(new AxisAlignedBB(0,      0,      0,      1,      1,      0.0625), EnumFacing.NORTH),
            Pair.of(new AxisAlignedBB(0,      0,      0.9375, 1,      1,      1),      EnumFacing.SOUTH),
            Pair.of(new AxisAlignedBB(0,      0,      0,      0.0625, 1,      1),      EnumFacing.WEST),
            Pair.of(new AxisAlignedBB(0.9375, 0,      0,      1,      1,      1),      EnumFacing.EAST)
    );

    public BlockGlowLichen(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockGlowLichen(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setTickRandomly(true);
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) { return new TileEntityGlowLichen(); }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) { return 0; }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) { return false; }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) { return false; }

    @Override
    public boolean isSideSolid(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        return false;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        final TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileEntityGlowLichen) { //used for rendering
            final TileEntityGlowLichen glowLichen = (TileEntityGlowLichen)tile;
            state = state.withProperty(DOWN, glowLichen.isAttachedTo(EnumFacing.DOWN));
            state = state.withProperty(UP, glowLichen.isAttachedTo(EnumFacing.UP));
            state = state.withProperty(NORTH, glowLichen.isAttachedTo(EnumFacing.NORTH));
            state = state.withProperty(SOUTH, glowLichen.isAttachedTo(EnumFacing.SOUTH));
            state = state.withProperty(WEST, glowLichen.isAttachedTo(EnumFacing.WEST));
            state = state.withProperty(EAST, glowLichen.isAttachedTo(EnumFacing.EAST));
        }

        return state;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        final TileEntity tile = source.getTileEntity(pos);
        if(tile instanceof TileEntityGlowLichen) {
            AxisAlignedBB bb = null;
            for(final Pair<AxisAlignedBB, EnumFacing> entry : BOXES)
                if(((TileEntityGlowLichen)tile).isAttachedTo(entry.getValue()))
                    bb = bb != null ? bb.union(entry.getKey()) : entry.getKey();

            if(bb != null) return bb;
        }

        return FULL_BLOCK_AABB;
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(@Nonnull IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
        final TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileEntityGlowLichen) {
            final List<RayTraceResult> list = BOXES.stream()
                    .filter(entry -> ((TileEntityGlowLichen)tile).isAttachedTo(entry.getValue()))
                    .map(entry -> rayTrace(pos, start, end, entry.getKey()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if(list.isEmpty()) return null;
            RayTraceResult furthest = null;
            double dist = -1;

            for(final RayTraceResult trace : list) {
                final double newDist = trace.hitVec.squareDistanceTo(end);
                if(newDist > dist) {
                    furthest = trace;
                    dist = newDist;
                }
            }

            return furthest;
        }

        return rayTrace(pos, start, end, FULL_BLOCK_AABB);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        return canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.offset(side.getOpposite())).isSideSolid(worldIn, pos, side);
    }

    @Override
    public void randomTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random) {
        final TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileEntityGlowLichen)
            for(final EnumFacing side : EnumFacing.VALUES)
                if(checkAndDropBlock((TileEntityGlowLichen)tile, worldIn, pos, side)) break;
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos) {
        if(!pos.equals(fromPos)) {
            final TileEntity tile = worldIn.getTileEntity(pos);
            if(tile instanceof TileEntityGlowLichen) {
                final BlockPos diff = pos.subtract(fromPos);
                checkAndDropBlock((TileEntityGlowLichen)tile, worldIn, pos, EnumFacing.getFacingFromVector(diff.getX(), diff.getY(), diff.getZ()));
            }
        }
    }

    public boolean checkAndDropBlock(@Nonnull TileEntityGlowLichen tile, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        if(tile.isAttachedTo(side.getOpposite()) && !canPlaceBlockOnSide(world, pos, side)) {
            tile.setAttachedTo(side.getOpposite(), false);

            if(tile.attachedSideData == 0) return world.setBlockToAir(pos);
            else world.notifyBlockUpdate(pos, getDefaultState(), getDefaultState(), Constants.BlockFlags.SEND_TO_CLIENTS);
        }

        return false;
    }

    @Override
    public boolean canClusterWith(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull IBlockState oldState) {
        final TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileEntityGlowLichen && !((TileEntityGlowLichen)tile).isAttachedTo(side.getOpposite());
    }

    @Override
    public boolean clusterWith(@Nonnull ItemBlock itemBlock, @Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState oldState) {
        final TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityGlowLichen) {
            ((TileEntityGlowLichen)tile).setAttachedTo(side.getOpposite(), true);

            world.notifyBlockUpdate(pos, getDefaultState(), getDefaultState(), Constants.BlockFlags.SEND_TO_CLIENTS);
            if(player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            return true;
        }

        return false;
    }

    @Override
    public boolean createInitialCluster(@Nonnull ItemBlock itemBlock, @Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState oldState) {
        world.setBlockState(pos, getDefaultState(), 11);

        final TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityGlowLichen) {
            ((TileEntityGlowLichen)tile).attachedSideData = 0;
            ((TileEntityGlowLichen)tile).setAttachedTo(side.getOpposite(), true);

            world.notifyBlockUpdate(pos, getDefaultState(), getDefaultState(), Constants.BlockFlags.SEND_TO_CLIENTS);
            if(player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            return true;
        }

        return false;
    }

    @Override
    public int quantityDropped(@Nonnull Random random) { return 0; }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) { return true; }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, int fortune) {
        final List<ItemStack> items = new ArrayList<>();
        final TileEntity tile = world.getTileEntity(pos);

        if(tile instanceof TileEntityGlowLichen) {
            for(final EnumFacing side : EnumFacing.VALUES) {
                if(((TileEntityGlowLichen)tile).isAttachedTo(side)) {
                    items.add(new ItemStack(SubaquaticItems.GLOW_LICHEN));
                }
            }
        }

        return items;
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        //TODO backport bonemeal functionality
        /*final TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileEntityGlowLichen) {
            final TileEntityGlowLichen glowLichen = (TileEntityGlowLichen)tile;
            for(EnumFacing side : EnumFacing.VALUES) {
                if(glowLichen.isAttachedTo(side)) {
                    for(EnumFacing offset : EnumFacing.VALUES) {
                        if(side.getAxis() != offset.getAxis() && !glowLichen.isAttachedTo(offset)) {

                        }
                    }
                }
            }
        }

        return false;*/
        return true;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return true;
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        //TODO backport bonemeal functionality
        //final List<EnumFacing> values = Lists.newArrayList(EnumFacing.VALUES);
        //Collections.shuffle(values, rand);
        if(!worldIn.isRemote) {
            final EntityItem item = new EntityItem(worldIn,
                    pos.getX() + worldIn.rand.nextFloat() * 0.5 + 0.25,
                    pos.getY() + worldIn.rand.nextFloat() * 0.5 + 0.25,
                    pos.getZ() + worldIn.rand.nextFloat() * 0.5 + 0.25,
                    new ItemStack(SubaquaticItems.GLOW_LICHEN));

            item.setDefaultPickupDelay();
            worldIn.spawnEntity(item);
        }
    }
}
