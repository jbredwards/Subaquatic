/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.ocean_api.api.OceanAPI;
import git.jbredwards.subaquatic.mod.common.block.BlockMangroveSapling;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.mangrove.WorldGenGrassDisk;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.mangrove.WorldGenMangroveTree;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFossils;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BiomeMangroveSwamp extends Biome
{
    @Nonnull
    public static WorldGenAbstractTree TREE = new WorldGenMangroveTree(false, false), TALL_TREE = new WorldGenMangroveTree(false, true);
    public BiomeMangroveSwamp(@Nonnull final BiomeProperties properties) {
        super(properties);
        OceanAPI.registerWaterFogColor(this, 5077600);
        spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 1, 1));
        // handled via entity init: spawnableWaterCreatureList.add(new SpawnListEntry(EntityTropicalFish.class, 25, 8, 8));
        // handled via entity init: spawnableCreatureList.add(new SpawnListEntry(EntityFrog.class, 10, 2, 5));

        topBlock = SubaquaticBlocks.MUD.getDefaultState();
        fillerBlock = topBlock;

        decorator.treesPerChunk = 25;
        decorator.extraTreeChance = 0;
        decorator.flowersPerChunk = 0;
        decorator.deadBushPerChunk = 1;
        decorator.clayPerChunk = 1;
        decorator.waterlilyPerChunk = 4;
        decorator.sandPatchesPerChunk = 0;
        decorator.gravelPatchesPerChunk = 0;
        decorator.grassPerChunk = 5;
    }

    @Override
    public void decorate(@Nonnull final World worldIn, @Nonnull final Random rand, @Nonnull final BlockPos pos) {
        new WorldGenGrassDisk().generate(worldIn, rand, worldIn.getTopSolidOrLiquidBlock(pos.add(rand.nextInt(16) + 8, -1, rand.nextInt(16) + 8)));
        super.decorate(worldIn, rand, pos);

        if(TerrainGen.decorate(worldIn, rand, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.FOSSIL) && rand.nextInt(64) == 0) new WorldGenFossils().generate(worldIn, rand, pos);
    }

    @Nonnull
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(@Nonnull final Random rand) {
        return rand.nextDouble() < BlockMangroveSapling.TALL_CHANCE ? TALL_TREE : TREE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(@Nonnull final BlockPos pos) {
        return getModdedBiomeFoliageColor(9285927);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(@Nonnull final BlockPos pos) {
        return getModdedBiomeGrassColor(GRASS_COLOR_NOISE.getValue(pos.getX() * 0.0225, pos.getZ() * 0.0225) < -0.1 ? 5011004 : 6975545);
    }
}
