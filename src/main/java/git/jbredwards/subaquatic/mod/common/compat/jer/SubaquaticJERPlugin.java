package git.jbredwards.subaquatic.mod.common.compat.jer;

import git.jbredwards.subaquatic.mod.common.entity.living.EntityCod;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityFish;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityPufferfish;
import git.jbredwards.subaquatic.mod.common.entity.living.EntitySalmon;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
import jeresources.api.IJERAPI;
import jeresources.api.IMobRegistry;
import jeresources.api.JERPlugin;
import jeresources.api.conditionals.LightLevel;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class SubaquaticJERPlugin {
    @JERPlugin
    public static IJERAPI JERApi;

    public static void init() {
        IMobRegistry registry = JERApi.getMobRegistry();
        World world = JERApi.getWorld();

        String[] codSpawnBiomes = getBiomeNames(Biomes.OCEAN, Biomes.DEEP_OCEAN, SubaquaticBiomes.COLD_OCEAN, SubaquaticBiomes.DEEP_COLD_OCEAN, SubaquaticBiomes.LUKEWARM_OCEAN, SubaquaticBiomes.DEEP_LUKEWARM_OCEAN);
        registry.register(new EntityCod(world), LightLevel.any, codSpawnBiomes, EntityCod.LOOT);

        //No specified spawn biomes, so it will revert to "Any"
        registry.register(new EntityFish(world), LightLevel.any, EntityFish.LOOT);

        String[] pufferfishSpawnBiomes = getBiomeNames(SubaquaticBiomes.LUKEWARM_OCEAN, SubaquaticBiomes.DEEP_LUKEWARM_OCEAN, SubaquaticBiomes.WARM_OCEAN, SubaquaticBiomes.DEEP_WARM_OCEAN);
        registry.register(new EntityPufferfish(world), LightLevel.any, pufferfishSpawnBiomes, EntityPufferfish.LOOT);

        String[] salmonSpawnBiomes = getBiomeNames(Biomes.RIVER, Biomes.FROZEN_RIVER, Biomes.FROZEN_OCEAN, SubaquaticBiomes.DEEP_FROZEN_OCEAN, SubaquaticBiomes.COLD_OCEAN, SubaquaticBiomes.DEEP_COLD_OCEAN);
        registry.register(new EntitySalmon(world), LightLevel.any, salmonSpawnBiomes, EntitySalmon.LOOT);
    }

    private static String[] getBiomeNames(Biome... biomes) {
        List<String> biomeNames = new ArrayList<>();

        for(Biome biome : biomes) {
            biomeNames.add(biome.getBiomeName());
        }

        return biomeNames.size() > 0 ? biomeNames.toArray(new String[0]) : new String[] {"jer.any"};
    }
}
