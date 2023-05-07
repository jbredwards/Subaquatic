package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Add bubble particles for chests when they open underwater
 * @author jbred
 *
 */
public final class PluginTileEntityChest implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_73660_a" : "update"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * update: (changes are around line 314)
         * Old code:
         * this.world.playSound((EntityPlayer)null, d1, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
         *
         * New code:
         * //add bubble particles for chests when they open underwater
         * this.world.playSound((EntityPlayer)null, d1, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
         * Hooks.addBubbles(this);
         */
        if(checkMethod(insn, obfuscated ? "func_184148_a" : "playSound")) {
            instructions.insert(insn, genMethodNode("addBubbles", "(Lnet/minecraft/tileentity/TileEntityChest;)V"));
            instructions.insert(insn, new VarInsnNode(ALOAD, 0));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void addBubbles(@Nonnull TileEntityChest tile) {
            if(tile.hasWorld() && tile.getWorld().isRemote) {
                final double x = tile.getPos().getX();
                final double y = tile.getPos().getY();
                final double z = tile.getPos().getZ();
                tile.getWorld().playSound(x + 0.5, y + 0.5, z + 0.5, SubaquaticSounds.CHEST_OPEN_UNDERWATER, SoundCategory.BLOCKS, 0.5f, 1, false);

                final Random rand = tile.getWorld().rand;
                int remainingParticles = MathHelper.getInt(rand, SubaquaticConfigHandler.Client.Particle.underwaterChestMinBubbleCount, SubaquaticConfigHandler.Client.Particle.underwaterChestMaxBubbleCount);
                while(remainingParticles --> 0) tile.getWorld().spawnParticle(EnumParticleTypes.WATER_BUBBLE,
                        x + rand.nextDouble() * 0.6 + 0.2, y + rand.nextDouble() * 0.5 + 0.4, z + rand.nextDouble() * 0.6 + 0.2,
                        rand.nextDouble() * 0.2 - 0.1, rand.nextDouble(), rand.nextDouble() * 0.2 - 0.1);
            }
        }
    }
}
