package git.jbredwards.subaquatic;

import git.jbredwards.subaquatic.common.event.*;
import git.jbredwards.subaquatic.common.init.ModSmelting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

import static git.jbredwards.subaquatic.util.Constants.*;

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
        MinecraftForge.EVENT_BUS.register(new FuelHandler());
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        ModSmelting.init();
    }
}
