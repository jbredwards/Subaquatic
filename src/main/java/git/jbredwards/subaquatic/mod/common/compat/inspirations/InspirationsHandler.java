/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.compat.inspirations;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import knightminer.inspirations.common.Config;
import knightminer.inspirations.library.Util;
import knightminer.inspirations.recipes.InspirationsRecipes;
import knightminer.inspirations.recipes.RecipesClientProxy;
import knightminer.inspirations.recipes.tileentity.TileCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.awt.*;

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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    static void ensureOldPotionFluidTexture(@Nonnull TextureStitchEvent.Pre event) {
        if(event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) event.getMap().registerSprite(Util.getResource("blocks/fluid_potion"));
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

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static Color getParticleColorAt(@Nonnull World world, double x, double y, double z) {
        if(Config.enableExtendedCauldron) {
            final Pair<BlockPos, TileEntity> here = SubaquaticWaterColorConfig.findClosestAround(World::getTileEntity, tile -> tile instanceof TileCauldron, world, x, y, z);
            if(here != null) {
                final TileCauldron cauldron = (TileCauldron)here.getRight();
                if(!cauldron.isWater()) return new Color(cauldron.getColor());
            }
        }

        return SubaquaticWaterColorConfig.getParticleColorAt(world, x, y, z);
    }
}
