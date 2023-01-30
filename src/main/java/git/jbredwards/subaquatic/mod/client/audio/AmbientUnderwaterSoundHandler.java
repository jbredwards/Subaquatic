package git.jbredwards.subaquatic.mod.client.audio;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID, value = Side.CLIENT)
public final class AmbientUnderwaterSoundHandler
{
    static boolean prevInsideWater;

    @SubscribeEvent
    static void tick(@Nonnull TickEvent.ClientTickEvent event) {
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player != null) {
            final boolean insideWater = player.isInsideOfMaterial(Material.WATER);
            if(event.phase == TickEvent.Phase.START) prevInsideWater = insideWater;
            else {
                //tick ambient sounds
                if(player.isInWater() && insideWater) {
                    final float rng = player.getRNG().nextFloat();

                    if(rng < 0.0001) Minecraft.getMinecraft().getSoundHandler().playSound(new UnderwaterSound(player, SubaquaticSounds.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRARARE, SoundCategory.AMBIENT));
                    else if(rng < 0.001) Minecraft.getMinecraft().getSoundHandler().playSound(new UnderwaterSound(player, SubaquaticSounds.AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE, SoundCategory.AMBIENT));
                    else if(rng < 0.01) Minecraft.getMinecraft().getSoundHandler().playSound(new UnderwaterSound(player, SubaquaticSounds.AMBIENT_UNDERWATER_LOOP_ADDITIONS, SoundCategory.AMBIENT));
                }

                //ambient water enter sound
                if(!prevInsideWater && insideWater) {
                    if(!player.isSpectator()) player.world.playSound(player.posX, player.posY, player.posZ, SubaquaticSounds.AMBIENT_UNDERWATER_ENTER, SoundCategory.AMBIENT, 1, 1, false);
                    Minecraft.getMinecraft().getSoundHandler().playSound(new UnderwaterSoundLoop(player));
                }

                //ambient water exit sound
                else if(prevInsideWater && !insideWater && !player.isSpectator())
                    player.world.playSound(player.posX, player.posY, player.posZ, SubaquaticSounds.AMBIENT_UNDERWATER_EXIT, SoundCategory.AMBIENT, 1, 1, false);
            }
        }
    }

    @SubscribeEvent
    static void resetPrevInsideWater(@Nonnull FMLNetworkEvent.ClientConnectedToServerEvent event) { prevInsideWater = false; }
}
