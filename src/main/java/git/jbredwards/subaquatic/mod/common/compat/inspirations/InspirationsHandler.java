package git.jbredwards.subaquatic.mod.common.compat.inspirations;

import knightminer.inspirations.common.Config;
import knightminer.inspirations.library.Util;
import knightminer.inspirations.recipes.InspirationsRecipes;
import knightminer.inspirations.recipes.RecipesClientProxy;
import knightminer.inspirations.recipes.tileentity.TileCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class InspirationsHandler
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    static void overrideCauldronWrapper(@Nonnull ModelRegistryEvent event) {
        if(InspirationsRecipes.cauldron != null) {
            ModelLoaderRegistry.registerLoader(InspirationsModelCauldron.Loader.INSTANCE);
            ModelLoader.setCustomStateMapper(InspirationsRecipes.cauldron, new RecipesClientProxy.CauldronStateMapper(Util.getResource("cauldron_multilayer")));
        }
    }

    public static boolean doesCauldronHaveMaterial(@Nonnull Material material, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        if(!Config.enableExtendedCauldron) return material == Material.WATER;
        final TileEntity tile = world.getTileEntity(pos);

        if(tile instanceof TileCauldron) {
            final TileCauldron cauldron = (TileCauldron)tile;
            if(cauldron.isWater() || cauldron.getState().getColor() != -1 || cauldron.getState().getPotion() != null)
                return material == Material.WATER;

            final Fluid fluid = cauldron.getState().getFluid();
            return fluid != null && fluid.canBePlacedInWorld() && material == fluid.getBlock().getDefaultState().getMaterial();
        }

        return false;
    }

    public static int getCauldronColor(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        if(Config.enableExtendedCauldron) {
            final TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileCauldron) {
                final TileCauldron cauldron = (TileCauldron)tile;
                if(!cauldron.isWater()) return cauldron.getColor();
            }
        }

        return BiomeColorHelper.getWaterColorAtPos(world, pos);
    }
}
