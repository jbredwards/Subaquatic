/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Improve Aqua Acrobatics mod compatibility by removing the stuff from that mod which this mod also does
 * @author jbred
 *
 */
public final class PluginAquaAcrobatics implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        if("com/fuzs/aquaacrobatics/block/BlockBubbleColumn".equals(classNode.name))
            classNode.interfaces.add("git/jbredwards/subaquatic/api/block/IOxygenSupplier");
        else classNode.methods.removeIf(method ->
                //remove AA EntityItemMixin
                method.name.equals("applyFloatMotionIfInWater") ||
                //remove AA BlockGrassMixin & BlockMyceliumMixin
                method.name.equals("updateUnderwaterToDirt") ||
                method.name.equals("avoidSettingGrass") ||
                //remove AA BiomeMixin
                method.name.equals("forceNewColor") ||
                //remove AA BiomeColorHelperMixin
                method.name.equals("getNewWaterColorMultiplier") ||
                //remove AA ItemRendererMixin
                method.name.equals("replaceOpacity") ||
                //remove AA ModelFluidMixin
                method.name.equals("getTextures_RealStill") ||
                method.name.equals("getTextures_RealFlowing") ||
                method.name.equals("bake_RealStill") ||
                method.name.equals("bake_RealFlowing") ||
                //remove AA FogHandler
                method.name.equals("onRenderFogColor") ||
                //remove AA EntityMixin climbing behavior (somehow causes crash with subaquatic installed, mixin is wack)
                method.name.equals("getFakeClimbingBlock") ||
                //remove AA EntityLivingBaseMixin gradual air replenishing (handled by subaquatic)
                method.name.equals("checkBubbleBreathing") ||
                method.name.equals("getNewAirValue") ||
                //remove AA TileCrucibleRendererMixin
                method.name.equals("getLegacyWaterTexture")
        );

        return false;
    }
}
