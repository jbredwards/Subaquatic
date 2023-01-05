package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import git.jbredwards.subaquatic.mod.client.particle.ParticleBubbleColumn;
import git.jbredwards.subaquatic.mod.common.capability.IBubbleColumn;
import git.jbredwards.subaquatic.mod.common.config.util.BubbleColumnPredicate;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Random;

/**
 * WILL BE ADDED IN A FUTURE UPDATE WHEN LESS BUGGY
 * @author jbred
 *
 */
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public class BlockBubbleColumn extends Block implements IFluidloggable, ICustomModel
{
    @Nonnull
    public static final PropertyBool PULL = PropertyBool.create("pull");

    public BlockBubbleColumn(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockBubbleColumn(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        translucent = true;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, PULL); }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(PULL, (meta & 1) == 0); }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) { return state.getValue(PULL) ? 0 : 1; }

    @Override
    public void registerModels() { ModelLoader.setCustomStateMapper(this, block -> new HashMap<>()); }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(@Nonnull IBlockState state) { return EnumBlockRenderType.INVISIBLE; }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Nonnull
    @Override
    public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) { return Items.AIR; }

    @Override
    public int quantityDropped(@Nonnull Random random) { return 0; }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) { return false; }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) { return false; }

    @Override
    public boolean canCollideCheck(@Nonnull IBlockState state, boolean hitIfLiquid) { return hitIfLiquid; }

    @Override
    public boolean isFluidValid(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Fluid fluid) {
        return canExist(state.getValue(PULL), getSoil(world, pos), fluid);
    }

    public boolean canExist(boolean isPull, @Nonnull IBlockState soil, @Nullable Fluid fluid) {
        final @Nullable BubbleColumnPredicate conditions = SubaquaticConfigHandler.getBubbleColumnConditions(soil.getBlock(), isPull);
        return conditions != null && conditions.test(soil, fluid);
    }

    @Nonnull
    public IBlockState getSoil(@Nonnull World world, @Nonnull BlockPos pos) {
        IBlockState soil; while(isEqualTo((soil = world.getBlockState(pos.down())).getBlock(), this)) pos = pos.down();
        return soil;
    }

    @Nonnull
    @Override
    public EnumActionResult onFluidDrain(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState here, int blockFlags) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), blockFlags);
        return EnumActionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void onBlockAdded(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) { worldIn.scheduleUpdate(pos, this, 1); }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos) {
        if(fromPos.equals(pos.up()) || fromPos.equals(pos.down())) worldIn.scheduleUpdate(pos, this, 1);
    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        final @Nullable Fluid fluidHere = FluidState.get(worldIn, pos).getFluid();
        final IBlockState downState = worldIn.getBlockState(pos.down());
        final boolean isPull = state.getValue(PULL);

        if(isEqualTo(downState.getBlock(), this) && isPull == downState.getValue(PULL) || canExist(isPull, downState, fluidHere)) spreadToUp(worldIn, pos, state);
        else if(isEqualTo(downState.getBlock(), this) && isPull == !downState.getValue(PULL) || canExist(!isPull, downState, fluidHere))
            worldIn.setBlockState(pos, state.withProperty(PULL, !isPull));

        else worldIn.setBlockToAir(pos);
    }

    public void spreadToUp(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        final IBlockState upState = worldIn.getBlockState(pos.up());
        final @Nullable Fluid fluid = FluidloggedUtils.getFluidFromState(upState);
        if(fluid != null && isFluidValid(state, worldIn, pos.up(), fluid) && FluidloggedUtils.isFluidloggableFluid(upState, worldIn, pos.up()))
            worldIn.setBlockState(pos.up(), state, 2);
    }

    @Override
    public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn) {
        final boolean isPull = state.getValue(PULL);
        if(pos.getY() == (int)entityIn.posY) {
            if(isPull) entityIn.motionY = Math.max(-1, entityIn.motionY - 0.08);
            else entityIn.motionY = Math.min(1.8, entityIn.motionY + 0.1);
        }

        if(!worldIn.isRemote && entityIn instanceof EntityLivingBase) {
            if(entityIn.isInsideOfMaterial(Material.WATER)) entityIn.setAir(300);
            //checks if the player was in a bubble column last tick
            final @Nullable IBubbleColumn cap = IBubbleColumn.get(entityIn);
            if(cap != null && !cap.isInBubbleColumn()) {
                cap.setInBubbleColumn(true);
                worldIn.playSound(null, pos, isPull
                        ? SubaquaticSounds.BUBBLE_COLUMN_DOWN_INSIDE
                        : SubaquaticSounds.BUBBLE_COLUMN_UP_INSIDE, SoundCategory.BLOCKS, 0.5f, 1.0f);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(@Nonnull IBlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        final boolean isPull = stateIn.getValue(PULL);
        //down particles
        if(isPull) {
            Minecraft.getMinecraft().effectRenderer.addEffect(
                    new ParticleBubbleColumn(worldIn, pos.getX() + 0.5, pos.getY() + rand.nextFloat() - 0.125, pos.getZ() + 0.5, 12, -1.0 / 30, 0.4, rand.nextInt(360)));
        }
        //up particles
        else {
            worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0, 0.3, 0.0);
            worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0.0, 0.3, 0.0);
        }
        //ambient sound effect
        if(rand.nextInt(200) == 0)
            worldIn.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, isPull
                            ? SubaquaticSounds.BUBBLE_COLUMN_DOWN_AMBIENT
                            : SubaquaticSounds.BUBBLE_COLUMN_UP_AMBIENT,
                    SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.2f,
                    0.9f + rand.nextFloat() * 0.15f, false);
    }

    /*@SubscribeEvent
    static void updateIsInBubbleColumn(@Nonnull LivingEvent.LivingUpdateEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        final @Nullable IBubbleColumn cap = IBubbleColumn.get(entity);
        if(cap != null && cap.isInBubbleColumn()) cap.setInBubbleColumn(entity.world.isMaterialInBB(
                entity.getEntityBoundingBox(), ModBlocks.BUBBLE_COLUMN_MATERIAL));
    }

    @SubscribeEvent
    static void updateMagmaAndSoulSand(@Nonnull BlockEvent.NeighborNotifyEvent event) {
        if(event.getNotifiedSides().contains(EnumFacing.UP)) {
            final boolean isPull = ModBlocks.BUBBLE_COLUMN.canExist(true, event.getState(), null);
            if(isPull || ModBlocks.BUBBLE_COLUMN.canExist(false, event.getState(), null)) {
                ModBlocks.BUBBLE_COLUMN.spreadToUp(event.getWorld(), event.getPos(), ModBlocks.BUBBLE_COLUMN.getDefaultState().withProperty(PULL, isPull));
                return;
            }
        }

        if(event.getNotifiedSides().contains(EnumFacing.DOWN)) {
            final IBlockState down = event.getWorld().getBlockState(event.getPos().down());
            final boolean isPull = ModBlocks.BUBBLE_COLUMN.canExist(true, down, null);
            if(isPull || ModBlocks.BUBBLE_COLUMN.canExist(false, down, null))
                ModBlocks.BUBBLE_COLUMN.spreadToUp(event.getWorld(), event.getPos().down(), ModBlocks.BUBBLE_COLUMN.getDefaultState().withProperty(PULL, isPull));
        }
    }*/
}
