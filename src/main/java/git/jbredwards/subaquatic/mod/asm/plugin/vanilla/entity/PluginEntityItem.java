/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.item.IFloatingBehavior;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Items float while in water
 * @author jbred
 *
 */
public final class PluginEntityItem implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_70071_h_" : "onUpdate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onUpdate: (changes are around line 118)
         * Old code:
         * if (!this.hasNoGravity())
         * {
         *     ...
         * }
         *
         * New code:
         * //items float in water
         * if(!Hooks.doesFloat(this))
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_189652_ae" : "hasNoGravity")) {
            instructions.insert(insn, genMethodNode("doesFloat", "(Lnet/minecraft/entity/Entity;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean doesFloat(@Nonnull Entity entity) {
            //items float
            if(entity instanceof EntityItem) {
                final Item item = ((EntityItem)entity).getItem().getItem();
                final boolean configEnabled = SubaquaticConfigHandler.Common.Entity.itemsFloat;

                //IFloatingBehavior implementation
                if(item instanceof IFloatingBehavior) {
                    if(((IFloatingBehavior)item).canEntityFloat((EntityItem)entity, configEnabled)) {
                        ((IFloatingBehavior)item).doEntityFloat((EntityItem)entity);
                        return true;
                    }
                }

                //default implementation
                else if(configEnabled && entity.isInsideOfMaterial(Material.WATER)) {
                    if(entity.motionY < 0.06) entity.motionY += 5.0E-4;

                    entity.motionX *= 0.99;
                    entity.motionZ *= 0.99;

                    return true;
                }
            }

            //xp orbs float
            else if(SubaquaticConfigHandler.Common.Entity.xpOrbsFloat && entity.isInsideOfMaterial(Material.WATER)) {
                if(entity.motionY < 0.06) entity.motionY += 5.0E-4;

                entity.motionX *= 0.99;
                entity.motionZ *= 0.99;

                return true;
            }

            //fallback on no gravity check
            return entity.hasNoGravity();
        }
    }
}
