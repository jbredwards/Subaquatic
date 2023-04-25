package git.jbredwards.subaquatic.mod.common;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import git.jbredwards.subaquatic.mod.common.message.SMessageBoatType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
        Blocks.BROWN_MUSHROOM.setTranslationKey(Subaquatic.MODID + ".brown_mushroom");
        Blocks.BROWN_MUSHROOM_BLOCK.setTranslationKey(Blocks.BROWN_MUSHROOM.translationKey);
        Blocks.PUMPKIN.setTranslationKey(Subaquatic.MODID + ".carved_pumpkin");
        Blocks.RED_MUSHROOM.setTranslationKey(Subaquatic.MODID + ".red_mushroom");
        Blocks.RED_MUSHROOM_BLOCK.setTranslationKey(Blocks.RED_MUSHROOM.translationKey);

        Blocks.FLOWING_WATER.setLightOpacity(2);
        Blocks.WATER.setLightOpacity(2);
        Blocks.WATERLILY.setSoundType(SubaquaticSounds.WET_GRASS);
    }

    @SubscribeEvent
    static void syncBoatContainers(@Nonnull PlayerEvent.StartTracking event) {
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            final IBoatType cap = IBoatType.get(event.getTarget());
            if(cap != null) Subaquatic.WRAPPER.sendTo(
                    new SMessageBoatType(cap.getType(), event.getTarget()),
                    (EntityPlayerMP)event.getEntityPlayer()
            );
        }
    }
}
