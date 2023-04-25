package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Custom ItemBlock implementation for mushroom blocks to allow for new metadata properties
 * @author jbred
 *
 */
public final class PluginItem implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_150900_l" : "registerItems"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * registerItems: (changes are around lines 1538 & 1539)
         * Old code:
         * registerItemBlock(Blocks.BROWN_MUSHROOM_BLOCK);
         * registerItemBlock(Blocks.RED_MUSHROOM_BLOCK);
         *
         * New code:
         * //custom ItemBlock implementation for mushroom blocks to allow for new metadata properties
         * registerItemBlock(Blocks.BROWN_MUSHROOM_BLOCK, new ItemBlockHugeMushroom(Blocks.BROWN_MUSHROOM_BLOCK));
         * registerItemBlock(Blocks.RED_MUSHROOM_BLOCK, new ItemBlockHugeMushroom(Blocks.RED_MUSHROOM_BLOCK));
         */
        if(checkField(insn, obfuscated ? "field_150420_aW" : "BROWN_MUSHROOM_BLOCK") || checkField(insn, obfuscated ? "field_150419_aX" : "RED_MUSHROOM_BLOCK")) {
            final InsnList list = new InsnList();
            list.add(new TypeInsnNode(NEW, "git/jbredwards/subaquatic/mod/common/item/block/ItemBlockHugeMushroom"));
            list.add(new InsnNode(DUP));
            list.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Blocks", ((FieldInsnNode)insn).name, "Lnet/minecraft/block/Block;"));
            list.add(new MethodInsnNode(INVOKESPECIAL, "git/jbredwards/subaquatic/mod/common/item/block/ItemBlockHugeMushroom", "<init>", "(Lnet/minecraft/block/Block;)V", false));
            list.add(genMethodNode("net/minecraft/item/Item", obfuscated ? "func_179214_a" : "registerItemBlock", "(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item;)V"));

            instructions.remove(insn.getNext());
            instructions.insert(insn, list);
        }
        /*
         * registerItems: (changes are around line 1858)
         * Old code:
         * registerItem(437, "dragon_breath", (new Item()).setCreativeTab(CreativeTabs.BREWING).setTranslationKey("dragon_breath").setContainerItem(item1));
         *
         * New code:
         * //new item for Dragon's Breath to allow for optional enchant glint
         * registerItem(437, "dragon_breath", (new ItemDragonBreath()).setCreativeTab(CreativeTabs.BREWING).setTranslationKey("dragon_breath").setContainerItem(item1));
         */
        else if(insn.getOpcode() == LDC && ((LdcInsnNode)insn).cst.equals("dragon_breath")) {
            ((TypeInsnNode)insn.getNext()).desc = "git/jbredwards/subaquatic/mod/common/item/ItemDragonBreath";
            ((MethodInsnNode)getNext(insn, 3)).owner = "git/jbredwards/subaquatic/mod/common/item/ItemDragonBreath";
            return true;
        }

        return false;
    }
}
