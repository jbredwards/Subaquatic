/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.audio;

import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class UnderwaterSound extends MovingSound implements IUnderwaterSound
{
    @Nonnull
    protected final EntityPlayerSP player;
    public UnderwaterSound(@Nonnull EntityPlayerSP playerIn, @Nonnull SoundEvent soundIn, @Nonnull SoundCategory categoryIn) {
        super(soundIn, categoryIn);
        player = playerIn;
    }

    @Override
    public void update() {
        if(!player.isDead && player.isInWater() && player.isInsideOfMaterial(Material.WATER)) {
            xPosF = (float)player.posX;
            yPosF = (float)player.posY;
            zPosF = (float)player.posZ;
        }

        else donePlaying = true;
    }
}
