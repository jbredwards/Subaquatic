package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public abstract class AbstractBlockCoral extends Block implements IGrowable
{
    @Nonnull public static final PropertyBool ALIVE = PropertyBool.create("alive");
    @Nonnull public final Fluid neededFluid;

    public AbstractBlockCoral(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn) {
        this(neededFluidIn, materialIn, materialIn.getMaterialMapColor());
    }

    public AbstractBlockCoral(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        neededFluid = neededFluidIn;

        setTickRandomly(true);
    }

    @Nonnull
    @Override
    protected abstract BlockStateContainer createBlockState();

    @Nonnull
    @Override
    public abstract IBlockState getStateFromMeta(int meta);

    @Override
    public abstract int getMetaFromState(@Nonnull IBlockState state);

    @Override
    public int damageDropped(@Nonnull IBlockState state) { return state.getValue(ALIVE) ? 0 : 1; }

    @Nonnull
    @Override
    public SoundType getSoundType(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nullable Entity entity) {
        return state.getValue(ALIVE) ? SubaquaticSounds.CORAL : SoundType.STONE;
    }

    @Nonnull
    @Override
    public MapColor getMapColor(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return state.getValue(ALIVE) ? blockMapColor : MapColor.STONE;
    }

    @Nonnull
    @Override
    public Material getMaterial(@Nonnull IBlockState state) { return state.getValue(ALIVE) ? material : Material.ROCK; }

    @Nonnull
    @Override
    public EnumPushReaction getPushReaction(@Nonnull IBlockState state) { return EnumPushReaction.NORMAL; }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        if(state.getValue(ALIVE) && !hasNeededFluid(worldIn, pos, state))
            worldIn.setBlockState(pos, state.withProperty(ALIVE, false));
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return (SubaquaticConfigHandler.Common.Block.deadCoralBonemeal || state.getValue(ALIVE)) && hasNeededFluid(worldIn, pos, state);
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return true;
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(state.getValue(ALIVE)) onGrowSpread(worldIn, rand, pos, state);
        else if(SubaquaticConfigHandler.Common.Block.deadCoralBonemeal) worldIn.setBlockState(pos, state.withProperty(ALIVE, true));
    }

    public abstract boolean hasNeededFluid(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state);
    protected void onGrowSpread(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {}
}
