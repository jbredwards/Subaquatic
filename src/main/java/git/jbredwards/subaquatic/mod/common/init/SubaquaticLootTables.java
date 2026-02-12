/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nonnull;

/**
 * stores all of this mod's loot tables
 * @author jbred
 *
 */
public final class SubaquaticLootTables
{
    @Nonnull public static final ResourceLocation ENTITIES_COD = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/cod"));
    @Nonnull public static final ResourceLocation ENTITIES_FISH = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/fish"));
    @Nonnull public static final ResourceLocation ENTITIES_FROG = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/frog"));
    @Nonnull public static final ResourceLocation ENTITIES_PUFFERFISH = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/pufferfish"));
    @Nonnull public static final ResourceLocation ENTITIES_SALMON = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/salmon"));
    @Nonnull public static final ResourceLocation ENTITIES_TADPOLE = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/tadpole"));
    @Nonnull public static final ResourceLocation ENTITIES_TROPICAL_FISH = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/tropical_fish"));
    @Nonnull public static final ResourceLocation ENTITIES_TURTLE = LootTableList.register(new ResourceLocation(Subaquatic.MODID, "entities/turtle"));
}
