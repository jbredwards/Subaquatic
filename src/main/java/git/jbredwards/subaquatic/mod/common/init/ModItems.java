package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.block.AbstractBlockCoral;
import git.jbredwards.subaquatic.mod.common.item.CreativeTab;
import git.jbredwards.subaquatic.mod.common.item.ItemBlockMeta;
import git.jbredwards.subaquatic.mod.common.item.ItemDurationFood;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * stores all of this mod's items
 * @author jbred
 *
 */
public final class ModItems
{
    //item init
    @Nonnull public static final List<Item> INIT = new ArrayList<>();

    //item blocks
    @Nonnull public static final ItemBlock DRIED_KELP_BLOCK = register("dried_kelp_block", new ItemBlock(ModBlocks.DRIED_KELP_BLOCK));
    @Nonnull public static final ItemBlock KELP = register("kelp", new ItemBlock(ModBlocks.KELP));
    @Nonnull public static final ItemBlock NAUTILUS_SHELL = register("nautilus_shell", new ItemBlock(ModBlocks.NAUTILUS_SHELL));
    @Nonnull public static final ItemBlock TUBE_CORAL_BLOCK = register("tube_coral_block", new ItemBlockMeta(ModBlocks.TUBE_CORAL_BLOCK, AbstractBlockCoral.ALIVE));
    @Nonnull public static final ItemBlock BRAIN_CORAL_BLOCK = register("brain_coral_block", new ItemBlockMeta(ModBlocks.BRAIN_CORAL_BLOCK, AbstractBlockCoral.ALIVE));
    @Nonnull public static final ItemBlock BUBBLE_CORAL_BLOCK = register("bubble_coral_block", new ItemBlockMeta(ModBlocks.BUBBLE_CORAL_BLOCK, AbstractBlockCoral.ALIVE));
    @Nonnull public static final ItemBlock FIRE_CORAL_BLOCK = register("fire_coral_block", new ItemBlockMeta(ModBlocks.FIRE_CORAL_BLOCK, AbstractBlockCoral.ALIVE));
    @Nonnull public static final ItemBlock HORN_CORAL_BLOCK = register("horn_coral_block", new ItemBlockMeta(ModBlocks.HORN_CORAL_BLOCK, AbstractBlockCoral.ALIVE));

    //items
    public static final ItemFood DRIED_KELP = register("dried_kelp", new ItemDurationFood(1, false), item -> item.maxUseDuration = 16);

    @Nonnull
    static <I extends Item> I register(@Nonnull String name, @Nonnull I item) {
        return register(name, item, itemIn -> {});
    }

    @Nonnull
    static <I extends Item> I register(@Nonnull String name, @Nonnull I item, @Nonnull Consumer<I> consumer) {
        INIT.add(item.setRegistryName(Subaquatic.MODID, name).setTranslationKey(Subaquatic.MODID + "." + name).setCreativeTab(CreativeTab.INSTANCE));
        consumer.accept(item);
        return item;
    }

    //ore dict registration
    public static void registerOreDict() {
        OreDictionary.registerOre("shellNautilus", NAUTILUS_SHELL);
    }
}
