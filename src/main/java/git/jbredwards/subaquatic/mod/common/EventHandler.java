package git.jbredwards.subaquatic.mod.common;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public final class EventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void improveVanillaBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        Blocks.FLOWING_WATER.setLightOpacity(2);
        Blocks.WATER.setLightOpacity(2);
        Blocks.WATERLILY.setSoundType(SubaquaticSounds.WET_GRASS);
    }
}
