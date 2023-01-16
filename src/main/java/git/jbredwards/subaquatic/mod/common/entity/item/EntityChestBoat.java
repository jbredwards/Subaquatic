package git.jbredwards.subaquatic.mod.common.entity.item;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 *
 * @author jbred
 *
 */
public class EntityChestBoat extends EntityBoat
{


    public EntityChestBoat(@Nonnull World worldIn) { super(worldIn); }
    public EntityChestBoat(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    public static void registerFixer(@Nonnull DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityChestBoat.class, "Items"));
    }
}
