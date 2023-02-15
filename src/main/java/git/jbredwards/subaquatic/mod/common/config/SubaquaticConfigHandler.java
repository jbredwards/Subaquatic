package git.jbredwards.subaquatic.mod.common.config;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.util.BubbleColumnPredicate;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author jbred
 *
 */
@Config(modid = Subaquatic.MODID)
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public final class SubaquaticConfigHandler
{
    @Config.SlidingOption
    @Config.RangeInt(min = 0, max = 15)
    @Config.LangKey("config.subaquatic.biomeColorBlendRadius")
    public static int biomeColorBlendRadius = 5;

    @Config.LangKey("config.subaquatic.cauldronFluidPhysics")
    public static boolean cauldronFluidPhysics = true;

    @Config.Ignore //wip, crashes with nothirium
    @Config.LangKey("config.subaquatic.coloredRain")
    public static boolean coloredRain = true;

    @Config.RangeInt(min = 0, max = 64)
    @Config.LangKey("config.subaquatic.oceanBiomeSize")
    public static int oceanBiomeSize = 6;

    @Config.LangKey("config.subaquatic.playBubblePopSound")
    public static boolean playBubblePopSound = false;

    @Config.LangKey("config.subaquatic.translucentXPOrbs")
    public static boolean translucentXPOrbs = true;

    @Config.Ignore
    @Nonnull static final Map<Block, BubbleColumnPredicate> BUBBLE_COLUMN_PULL = new HashMap<>(), BUBBLE_COLUMN_PUSH = new HashMap<>();

    @Nullable
    public static BubbleColumnPredicate getBubbleColumnConditions(@Nonnull Block soil, boolean isPull) {
        return (isPull ? BUBBLE_COLUMN_PULL : BUBBLE_COLUMN_PUSH).get(soil);
    }

    public static void init() {

    }

    @SubscribeEvent
    static void syncConfig(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if(Subaquatic.MODID.equals(event.getModID())) {
            ConfigManager.sync(Subaquatic.MODID, Config.Type.INSTANCE);
            init();
        }
    }

    //TODO: this exists for testing purposes and will be removed once bubble columns are added
    static {
        BUBBLE_COLUMN_PULL.put(Blocks.MAGMA, new BubbleColumnPredicate(Blocks.MAGMA, new int[0], new HashSet<>()));
        BUBBLE_COLUMN_PUSH.put(Blocks.SOUL_SAND, new BubbleColumnPredicate(Blocks.SOUL_SAND, new int[0], new HashSet<>()));
    }
}
