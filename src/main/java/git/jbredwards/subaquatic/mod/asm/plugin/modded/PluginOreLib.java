package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.fluidlogged_api.api.asm.impl.IChunkProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Allow OreLib's IBlockAccessEx to read FluidStates
 * @author jbred
 *
 */
public final class PluginOreLib implements IASMPlugin
{
    final boolean useWorld;
    public PluginOreLib(boolean useWorldIn) { useWorld = useWorldIn; }

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.interfaces.add("git/jbredwards/fluidlogged_api/api/asm/impl/IChunkProvider");
        /*
         * New code:
         * //allow OreLib's IBlockAccessEx to read FluidStates
         * @ASMGenerated
         * public Chunk getChunkFromBlockCoords(BlockPos pos)
         * {
         *     return Hooks.getChunk(this.cache, pos);
         * }
         */
        addMethod(classNode, "getChunkFromBlockCoords", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/chunk/Chunk;",
            "getChunk", "(Lgit/jbredwards/fluidlogged_api/api/asm/impl/IChunkProvider;)Lnet/minecraft/world/chunk/Chunk;", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitFieldInsn(GETFIELD, classNode.name, useWorld ? "world" : "cache", useWorld ? "Lnet/minecraft/world/World;" : "Lnet/minecraft/world/ChunkCache;");
                generator.visitVarInsn(ALOAD, 1);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nullable
        public static Chunk getChunk(@Nullable IChunkProvider cache, @Nonnull BlockPos pos) {
            return cache == null ? null : cache.getChunkFromBlockCoords(pos);
        }
    }
}
