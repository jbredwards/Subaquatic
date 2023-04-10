package git.jbredwards.subaquatic.mod.common.config.util;

import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Consumer;

/**
 *
 * @author jbred
 *
 */
public final class ConfigUtils
{
    //parses the config file in each mod, if the mod has one
    public static void parseFromMods(@Nonnull String fileName, @Nonnull Consumer<Reader> fileParser) {
        for(String modid : Loader.instance().getIndexedModList().keySet()) {
            final @Nullable InputStream stream = Loader.class.getResourceAsStream(String.format("/assets/%s/%s", modid, fileName));
            if(stream != null) fileParser.accept(new InputStreamReader(stream));
        }
    }
}
