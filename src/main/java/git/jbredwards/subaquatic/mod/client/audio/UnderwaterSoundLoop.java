package git.jbredwards.subaquatic.mod.client.audio;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class UnderwaterSoundLoop extends MovingSound implements IUnderwaterSound
{
    @Nonnull
    protected final EntityPlayerSP player;
    protected int ticksInWater = 0;

    public UnderwaterSoundLoop(@Nonnull EntityPlayerSP playerIn) {
        super(SubaquaticSounds.AMBIENT_UNDERWATER_LOOP, SoundCategory.AMBIENT);
        player = playerIn;

        repeat = true;
        repeatDelay = 0;
    }

    @Override
    public void update() {
        if(!player.isDead && ticksInWater >= 0) {
            xPosF = (float)player.posX;
            yPosF = (float)player.posY;
            zPosF = (float)player.posZ;

            if(player.isInWater() && player.isInsideOfMaterial(Material.WATER)) ticksInWater++;
            else ticksInWater -= 2;

            ticksInWater = Math.min(ticksInWater, 40);
            volume = Math.max(0, Math.min(ticksInWater / 40f, 1));
        }

        else donePlaying = true;
    }
}
