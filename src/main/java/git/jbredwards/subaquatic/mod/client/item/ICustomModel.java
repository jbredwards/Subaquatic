package git.jbredwards.subaquatic.mod.client.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author jbred
 *
 */
public interface ICustomModel
{
    @SideOnly(Side.CLIENT)
    void registerModels();
}
