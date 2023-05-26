package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.block.IOxygenSupplier;
import git.jbredwards.subaquatic.api.entity.INextAirEntity;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Backport new oxygen replenish system & add IOxygenSupplier
 * @author jbred
 *
 */
public final class PluginEntityLivingBase implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_70030_z" : "onEntityUpdate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onEntityUpdate: (changes are around line 314)
         * Old code:
         * if (!this.isInsideOfMaterial(Material.WATER))
         * {
         *     ...
         * }
         *
         * New code:
         * //backport new oxygen replenish system & add IOxygenSupplier
         * if (!Hooks.hasNoOxygen(this))
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_70055_a" : "isInsideOfMaterial")) {
            instructions.insert(insn, genMethodNode("hasNoOxygen", "(Lnet/minecraft/entity/EntityLivingBase;)Z"));
            removeFrom(instructions, insn, -1);
        }
        /*
         * onEntityUpdate: (changes are around line 316)
         * Old code:
         * setAir(300);
         *
         * New code:
         * //remove old code (this was handled by Hooks.hasNoOxygen)
         * ...
         */
        else if(checkMethod(insn, obfuscated ? "func_70050_g" : "setAir")) {
            removeFrom(instructions, insn, -2);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean hasNoOxygen(@Nonnull EntityLivingBase entity) {
            final BlockPos eyePos = new BlockPos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
            final IBlockState eyeState = entity.world.getBlockState(eyePos);

            if(eyeState.getBlock() instanceof IOxygenSupplier) {
                final IOxygenSupplier oxygenSupplier = (IOxygenSupplier)eyeState.getBlock();
                if(oxygenSupplier.canSupplyOxygenTo(entity.world, eyePos, eyeState, entity)) {
                    entity.setAir(increaseAirSupply(entity, oxygenSupplier.getOxygenToSupply(entity.world, eyePos, eyeState, entity)));
                    return false;
                }
            }

            else if(!entity.isInsideOfMaterial(Material.WATER)) {
                entity.setAir(increaseAirSupply(entity, 4));
                return false;
            }

            return true;
        }

        //helper
        public static int increaseAirSupply(@Nonnull EntityLivingBase entity, int airToGain) {
            return entity instanceof INextAirEntity
                    ? ((INextAirEntity)entity).increaseAirSupply(entity.getAir(), airToGain, !SubaquaticConfigHandler.Common.Entity.gradualAirReplenish)
                    : SubaquaticConfigHandler.Common.Entity.gradualAirReplenish ? Math.min(entity.getAir() + airToGain, 300) : 300;
        }
    }
}
