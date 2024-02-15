/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.api.block.IOxygenSupplier;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import git.jbredwards.subaquatic.mod.client.particle.ParticleBubbleColumnDown;
import git.jbredwards.subaquatic.mod.client.particle.ParticleBubbleColumnUp;
import git.jbredwards.subaquatic.mod.common.capability.IBubbleColumn;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public class BlockBubbleColumn extends Block implements IFluidloggable, ICustomModel, IOxygenSupplier
{
    public final boolean isDown;
    public BlockBubbleColumn(@Nonnull Material materialIn, boolean isDownIn) {
        this(materialIn, materialIn.getMaterialMapColor(), isDownIn);
    }

    public BlockBubbleColumn(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn, boolean isDownIn) {
        super(materialIn, mapColorIn);
        isDown = isDownIn;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() { ModelLoader.setCustomStateMapper(this, block -> Collections.emptyMap()); }

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
        return fluid.canBePlacedInWorld() && fluid.getBlock().getDefaultState().getMaterial() == Material.WATER;
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
    public int tickRate(@Nonnull World worldIn) { return 5; }

    @Override
    public void onBlockAdded(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos) {
        if(fromPos.equals(pos.up()) || fromPos.equals(pos.down())) worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        final IBlockState soil = worldIn.getBlockState(pos.down());
        if(soil.getBlock() == this || isValidSoil(soil)) trySpreadTo(worldIn, pos.up());
        else worldIn.setBlockToAir(pos);
    }

    public boolean isValidSoil(@Nonnull IBlockState soil) {
        return isDown ? SubaquaticConfigHandler.BUBBLE_COLUMN_SOIL_DOWN.contains(soil) : SubaquaticConfigHandler.BUBBLE_COLUMN_SOIL_UP.contains(soil);
    }

    public void trySpreadTo(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        final IBlockState above = worldIn.getBlockState(pos);
        if(above.getBlock() != this && above.getBlock().isReplaceable(worldIn, pos)) {
            final FluidState aboveFluid = FluidloggedUtils.getFluidState(worldIn, pos, above);

            if(!aboveFluid.isEmpty()
            && isFluidValid(getDefaultState(), worldIn, pos, aboveFluid.getFluid())
            && FluidloggedUtils.isFluidloggableFluid(aboveFluid.getState(), worldIn, pos))
                worldIn.setBlockState(pos, getDefaultState(), 2);
        }
    }

    @Override
    public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn) {
        final @Nullable IBubbleColumn cap = IBubbleColumn.get(entityIn);
        if(cap != null) {
            //handle "air on top" bubble column collision
            final IBlockState above = worldIn.getBlockState(pos.up());
            if(above.getBlock().isAir(above, worldIn, pos.up())) {
                cap.onCollideTop(entityIn, this);
                if(worldIn instanceof WorldServer) {
                    ((WorldServer)worldIn).spawnParticle(EnumParticleTypes.WATER_SPLASH, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 2, 0.25, 0, 0.25, 1);
                    ((WorldServer)worldIn).spawnParticle(EnumParticleTypes.WATER_BUBBLE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 2, 0.25, 0, 0.25, 0.2);
                }
            }

            //handle normal bubble column collision
            else cap.onCollide(entityIn, this);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(@Nonnull IBlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        spawnParticles(worldIn, pos, rand);
        if(rand.nextInt(200) == 0)
            worldIn.playSound(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                getAmbientSound(),
                SoundCategory.BLOCKS,
                0.2f + rand.nextFloat() * 0.2f,
                0.9f + rand.nextFloat() * 0.15f,
                false
        );
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticles(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        final ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
        //down particles
        if(isDown) manager.addEffect(ParticleBubbleColumnDown.FACTORY.generate(worldIn, pos.getX() + 0.5, pos.getY() + rand.nextFloat() - 0.125, pos.getZ() + 0.5, 12, -1.0 / 30, 0.4, rand.nextInt(360)));
        //up particles
        else {
            manager.addEffect(ParticleBubbleColumnUp.FACTORY.generate(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0, 0.4, 0.0));
            manager.addEffect(ParticleBubbleColumnUp.FACTORY.generate(worldIn, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0.0, 0.4, 0.0));
        }
    }

    @Nonnull
    public SoundEvent getAmbientSound() { return isDown ? SubaquaticSounds.BUBBLE_COLUMN_DOWN_AMBIENT : SubaquaticSounds.BUBBLE_COLUMN_UP_AMBIENT; }

    @Nonnull
    public SoundEvent getInsideSound() { return isDown ? SubaquaticSounds.BUBBLE_COLUMN_DOWN_INSIDE : SubaquaticSounds.BUBBLE_COLUMN_UP_INSIDE; }

    @SubscribeEvent
    static void generateBubbleColumns(@Nonnull BlockEvent.NeighborNotifyEvent event) {
        if(!event.getWorld().isRemote) {
            //placing bubble column support block into fluid
            if(event.getNotifiedSides().contains(EnumFacing.UP)) {
                if(SubaquaticBlocks.BUBBLE_COLUMN_UP.isValidSoil(event.getState())) SubaquaticBlocks.BUBBLE_COLUMN_UP.trySpreadTo(event.getWorld(), event.getPos().up());
                else if(SubaquaticBlocks.BUBBLE_COLUMN_DOWN.isValidSoil(event.getState())) SubaquaticBlocks.BUBBLE_COLUMN_DOWN.trySpreadTo(event.getWorld(), event.getPos().up());
            }
            //placing fluid onto bubble column support block
            if((event.getState().getBlock() instanceof BlockBubbleColumn || FluidloggedUtils.isFluid(event.getState())) && event.getNotifiedSides().contains(EnumFacing.DOWN)) {
                final IBlockState state = event.getWorld().getBlockState(event.getPos().down());
                if(SubaquaticBlocks.BUBBLE_COLUMN_UP.isValidSoil(state)) SubaquaticBlocks.BUBBLE_COLUMN_UP.trySpreadTo(event.getWorld(), event.getPos());
                else if(SubaquaticBlocks.BUBBLE_COLUMN_DOWN.isValidSoil(state)) SubaquaticBlocks.BUBBLE_COLUMN_DOWN.trySpreadTo(event.getWorld(), event.getPos());
            }
        }
    }
}
