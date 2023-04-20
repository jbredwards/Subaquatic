package git.jbredwards.subaquatic.mod.common.recipe;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.message.SMessageBottleParticles;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public class BlockSoakRecipe
{
    @Nonnull public static final List<BlockSoakRecipe> RECIPES = new LinkedList<>();

    @Nonnull public final List<PotionType> potionTypes;
    @Nonnull public final List<Pair<IBlockState, ItemStack>> inputs;
    @Nonnull public final Pair<IBlockState, ItemStack> output;

    public BlockSoakRecipe(@Nonnull List<Pair<IBlockState, ItemStack>> inputsIn, @Nonnull Pair<IBlockState, ItemStack> outputIn) {
        this(Collections.singletonList(PotionTypes.WATER), inputsIn, outputIn);
    }

    public BlockSoakRecipe(@Nonnull List<PotionType> potionTypesIn, @Nonnull List<Pair<IBlockState, ItemStack>> inputsIn, @Nonnull Pair<IBlockState, ItemStack> outputIn) {
        potionTypes = Collections.unmodifiableList(potionTypesIn);
        inputs = Collections.unmodifiableList(inputsIn);
        output = outputIn;
    }

    public boolean checkRecipe(@Nonnull IBlockState state, @Nonnull PotionType potion) {
        if(!potionTypes.contains(potion)) return false;
        for(Pair<IBlockState, ItemStack> validInput : inputs) if(validInput.getLeft() == state) return true;
        return false;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull IBlockState oldState, @Nonnull World world, @Nonnull BlockPos pos, @Nullable EntityPlayer player) {
        if(!world.isRemote) {
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.PLAYERS, 1, 1);
            playSoakEffects(stack, oldState, world, pos);

            /*IProperty<EnumFacing> rotationProp = null;
            EnumFacing oldRotation = null;
            for(Map.Entry<IProperty<?>, Comparable<?>> entry : oldState.getProperties().entrySet()) {
                if(entry.getKey().getName().equals("rotation") && entry.getKey().getValueClass() == EnumFacing.class) {
                    rotationProp = (IProperty<EnumFacing>)entry.getKey();
                    oldRotation = (EnumFacing)entry.getValue();
                }
            }

            IBlockState newState = output.getLeft();
            if(rotationProp != null && newState.getPropertyKeys().contains(rotationProp))
                newState = newState.withProperty(rotationProp, oldRotation);*/

            world.setBlockState(pos, output.getLeft());
        }

        if(player == null || !player.isCreative()) return new ItemStack(Items.GLASS_BOTTLE);
        else return stack;
    }

    protected void playSoakEffects(@Nonnull ItemStack stack, @Nonnull IBlockState oldState, @Nonnull World world, @Nonnull BlockPos pos) {
        //adds special effects for magma-type blocks
        if(oldState.getBlock() instanceof BlockMagma
        || oldState.getMaterial() == Material.FIRE
        || oldState.getMaterial() == Material.LAVA
        || oldState.getBlock().isBurning(world, pos)
        || oldState.getLightValue(world, pos) != 0 && output.getLeft().getLightValue() == 0) {
            world.playEvent(Constants.WorldEvents.FIRE_EXTINGUISH_SOUND, pos, 0);
            ((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.3, 0.3, 0.3, 0);
        }

        //adds generic splash effects for everything else
        else Subaquatic.WRAPPER.sendToAllAround(
            new SMessageBottleParticles(pos, PotionUtils.getColor(stack)),
            new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 64)
        );
    }

    @Nonnull
    public static IBehaviorDispenseItem getDispenserHandler() {
        return new BehaviorDefaultDispenseItem() {
            @Nonnull
            @Override
            protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
                final EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING).getOpposite();
                if(facing != EnumFacing.UP) {
                    final PotionType potion = PotionUtils.getPotionFromItem(stack);
                    if(potion != null) {
                        final BlockPos pos = source.getBlockPos().offset(facing);
                        final IBlockState state = source.getWorld().getBlockState(pos);

                        for(BlockSoakRecipe recipe : RECIPES) {
                            if(recipe.checkRecipe(state, potion)) {
                                final ItemStack result = recipe.apply(stack, state, source.getWorld(), pos, null);
                                if(stack.getCount() == 1) return result;
                                else if(((TileEntityDispenser)source.getBlockTileEntity()).addItemStack(result) < 0) {
                                    super.dispenseStack(source, result);
                                }

                                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
                            }
                        }
                    }
                }

                return super.dispenseStack(source, stack);
            }
        };
    }

    @SubscribeEvent
    static void onBottleRightClick(@Nonnull PlayerInteractEvent.RightClickBlock event) {
        if(event.getFace() != EnumFacing.DOWN) {
            final ItemStack stack = event.getItemStack();
            if(stack.getItem() == Items.POTIONITEM) {
                final PotionType potion = PotionUtils.getPotionFromItem(stack);
                if(potion != null) {
                    final IBlockState state = event.getWorld().getBlockState(event.getPos());
                    for(BlockSoakRecipe recipe : RECIPES) {
                        if(recipe.checkRecipe(state, potion)) {
                            final EntityPlayer player = event.getEntityPlayer();
                            final ItemStack result = recipe.apply(stack, state, event.getWorld(), event.getPos(), player);
                            if(result != stack) {
                                stack.shrink(1);
                                if(stack.isEmpty()) player.setHeldItem(event.getHand(), result);
                                else ItemHandlerHelper.giveItemToPlayer(player, result);
                            }

                            event.setCancellationResult(EnumActionResult.SUCCESS);
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}
