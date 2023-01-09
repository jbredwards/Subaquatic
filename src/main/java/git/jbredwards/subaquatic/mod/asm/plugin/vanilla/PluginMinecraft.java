package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.EnumHelper;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Allow underwater music to be played
 * @author jbred
 *
 */
public final class PluginMinecraft implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_147109_W" : "getAmbientMusicType"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * getAmbientMusicType: (changes are around line 3242)
         * Old code:
         * MusicTicker.MusicType type = this.world.provider.getMusicType();
         *
         * New code:
         * //allow underwater music to be played
         * MusicTicker.MusicType type = Hooks.getMusicType(this.world.provider);
         */
        if(checkMethod(insn, "getMusicType")) {
            instructions.insert(insn, genMethodNode("getMusicType", "(Lnet/minecraft/world/WorldProvider;)Lnet/minecraft/client/audio/MusicTicker$MusicType;"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        //helper
        public static final MusicTicker.MusicType UNDERWATER = EnumHelper.addEnum(MusicTicker.MusicType.class,
                "UNDERWATER", new Class[] {SoundEvent.class, int.class, int.class}, SubaquaticSounds.UNDERWATER_MUSIC, 12000, 24000);

        @Nullable
        public static MusicTicker.MusicType getMusicType(@Nonnull WorldProvider provider) {
            final MusicTicker.MusicType providerType = provider.getMusicType();
            if(providerType != null) return providerType;

            final EntityPlayerSP player = Minecraft.getMinecraft().player;
            if(!isPlayingGameMusic() && player.isInWater() && player.isInsideOfMaterial(Material.WATER)) {
                final Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(player.world.getBiome(new BlockPos(player.posX, player.posY, player.posZ)));
                if(biomeTypes.contains(BiomeDictionary.Type.RIVER) || biomeTypes.contains(BiomeDictionary.Type.OCEAN) || biomeTypes.contains(BiomeDictionary.Type.BEACH))
                    return UNDERWATER;
            }

            return null;
        }

        //helper
        static boolean isPlayingGameMusic() {
            return Minecraft.getMinecraft().getMusicTicker().currentMusic != null
                    && MusicTicker.MusicType.GAME.getMusicLocation().getSoundName().equals(Minecraft.getMinecraft().getMusicTicker().currentMusic.getSoundLocation());
        }
    }
}
