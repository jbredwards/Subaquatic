package git.jbredwards.fluidlogged_additions.common.init;

import git.jbredwards.fluidlogged_additions.util.Constants;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public enum ModSounds
{
    ;

    //sound init
    public static final List<SoundEvent> INIT = new ArrayList<>();

    //sound types
    public static final SoundType CORAL = registerType(1, 1, "coral");

    public static SoundEvent register(String name) {
        final SoundEvent sound = new SoundEvent(new ResourceLocation(Constants.MODID, name)).setRegistryName(name);
        INIT.add(sound);

        return sound;
    }

    public static SoundType registerType(float volume, float pitch, String name) {
        final SoundEvent soundBreak = register("blocks." + name + ".break");
        final SoundEvent soundStep = register("blocks." + name + ".step");

        return new SoundType(volume, pitch, soundBreak, soundStep, soundBreak, soundStep, soundStep);
    }
}
