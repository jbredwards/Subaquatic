package git.jbredwards.fluidlogged_additions.common.block;

import git.jbredwards.fluidlogged.common.block.IFluidloggable;
import git.jbredwards.fluidlogged.util.FluidloggedUtils;
import git.jbredwards.fluidlogged_additions.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("NullableProblems")
public class BlockNautilusShell extends Block implements IFluidloggable
{
    public static final PropertyEnum<Rot> ROTATION = PropertyEnum.create("rotation", Rot.class);
    public static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.09375, 0.9375);
    public static final SoundType SOUND = new SoundType(1, 1, SoundEvents.ENTITY_ITEM_PICKUP, SoundEvents.BLOCK_STONE_STEP, SoundEvents.ENTITY_ITEM_PICKUP, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

    public BlockNautilusShell(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        setSoundType(SOUND);
        setDefaultState(getDefaultState().withProperty(ROTATION, Rot.DEG_0));
    }

    public BlockNautilusShell(Material materialIn) {
        super(materialIn);
        setSoundType(SOUND);
        setDefaultState(getDefaultState().withProperty(ROTATION, Rot.DEG_0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(ROTATION).build();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ROTATION).ordinal();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ROTATION, Rot.values()[Math.min(meta, 7)]);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(ROTATION, state.getValue(ROTATION).rotate(rot));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withProperty(ROTATION, state.getValue(ROTATION).mirror(mirrorIn));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return !Minecraft.getMinecraft().player.isCreative();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return !Minecraft.getMinecraft().player.isCreative();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!canPlaceBlockAt(worldIn, pos)) {
            worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1, 1);
            FluidloggedUtils.setStoredOrReal(worldIn, pos, state, null, true);
            dropBlockAsItem(worldIn, pos, state, 0);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if(placer.isSneaking()) return getDefaultState().withProperty(ROTATION, Rot.fromRotation(placer.rotationYawHead + 180));
        else return getDefaultState().withProperty(ROTATION, Rot.fromRotation(placer.rotationYawHead));
    }

    public enum Rot implements IStringSerializable
    {
        DEG_0  ("0",   0),
        DEG_45 ("45",  45),
        DEG_90 ("90",  90),
        DEG_135("135", 135),
        DEG_180("180", 180),
        DEG_225("225", 225),
        DEG_270("270", 270),
        DEG_315("315", 315);

        public final String name;
        public final int angle;

        Rot(String name, int angle) {
            this.name = name;
            this.angle = angle;
        }

        @Override
        public String getName() {
            return name;
        }

        public Rot rotate(Rotation rot) {
            return rotate(fromRotation(rot));
        }

        public Rot rotate(Rot rot) {
            return rotate(rot.angle);
        }

        public Rot rotate(float rot) {
            return fromRotation(angle + rot);
        }

        //gets the Rot from entity rotation
        public static Rot fromRotation(float rot) {
            //angle must be between 0 - 360.
            if(rot > 360) return fromRotation(rot - 360);
            else if(rot < 0) return fromRotation(rot + 360);

            //gets the closest value
            final int closest = (int)Util.closest(rot, 0, 45, 90, 135, 180, 225, 270, 315, 360);

            //gets the Rot from the closest
            switch(closest) {
                case 45:  return DEG_45;
                case 90:  return DEG_90;
                case 135: return DEG_135;
                case 180: return DEG_180;
                case 225: return DEG_225;
                case 270: return DEG_270;
                case 315: return DEG_315;
                default:  return DEG_0;
            }
        }

        //converts from Rotation to Rot
        public static Rot fromRotation(Rotation rot) {
            switch(rot) {
                case CLOCKWISE_90:        return DEG_90;
                case CLOCKWISE_180:       return DEG_180;
                case COUNTERCLOCKWISE_90: return DEG_270;
                default:                  return DEG_0;
            }
        }

        //mirrors the rot
        public Rot mirror(Mirror mirror) {
            switch(mirror) {
                case LEFT_RIGHT: {
                    switch(this) {
                        case DEG_45:  return DEG_315;
                        case DEG_90:  return DEG_270;
                        case DEG_135: return DEG_225;
                        case DEG_225: return DEG_135;
                        case DEG_315: return DEG_45;
                        default:      return this;
                    }
                }
                case FRONT_BACK: {
                    switch(this) {
                        case DEG_0:   return DEG_180;
                        case DEG_45:  return DEG_135;
                        case DEG_135: return DEG_45;
                        case DEG_180: return DEG_0;
                        case DEG_225: return DEG_315;
                        case DEG_315: return DEG_225;
                        default:      return this;
                    }
                }
                //no mirror
                default: return this;
            }
        }
    }
}
