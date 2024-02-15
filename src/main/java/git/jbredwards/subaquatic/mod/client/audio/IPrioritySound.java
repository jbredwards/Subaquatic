/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public interface IPrioritySound extends ISound
{
    boolean isPriority();
}
