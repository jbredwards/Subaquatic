package git.jbredwards.fluidlogged_additions;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;

import static git.jbredwards.fluidlogged_additions.util.Constants.*;

/**
 *
 * @author jbred
 *
 */
@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public final class Main extends DummyModContainer
{
    //plugin
    public Main() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = MODID + "_plugin";
        meta.name = NAME + " Plugin";
        meta.version = VERSION;
        meta.credits = "jbredwards";
        meta.authorList = ImmutableList.of("jbredwards");
        meta.url = "https://curseforge.com/minecraft/mc-mods/fluidlogged-additions";
    }


}
