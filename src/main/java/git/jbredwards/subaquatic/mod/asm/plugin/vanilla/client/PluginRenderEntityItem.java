/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.item.IFloatingBehavior;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Don't render item bobbing while in water
 * @author jbred
 *
 */
public final class PluginRenderEntityItem implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_177077_a" : "transformModelCount"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         *transformModelCount: (changes are around line 47)
         * Old code:
         * float f1 = shouldBob() ? MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
         *
         * New code:
         * //don't render item bobbing while in water
         * float f1 = Hooks.shouldBob(this, itemIn) ? MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
         */
        if(checkMethod(insn, "shouldBob")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 1));
            instructions.insertBefore(insn, genMethodNode("shouldBob", "(Lnet/minecraft/client/renderer/entity/RenderEntityItem;Lnet/minecraft/entity/item/EntityItem;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @SideOnly(Side.CLIENT)
        public static boolean shouldBob(@Nonnull RenderEntityItem renderer, @Nonnull EntityItem entity) {
            if(!renderer.shouldBob()) return false;
            else if(entity.world == null) return true;

            final Item item = entity.getItem().getItem();
            final boolean configEnabled = SubaquaticConfigHandler.Common.Entity.itemsFloat;

            return item instanceof IFloatingBehavior
                    ? !((IFloatingBehavior)item).canEntityFloat(entity, configEnabled)
                    : !configEnabled || !entity.isInsideOfMaterial(Material.WATER);
        }
    }
}
