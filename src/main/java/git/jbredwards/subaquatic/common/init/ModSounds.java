package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.Subaquatic;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * stores all of this mod's sounds
 * @author jbred
 *
 */
public final class ModSounds
{
    //sound init
    @Nonnull public static final List<SoundEvent> INIT = new ArrayList<>();

    //sound types
    @Nonnull public static final SoundType CORAL = registerType("coral", 1, 1);

    @Nonnull
    public static SoundEvent register(@Nonnull String name) {
        final SoundEvent sound = new SoundEvent(new ResourceLocation(Subaquatic.MODID, name));
        INIT.add(sound.setRegistryName(Subaquatic.MODID, name));
        return sound;
    }

    @Nonnull
    public static SoundType registerType(@Nonnull String name, float volume, float pitch) {
        final SoundEvent soundBreak = register("blocks." + name + ".break");
        final SoundEvent soundStep = register("blocks." + name + ".step");
        return new SoundType(volume, pitch, soundBreak, soundStep, soundBreak, soundStep, soundStep);
    }
}
