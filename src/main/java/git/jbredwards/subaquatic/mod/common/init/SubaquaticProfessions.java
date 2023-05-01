package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.util.villager.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * stores all of this mod's professions
 * @author jbred
 *
 */
public final class SubaquaticProfessions
{
    //profession init
    @Nonnull public static final List<VillagerProfession> INIT = new LinkedList<>();

    //professions
    @Nonnull public static final VillagerProfession MARINE_BIOLOGIST = register("marine_biologist", career -> career
            .addTrade(1,
                    TradeForEntityBucket.FISH_BUCKET,
                    TradeUtil.newTrade(Items.EMERALD, 1, 1, SubaquaticItems.KELP, 1, 3),
                    TradeUtil.newTrade(Items.EMERALD, 1, 1, SubaquaticItems.GLOW_LICHEN, 2, 3),
                    TradeUtil.newTrade(Items.DYE, 12, 24, Items.EMERALD, 1, 1),
                    TradeUtil.newTrade(SubaquaticItems.DRIED_KELP_BLOCK, 3, 5, Items.EMERALD, 1, 1),
                    TradeUtil.newTrade(Items.EMERALD, 1, 1, new ItemStack(Blocks.WATERLILY), 1, 3))
            .addTrade(2,
                    TradeForEntityBucket.FISH_BUCKET_SPAWNABLE,
                    TradeUtil.newTrade(Items.EMERALD, 3, 3, Items.PRISMARINE_SHARD, 5, 8),
                    TradeUtil.newTrade(Items.EMERALD, 2, 2, SubaquaticItems.SEA_PICKLE, 1, 1),
                    TradeUtil.newTrade(Items.EMERALD, 2, 2, Items.PRISMARINE_SHARD, 5, 8),
                    TradeForCoral.CORAL_BLOCK,
                    TradeUtil.or(TradeForCoral.CORAL_FAN, TradeForCoral.CORAL_FIN))
            .addTrade(3,
                    TradeForEntityBucket.TROPICAL_FISH_BUCKET,
                    TradeUtil.newTrade(Items.EMERALD, 4, 5, Items.PRISMARINE_CRYSTALS, 5, 8),
                    TradeUtil.newTrade(SubaquaticItems.NAUTILUS_SHELL, 2, 4, Items.EMERALD, 2, 3),
                    TradeUtil.newTrade(Items.EMERALD, 7, 10, SubaquaticItems.NAUTILUS_SHELL, 1, 1))
            .addTrade(4,
                    TradeForEntityBucket.TROPICAL_FISH_BUCKET_ANY,
                    TradeUtil.newTrade(Items.EMERALD, 32, 40, new ItemStack(Blocks.SPONGE), 1, 1),
                    TradeForFroglight.FROGLIGHT));

    //register a profession with one career
    @Nonnull
    static VillagerProfession register(@Nonnull String name, @Nonnull Consumer<VillagerCareerRandom> consumer) {
        return registerMulti(name, profession -> consumer.accept(new VillagerCareerRandom(profession, Subaquatic.MODID + "." + name, 2)));
    }

    //register a profession with multiple careers
    @Nonnull
    static VillagerProfession registerMulti(@Nonnull String name, @Nonnull Consumer<VillagerProfession> consumer) {
        final VillagerProfession profession = new VillagerProfession(
                Subaquatic.MODID + ":" + name,
                Subaquatic.MODID + ":textures/entity/villager/" + name + ".png",
                Subaquatic.MODID + ":textures/entity/villager/" + name + "_zombie.png");

        INIT.add(profession);
        consumer.accept(profession);
        return profession;
    }
}
