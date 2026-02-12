/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * stores all of this mod's sounds
 * @author jbred
 *
 */
public final class SubaquaticSounds
{
    //sound init
    @Nonnull public static final List<SoundEvent> INIT = new LinkedList<>();

    //block sounds
    @Nonnull public static final SoundEvent BLOCK_BEACON_ACTIVATE = register("blocks.beacon.activate");
    @Nonnull public static final SoundEvent BLOCK_BEACON_AMBIENT = register("blocks.beacon.ambient");
    @Nonnull public static final SoundEvent BLOCK_BEACON_DEACTIVATE = register("blocks.beacon.deactivate");
    @Nonnull public static final SoundEvent BLOCK_BEACON_POWER_SELECT = register("blocks.beacon.power_select");
    @Nonnull public static final SoundEvent BLOCK_BUBBLE_COLUMN_BUBBLE_POP = register("blocks.bubble_column.bubble_pop");
    @Nonnull public static final SoundEvent BLOCK_BUBBLE_COLUMN_DOWN_AMBIENT = register("blocks.bubble_column.down.ambient");
    @Nonnull public static final SoundEvent BLOCK_BUBBLE_COLUMN_DOWN_INSIDE = register("blocks.bubble_column.down.inside");
    @Nonnull public static final SoundEvent BLOCK_BUBBLE_COLUMN_UP_AMBIENT = register("blocks.bubble_column.up.ambient");
    @Nonnull public static final SoundEvent BLOCK_BUBBLE_COLUMN_UP_INSIDE = register("blocks.bubble_column.up.inside");
    @Nonnull public static final SoundEvent BLOCK_CHEST_OPEN_UNDERWATER = register("blocks.chest.open_underwater");
    @Nonnull public static final SoundEvent BLOCK_FROGSPAWN_HATCH = register("blocks.frogspawn.hatch");
    @Nonnull public static final SoundEvent BLOCK_PUMPKIN_CARVE = register("blocks.pumpkin.carve");

    //entity sounds
    @Nonnull public static final SoundEvent ENTITY_COD_DEATH = register("entity.cod.death");
    @Nonnull public static final SoundEvent ENTITY_COD_FLOP = register("entity.cod.flop");
    @Nonnull public static final SoundEvent ENTITY_COD_HURT = register("entity.cod.hurt");
    @Nonnull public static final SoundEvent ENTITY_FISH_DEATH = register("entity.fish.death");
    @Nonnull public static final SoundEvent ENTITY_FISH_FLOP = register("entity.fish.flop");
    @Nonnull public static final SoundEvent ENTITY_FISH_HURT = register("entity.fish.hurt");
    @Nonnull public static final SoundEvent ENTITY_FISH_SWIM = register("entity.fish.swim");
    @Nonnull public static final SoundEvent ENTITY_PUFFERFISH_DEATH = register("entity.pufferfish.death");
    @Nonnull public static final SoundEvent ENTITY_PUFFERFISH_DEFLATE = register("entity.pufferfish.deflate");
    @Nonnull public static final SoundEvent ENTITY_PUFFERFISH_FLOP = register("entity.pufferfish.flop");
    @Nonnull public static final SoundEvent ENTITY_PUFFERFISH_HURT = register("entity.pufferfish.hurt");
    @Nonnull public static final SoundEvent ENTITY_PUFFERFISH_INFLATE = register("entity.pufferfish.inflate");
    @Nonnull public static final SoundEvent ENTITY_PUFFERFISH_STING = register("entity.pufferfish.sting");
    @Nonnull public static final SoundEvent ENTITY_SALMON_DEATH = register("entity.salmon.death");
    @Nonnull public static final SoundEvent ENTITY_SALMON_FLOP = register("entity.salmon.flop");
    @Nonnull public static final SoundEvent ENTITY_SALMON_HURT = register("entity.salmon.hurt");
    @Nonnull public static final SoundEvent ENTITY_TROPICAL_FISH_DEATH = register("entity.tropical_fish.death");
    @Nonnull public static final SoundEvent ENTITY_TROPICAL_FISH_FLOP = register("entity.tropical_fish.flop");
    @Nonnull public static final SoundEvent ENTITY_TROPICAL_FISH_HURT = register("entity.tropical_fish.hurt");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_BABY_DEATH = register("entity.turtle.baby.death");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_BABY_HURT = register("entity.turtle.baby.hurt");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_BABY_STEP = register("entity.turtle.baby.step");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_AMBIENT = register("entity.turtle.ambient");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_DEATH = register("entity.turtle.death");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_EGG_BREAK = register("entity.turtle.egg_break");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_EGG_CRACK = register("entity.turtle.egg_crack");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_EGG_HATCH = register("entity.turtle.egg_hatch");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_HURT = register("entity.turtle.hurt");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_LAY_EGG = register("entity.turtle.lay_egg");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_STEP = register("entity.turtle.step");
    @Nonnull public static final SoundEvent ENTITY_TURTLE_SWIM = register("entity.turtle.swim");
    @Nonnull public static final SoundEvent ENTITY_ZOMBIE_DESTROY_EGG = register("entity.zombie.destroy_egg");

    //misc sounds
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_ENTER = register("ambient.underwater.enter");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_EXIT = register("ambient.underwater.exit");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP = register("ambient.underwater.loop");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP_ADDITIONS = register("ambient.underwater.loop.additions");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE = register("ambient.underwater.loop.additions.rare");
    @Nonnull public static final SoundEvent AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRARARE = register("ambient.underwater.loop.additions.ultrarare");
    @Nonnull public static final SoundEvent BUCKET_FILL_FISH = register("items.bucket.fill.fish");
    @Nonnull public static final SoundEvent GUI_BUBBLE_POP = register("gui.bubble_pop");
    @Nonnull public static final SoundEvent UNDERWATER_MUSIC = register("music.underwater");

    //sound types
    @Nonnull public static final SoundType CORAL = registerType("coral", 1, 1);
    @Nonnull public static final SoundType FROGLIGHT = registerType("froglight", 1, 1);
    @Nonnull public static final SoundType FROGSPAWN = registerType("frogspawn", 1, 1);
    @Nonnull public static final SoundType HANGING_ROOTS = registerType("hanging_roots", 1, 1);
    @Nonnull public static final SoundType MANGROVE_ROOTS = registerType("mangrove_roots", 1, 1);
    @Nonnull public static final SoundType MANGROVE_ROOTS_MUD = registerType("mangrove_roots_mud", 1, 1);
    @Nonnull public static final SoundType MUD = registerType("mud", 1, 1);
    @Nonnull public static final SoundType PACKED_MUD = registerType("packed_mud", 1, 1);
    @Nonnull public static final SoundType PACKED_MUD_BRICKS = registerType("packed_mud_bricks", 1, 1);
    @Nonnull public static final SoundType ROOTED_DIRT = registerType("rooted_dirt", 1, 1);
    @Nonnull public static final SoundType TURTLE_EGG = registerType("turtle_egg", 1, 1);
    @Nonnull public static final SoundType WET_GRASS = registerType("wet_grass", 1, 1);

    //parrot imitate sounds
    static void handleParrotImitateSounds() {
    }

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
