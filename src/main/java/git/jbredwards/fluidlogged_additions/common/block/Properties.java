package git.jbredwards.fluidlogged_additions.common.block;

import net.minecraft.block.Block;

import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public final class Properties
{
    @Nullable public String tool = null;
    public int level = 0;
    public float hardness = 0;
    public float resistance = 0;
    public float lightLevel = 0;

    public Properties() {}

    //gets the properties from a block
    public Properties copy(Block parent) {
        this.tool = parent.getHarvestTool(parent.getDefaultState());
        this.level = parent.getHarvestLevel(parent.getDefaultState());
        this.hardness = parent.getDefaultState().getBlockHardness(null, null);
        this.resistance = parent.getExplosionResistance(null) * 5 / 3;
        this.lightLevel = parent.getDefaultState().getLightValue() / 15f;
        return this;
    }

    //sets the tool type
    public Properties tool(@Nullable String tool) {
        this.tool = tool;
        return this;
    }

    //sets the level
    public Properties level(int level) {
        this.level = level;
        return this;
    }

    //sets the level
    public Properties hardness(int hardness) {
        this.hardness = hardness;
        return this;
    }

    //sets the level
    public Properties resistance(int resistance) {
        this.resistance = resistance;
        return this;
    }

    //sets the level
    public Properties lightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
        return this;
    }
}
