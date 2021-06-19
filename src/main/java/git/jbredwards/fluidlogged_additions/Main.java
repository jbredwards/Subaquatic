package git.jbredwards.fluidlogged_additions;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import git.jbredwards.fluidlogged_additions.common.event.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

import static git.jbredwards.fluidlogged_additions.util.Constants.*;

/**
 *
 * @author jbred
 *
 */
@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public final class Main
{
    public static final Logger logger = LogManager.getFormatterLogger(MODID);
    public static void print(Object toPrint) { logger.info(toPrint); }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public static void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(new RegistryHandler());
    }
}
