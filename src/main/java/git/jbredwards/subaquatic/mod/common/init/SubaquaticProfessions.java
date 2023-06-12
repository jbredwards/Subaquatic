package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.util.villager.career.*;
import git.jbredwards.subaquatic.mod.common.entity.util.villager.profession.IProfessionSupplier;
import git.jbredwards.subaquatic.mod.common.entity.util.villager.profession.VillagerProfessionMarineBiologist;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
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
    @Nonnull public static final VillagerProfessionMarineBiologist MARINE_BIOLOGIST = register("marine_biologist", VillagerProfessionMarineBiologist::new, career -> career
            .addTrade(1,
                    TradeForEntityBucket.FISH_BUCKET,
                    TradeUtil.newTrade(Items.EMERALD, 1, 1, SubaquaticItems.KELP, 1, 3),
                    TradeUtil.newTrade(Items.EMERALD, 1, 1, SubaquaticItems.GLOW_LICHEN, 2, 3),
                    TradeUtil.newTrade(SubaquaticItems.DRIED_KELP_BLOCK, 3, 5, Items.EMERALD, 1, 1),
                    TradeUtil.newTrade(Items.EMERALD, 1, 1, new ItemStack(Blocks.WATERLILY), 1, 3),
                    TradeUtil.newTrade(Items.EMERALD, 1, 1, SubaquaticItems.SEAGRASS, 3, 5))//,
                    //TODO TradeUtil.of(random -> new MerchantRecipe(new ItemStack(Items.EMERALD, MathHelper.getInt(random, 2, 4)), new ItemStack(Items.DYE, 1, 15), new ItemStack(SubaquaticItems.AQUATIC_BONE_MEAL))))
            .addTrade(2,
                    TradeForCoral.CORAL_BLOCK,
                    TradeForEntityBucket.FISH_BUCKET_SPAWNABLE,
                    TradeUtil.newTrade(Items.EMERALD, 3, 3, Items.PRISMARINE_SHARD, 5, 8),
                    TradeUtil.newTrade(Items.EMERALD, 2, 2, SubaquaticItems.SEA_PICKLE, 1, 1),
                    TradeUtil.newTrade(Items.EMERALD, 2, 2, Items.PRISMARINE_SHARD, 5, 8),
                    TradeUtil.or(TradeForCoral.CORAL_FAN, TradeForCoral.CORAL_FIN))
            .addTrade(3,
                    TradeForEntityBucket.TROPICAL_FISH_BUCKET,
                    TradeUtil.newTrade(Items.EMERALD, 4, 5, Items.PRISMARINE_CRYSTALS, 5, 8),
                    TradeUtil.newTrade(SubaquaticItems.NAUTILUS_SHELL, 2, 4, Items.EMERALD, 1, 1),
                    TradeUtil.newTrade(Items.EMERALD, 7, 10, SubaquaticItems.NAUTILUS_SHELL, 1, 1))
            .addTrade(4,
                    TradeForEntityBucket.TROPICAL_FISH_BUCKET_ANY,
                    TradeUtil.newTrade(Items.EMERALD, 32, 40, new ItemStack(Blocks.SPONGE), 1, 1),
                    TradeUtil.of(random -> new MerchantRecipe(new ItemStack(Items.EMERALD, MathHelper.getInt(random, 20, 25)), new ItemStack(Items.MAGMA_CREAM, 4), new ItemStack(SubaquaticItems.FROGLIGHT, 1, random.nextInt(3))))));

    //add additional trades
    static void handleAdditionalTrades() {
        //new fisherman trade for ink sacs
        VillagerRegistry.FARMER.getCareer(1).addTrade(2, TradeUtil.newTrade(Items.DYE, 12, 24, Items.EMERALD, 1, 1));
    }

    //register a profession with one career
    @Nonnull
    static <T extends VillagerProfession> T register(@Nonnull String name, @Nonnull IProfessionSupplier<T> supplier, @Nonnull Consumer<VillagerCareerRandom> consumer) {
        return registerMulti(name, supplier, profession -> consumer.accept(new VillagerCareerRandom(profession, Subaquatic.MODID + "." + name, 2)));
    }

    //register a profession with multiple careers
    @Nonnull
    static <T extends VillagerProfession> T registerMulti(@Nonnull String name, @Nonnull IProfessionSupplier<T> supplier, @Nonnull Consumer<T> consumer) {
        final T profession = supplier.get(
                Subaquatic.MODID + ":" + name,
                Subaquatic.MODID + ":textures/entity/villager/" + name + ".png",
                Subaquatic.MODID + ":textures/entity/villager/" + name + "_zombie.png");

        INIT.add(profession);
        consumer.accept(profession);
        return profession;
    }
}
