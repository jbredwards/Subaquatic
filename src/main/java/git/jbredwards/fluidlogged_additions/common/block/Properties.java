package git.jbredwards.fluidlogged_additions.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * used for setting block properties in a nice, compacted way
 * @author jbred
 *
 */
public final class Properties<B extends Block>
{
    @Nullable public String tool = null;
    public int level = 0;
    public float hardness = 0;
    public float resistance = 0;
    public float lightLevel = 0;
    public SoundType sound = SoundType.STONE;
    @Nullable public Consumer<B> func;

    public Properties() {}

    //gets the properties from a block
    public Properties<B> from(Block parent) {
        tool(parent.getHarvestTool(parent.getDefaultState()));
        level(parent.getHarvestLevel(parent.getDefaultState()));
        hardness(parent.getDefaultState().getBlockHardness(null, null));
        resistance(parent.getExplosionResistance(null) * 5 / 3f);
        lightLevel(parent.getDefaultState().getLightValue() / 15f);
        sound(parent.getSoundType());
        return this;
    }

    //sets the tool type
    public Properties<B> tool(@Nullable String tool) {
        this.tool = tool;
        return this;
    }

    //sets the level
    public Properties<B> level(int level) {
        this.level = level;
        return this;
    }

    //sets the hardness
    public Properties<B> hardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    //sets the resistance
    public Properties<B> resistance(float resistance) {
        this.resistance = resistance;
        return this;
    }

    //sets the light level
    public Properties<B> lightLevel(float lightLevel) {
        this.lightLevel = lightLevel;
        return this;
    }

    //sets the sound
    public Properties<B> sound(SoundType sound) {
        this.sound = sound;
        return this;
    }

    //call void at the same line as the constructor
    public Properties<B> func(Consumer<B> func) {
        this.func = func;
        return this;
    }
}
