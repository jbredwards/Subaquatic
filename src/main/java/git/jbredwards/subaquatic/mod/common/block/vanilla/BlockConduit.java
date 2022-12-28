package git.jbredwards.subaquatic.mod.common.block.vanilla;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("NullableProblems")
public class BlockConduit extends Block implements IFluidloggable
{
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final AxisAlignedBB AABB = new AxisAlignedBB((5.0 / 16), (5.0 / 16), (5.0 / 16), (11.0 / 16), (11.0 / 16), (11.0 / 16));

    public BlockConduit(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        setDefaultState(getDefaultState().withProperty(ACTIVE, false));
    }

    public BlockConduit(Material materialIn) {
        super(materialIn);
        setDefaultState(getDefaultState().withProperty(ACTIVE, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(ACTIVE).build();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ACTIVE, meta > 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    //doesn't use the model render when active
    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return state.getValue(ACTIVE) ? EnumBlockRenderType.INVISIBLE : EnumBlockRenderType.MODEL;
    }

    /*@SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFluidValid(@Nonnull IBlockState here, @Nullable Fluid fluid) {
        return fluid == null || fluid == FluidRegistry.WATER;
    }

    //makes the object cache not null
    @Override
    public void readFromStoredNBT(@Nonnull TileEntityFluidlogged te) {
        if(te.cache == null) te.cache = new Object[] {0, 0};
    }

    @Override
    public void fluidloggedTick(@Nonnull TileEntityFluidlogged te) {
        final long ticks = te.getWorld().getTotalWorldTime();

        //actions
        if(ticks % 40 == 0) {
            te.setStored(te.stored.withProperty(ACTIVE, shouldBeActive(te)), false);

            if(te.stored.getValue(ACTIVE)) {
                addEffectsToPlayers(te);
                attackMobs(te);
            }
        }

        //heart beat sound
        if(ticks % 80 == 0 && te.stored.getValue(ACTIVE)) {
            te.getWorld().playSound(null, te.getPos(), sound, SoundCategory.BLOCKS, 1, 1);
        }

        //ambient sound
        if(ticks > getNextSoundTime(te) && te.stored.getValue(ACTIVE)) {
            setNextSoundTime(te, ticks + 60 + te.getWorld().rand.nextInt(40));
            te.getWorld().playSound(null, te.getPos(), sound, SoundCategory.BLOCKS, 1, 1);
        }
    }

    public boolean shouldBeActive(@Nonnull TileEntityFluidlogged te) {
        final List<BlockPos> positions = new ArrayList<>();

        //makes sure this is surrounded by water
        for(int x = -1; x < 2; ++x) {
            for(int y = -1; y < 2; ++y) {
                for(int z = -1; z < 2; ++z) {
                    @Nullable Fluid fluid = FluidloggedUtils.getFluidFromBlock(te.getWorld().getBlockState(te.getPos().add(x, y, z)).getBlock());
                    if(fluid == null || !isFluidValid(te.stored, fluid)) {
                        return false;
                    }
                }
            }
        }

        //looks for the rings
        for(int x = -2; x < 3; ++x) {
            for(int y = -2; y < 3; ++y) {
                for(int z = -2; z < 3; ++z) {
                    int absX = Math.abs(x);
                    int absY = Math.abs(y);
                    int absZ = Math.abs(z);

                    //prismarine found in correct position
                    if((absX > 1 || absY > 1 || absZ > 1) && (x == 0 && (absY == 2 || absZ == 2) || y == 0 && (absX == 2 || absZ == 2) || z == 0 && (absX == 2 || y == 2))) {
                        BlockPos pos = te.getPos().add(x, y, z);
                        if(te.getWorld().getBlockState(pos).getBlock() == Blocks.PRISMARINE) {
                            positions.add(pos);
                        }
                    }
                }
            }
        }

        setPrismarineSize(te, positions.size());
        final boolean ret = positions.size() >= 16;

        //ambient particles
        if(ret) {
            for(BlockPos pos : positions) {

            }
        }

        return ret;
    }

    protected void addEffectsToPlayers(@Nonnull TileEntityFluidlogged te) {
        if(!te.getWorld().isRemote) {
            final int size = getPrismarineSize(te) / 7 * 16;
            final int x = te.getPos().getX();
            final int y = te.getPos().getY();
            final int z = te.getPos().getZ();

            final AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1).grow(size).expand(0, te.getWorld().getHeight(), 0);
            final List<EntityPlayer> players = te.getWorld().getEntitiesWithinAABB(EntityPlayer.class, aabb);

            for(EntityPlayer player : players) {
                if(player.isWet() && te.getPos().getDistance((int)player.posX, (int)player.posY, (int)player.posZ) <= size) {
                    player.addPotionEffect(new PotionEffect(potion, 260, 0, true, true));
                }
            }
        }
    }

    //-----------------------
    //getters & setters below
    //-----------------------

    protected <T> T getCache(@Nullable TileEntity te, int slot, T defaultRet) {
        if(!(te instanceof TileEntityFluidlogged))                return defaultRet;
        else if(((TileEntityFluidlogged)te).cache == null)        return defaultRet;
        else return (T)((TileEntityFluidlogged)te).cache[slot];
    }

    protected void setCache(@Nullable TileEntity te, int slot, Object in) {
        if(te instanceof TileEntityFluidlogged && ((TileEntityFluidlogged)te).cache != null) {
            ((TileEntityFluidlogged)te).cache[slot] = in;
        }
    }

    public long getNextSoundTime(@Nullable TileEntity te) {
        return getCache(te, 0, 0);
    }

    public void setNextSoundTime(@Nullable TileEntity te, long time) {
        setCache(te, 0, time);
    }

    public int getPrismarineSize(@Nullable TileEntity te) {
        return getCache(te, 1, 0);
    }

    public void setPrismarineSize(@Nullable TileEntity te, int size) {
        setCache(te, 1, size);
    }*/
}
