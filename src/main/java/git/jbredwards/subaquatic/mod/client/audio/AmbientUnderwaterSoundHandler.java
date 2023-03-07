package git.jbredwards.subaquatic.mod.client.audio;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID, value = Side.CLIENT)
public final class AmbientUnderwaterSoundHandler
{
    @Nullable
    static UnderwaterSoundLoop currentLoopSound = null;
    static boolean prevInsideWater;

    @SubscribeEvent
    static void tick(@Nonnull TickEvent.ClientTickEvent event) {
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player != null) {
            if(event.phase == TickEvent.Phase.START) {
                final boolean insideWater = player.isInsideOfMaterial(Material.WATER);

                //tick ambient sounds
                final SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
                if(player.isInWater() && insideWater) {
                    final float rng = player.getRNG().nextFloat();

                    if(rng < 0.0001) soundHandler.playSound(new UnderwaterSound(player, SubaquaticSounds.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRARARE, SoundCategory.AMBIENT));
                    else if(rng < 0.001) soundHandler.playSound(new UnderwaterSound(player, SubaquaticSounds.AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE, SoundCategory.AMBIENT));
                    else if(rng < 0.01) soundHandler.playSound(new UnderwaterSound(player, SubaquaticSounds.AMBIENT_UNDERWATER_LOOP_ADDITIONS, SoundCategory.AMBIENT));
                }

                //ambient water enter sound
                if(!prevInsideWater && insideWater) {
                    if(!player.isSpectator()) player.world.playSound(player.posX, player.posY, player.posZ, SubaquaticSounds.AMBIENT_UNDERWATER_ENTER, SoundCategory.AMBIENT, 1, 1, false);
                }

                //ambient water sound
                else if(insideWater) {
                    //this ensures the sound restarts in the event that it stops while still underwater (due to lag)
                    if(currentLoopSound == null || !soundHandler.isSoundPlaying(currentLoopSound))
                        soundHandler.playSound(currentLoopSound = new UnderwaterSoundLoop(player));
                }

                //ambient water exit sound
                else if(prevInsideWater && !player.isSpectator())
                    player.world.playSound(player.posX, player.posY, player.posZ, SubaquaticSounds.AMBIENT_UNDERWATER_EXIT, SoundCategory.AMBIENT, 1, 1, false);

                //update prevInsideWater
                prevInsideWater = insideWater;
            }
        }

        //player does not exist
        else {
            if(currentLoopSound != null) currentLoopSound = null;
            prevInsideWater = false;
        }
    }
}
