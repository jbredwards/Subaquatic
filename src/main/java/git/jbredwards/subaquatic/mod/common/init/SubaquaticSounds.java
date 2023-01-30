package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
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
public final class SubaquaticSounds
{
    //sound init
    @Nonnull public static final List<SoundEvent> INIT = new ArrayList<>();

    //block sounds
    @Nonnull public static final SoundEvent BUBBLE_COLUMN_UP_AMBIENT = register("blocks.bubble_column.up.ambient");
    @Nonnull public static final SoundEvent BUBBLE_COLUMN_DOWN_AMBIENT = register("blocks.bubble_column.down.ambient");
    @Nonnull public static final SoundEvent BUBBLE_COLUMN_UP_INSIDE = register("blocks.bubble_column.up.inside");
    @Nonnull public static final SoundEvent BUBBLE_COLUMN_DOWN_INSIDE = register("blocks.bubble_column.down.inside");
    @Nonnull public static final SoundEvent PUMPKIN_CARVE = register("blocks.pumpkin.carve");

    //entity sounds
    @Nonnull public static final SoundEvent ENTITY_FISH_SWIM = register("entity.fish.swim");
    @Nonnull public static final SoundEvent ENTITY_SALMON_AMBIENT = register("entity.salmon.ambient");
    @Nonnull public static final SoundEvent ENTITY_SALMON_DEATH = register("entity.salmon.death");
    @Nonnull public static final SoundEvent ENTITY_SALMON_FLOP = register("entity.salmon.flop");
    @Nonnull public static final SoundEvent ENTITY_SALMON_HURT = register("entity.salmon.hurt");

    //misc sounds
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_ENTER = register("ambient.underwater.enter");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_EXIT = register("ambient.underwater.exit");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP = register("ambient.underwater.loop");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP_ADDITIONS = register("ambient.underwater.loop.additions");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE = register("ambient.underwater.loop.additions.rare");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRARARE = register("ambient.underwater.loop.additions.ultrarare");
    @Nonnull public static final SoundEvent BUCKET_FILL_FISH = register("items.bucket.fill.fish");
    @Nonnull public static final SoundEvent UNDERWATER_MUSIC = register("music.underwater");

    //sound types
    @Nonnull public static final SoundType CORAL = registerType("coral", 1, 1);
    @Nonnull public static final SoundType WET_GRASS = registerType("wet_grass", 1, 1);

    @Nonnull
    public static SoundEvent register(@Nonnull String name) {
        final SoundEvent sound = new SoundEvent(new ResourceLocation(Subaquatic.MODID, name));
        INIT.add(sound.setRegistryName(Subaquatic.MODID, name));
        return sound;
    }

    @Nonnull
    public static SoundType registerType(@Nonnull String name, float volume, float pitch) {
        return new SoundType(volume, pitch,
                register("blocks." + name + ".break"),
                register("blocks." + name + ".step"),
                register("blocks." + name + ".place"),
                register("blocks." + name + ".hit"),
                register("blocks." + name + ".fall"));
    }
}
