package git.jbredwards.subaquatic.mod;

import com.cleanroommc.assetmover.AssetMoverAPI;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @author jbred
 *
 */
@Mod(modid = Subaquatic.MODID, name = Subaquatic.NAME, version = "1.0.0", dependencies = "required-after:fluidlogged_api@[1.9.0.5,);")
public final class Subaquatic
{
    @Nonnull public static final String MODID = "subaquatic", NAME = "Subaquatic";
    @Nonnull public static final Logger LOGGER = LogManager.getFormatterLogger(NAME);

    @SuppressWarnings("ConstantConditions")
    @Mod.EventHandler
    static void construct(@Nonnull FMLConstructionEvent event) throws IOException {
        if(event.getSide() == Side.CLIENT) {
            LOGGER.info("Attempting to gather the vanilla assets required by this mod, this may take a while if it's your first load...");
            final String[][] assets = new Gson().fromJson(IOUtils.toString(
                    Loader.class.getResourceAsStream(String.format("/assets/%s/assetmover.jsonc", MODID)),
                    Charset.defaultCharset()), String[][].class);

            final ProgressManager.ProgressBar progressBar = ProgressManager.push("AssetMover", assets.length);
            for(String[] asset : assets) { //display progress, otherwise it looks like the game froze
                progressBar.step(asset[2].replaceFirst(String.format("assets/%s/", MODID), ""));
                AssetMoverAPI.fromMinecraft(asset[0], ImmutableMap.of(asset[1], asset[2]));
            }

            ProgressManager.pop(progressBar);
            LOGGER.info("Success!");
        }
    }
}
