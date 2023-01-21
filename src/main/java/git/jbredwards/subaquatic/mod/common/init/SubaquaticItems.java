package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.block.AbstractBlockCoral;
import git.jbredwards.subaquatic.mod.common.item.*;
import git.jbredwards.subaquatic.mod.common.item.block.ItemBlockCluster;
import git.jbredwards.subaquatic.mod.common.item.block.ItemBlockMeta;
import git.jbredwards.subaquatic.mod.common.item.block.ItemBlockSeagrass;
import git.jbredwards.subaquatic.mod.common.item.boat.ItemEnderChestBoat;
import net.minecraft.init.Blocks;
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
public final class SubaquaticItems
{
    //item init
    @Nonnull public static final List<Item> INIT = new ArrayList<>();

    //item blocks
    @Nonnull public static final ItemBlock DRIED_KELP_BLOCK = register("dried_kelp_block", new ItemBlock(SubaquaticBlocks.DRIED_KELP_BLOCK));
    @Nonnull public static final ItemBlock BLUE_ICE = register("blue_ice", new ItemBlock(SubaquaticBlocks.BLUE_ICE));
    @Nonnull public static final ItemBlock PUMPKIN = register("pumpkin", new ItemBlock(SubaquaticBlocks.PUMPKIN));
    @Nonnull public static final ItemBlock TUBE_CORAL_BLOCK = register("tube_coral_block", new ItemBlockMeta(SubaquaticBlocks.TUBE_CORAL_BLOCK, AbstractBlockCoral.ALIVE, false));
    @Nonnull public static final ItemBlock BRAIN_CORAL_BLOCK = register("brain_coral_block", new ItemBlockMeta(SubaquaticBlocks.BRAIN_CORAL_BLOCK, AbstractBlockCoral.ALIVE, false));
    @Nonnull public static final ItemBlock BUBBLE_CORAL_BLOCK = register("bubble_coral_block", new ItemBlockMeta(SubaquaticBlocks.BUBBLE_CORAL_BLOCK, AbstractBlockCoral.ALIVE, false));
    @Nonnull public static final ItemBlock FIRE_CORAL_BLOCK = register("fire_coral_block", new ItemBlockMeta(SubaquaticBlocks.FIRE_CORAL_BLOCK, AbstractBlockCoral.ALIVE, false));
    @Nonnull public static final ItemBlock HORN_CORAL_BLOCK = register("horn_coral_block", new ItemBlockMeta(SubaquaticBlocks.HORN_CORAL_BLOCK, AbstractBlockCoral.ALIVE, false));
    @Nonnull public static final ItemBlock TUBE_CORAL_FAN = register("tube_coral_fan", new ItemBlockMeta(SubaquaticBlocks.TUBE_CORAL_FAN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock BRAIN_CORAL_FAN = register("brain_coral_fan", new ItemBlockMeta(SubaquaticBlocks.BRAIN_CORAL_FAN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock BUBBLE_CORAL_FAN = register("bubble_coral_fan", new ItemBlockMeta(SubaquaticBlocks.BUBBLE_CORAL_FAN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock FIRE_CORAL_FAN = register("fire_coral_fan", new ItemBlockMeta(SubaquaticBlocks.FIRE_CORAL_FAN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock HORN_CORAL_FAN = register("horn_coral_fan", new ItemBlockMeta(SubaquaticBlocks.HORN_CORAL_FAN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock TUBE_CORAL_FIN = register("tube_coral_fin", new ItemBlockMeta(SubaquaticBlocks.TUBE_CORAL_FIN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock BRAIN_CORAL_FIN = register("brain_coral_fin", new ItemBlockMeta(SubaquaticBlocks.BRAIN_CORAL_FIN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock BUBBLE_CORAL_FIN = register("bubble_coral_fin", new ItemBlockMeta(SubaquaticBlocks.BUBBLE_CORAL_FIN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock FIRE_CORAL_FIN = register("fire_coral_fin", new ItemBlockMeta(SubaquaticBlocks.FIRE_CORAL_FIN, AbstractBlockCoral.ALIVE, true));
    @Nonnull public static final ItemBlock HORN_CORAL_FIN = register("horn_coral_fin", new ItemBlockMeta(SubaquaticBlocks.HORN_CORAL_FIN, AbstractBlockCoral.ALIVE, true));

    //items
    @Nonnull public static final ItemBlockCluster SEA_PICKLE = register("sea_pickle", new ItemBlockCluster(SubaquaticBlocks.SEA_PICKLE));
    @Nonnull public static final ItemBlockSeagrass SEAGRASS = register("seagrass", new ItemBlockSeagrass(SubaquaticBlocks.SEAGRASS));
    @Nonnull public static final ItemBlock KELP = register("kelp", new ItemBlock(SubaquaticBlocks.KELP));
    @Nonnull public static final ItemFood DRIED_KELP = register("dried_kelp", new ItemDurationFood(1, false), item -> item.maxUseDuration = 16);
    @Nonnull public static final ItemBlock NAUTILUS_SHELL = register("nautilus_shell", new ItemBlock(SubaquaticBlocks.NAUTILUS_SHELL));

    //boat containers
    @Nonnull public static final ItemEnderChestBoat ENDER_CHEST_BOAT = register("ender_chest_boat", new ItemEnderChestBoat());

    //ore dict registration
    public static void registerOreDictionary() {
        OreDictionary.registerOre("cropPumpkin", PUMPKIN);
        OreDictionary.registerOre("cropPumpkin", Blocks.PUMPKIN);
        OreDictionary.registerOre("shellNautilus", NAUTILUS_SHELL);
    }

    @Nonnull
    static <I extends Item> I register(@Nonnull String name, @Nonnull I item) {
        INIT.add(item.setRegistryName(Subaquatic.MODID, name).setTranslationKey(Subaquatic.MODID + "." + name).setCreativeTab(SubaquaticCreativeTab.INSTANCE));
        return item;
    }

    @Nonnull
    static <I extends Item> I register(@Nonnull String name, @Nonnull I item, @Nonnull Consumer<I> consumer) {
        consumer.accept(item);
        return register(name, item);
    }
}
