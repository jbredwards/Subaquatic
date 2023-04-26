package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockWallTypeless extends BlockWall implements ICustomModel
{
    public BlockWallTypeless(@Nonnull Block modelBlock) { super(modelBlock); }

    @Override
    public void getSubBlocks(@Nonnull CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(VARIANT).build());
    }
}
