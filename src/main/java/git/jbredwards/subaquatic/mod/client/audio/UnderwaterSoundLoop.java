/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.audio;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class UnderwaterSoundLoop extends UnderwaterSound implements IPrioritySound
{
    protected int ticksInWater = 0;
    public UnderwaterSoundLoop(@Nonnull EntityPlayerSP playerIn) {
        super(playerIn, SubaquaticSounds.AMBIENT_UNDERWATER_LOOP, SoundCategory.AMBIENT);
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
            volume = MathHelper.clamp(ticksInWater / 40f, 0, 1);
        }

        else {
            donePlaying = true;
            AmbientUnderwaterSoundHandler.currentLoopSound = null;
        }
    }

    @Override
    public boolean isPriority() { return true; }
}
