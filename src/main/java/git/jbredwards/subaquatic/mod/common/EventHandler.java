package git.jbredwards.subaquatic.mod.common;

import git.jbredwards.subaquatic.api.event.OnGetEntityFromFishingEvent;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticProfessions;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import git.jbredwards.subaquatic.mod.common.message.SMessageBoatType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public final class EventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void improveVanillaBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        Blocks.BROWN_MUSHROOM.setTranslationKey(Subaquatic.MODID + ".brown_mushroom");
        Blocks.BROWN_MUSHROOM_BLOCK.setTranslationKey(Blocks.BROWN_MUSHROOM.translationKey);
        Blocks.PUMPKIN.setTranslationKey(Subaquatic.MODID + ".carved_pumpkin");
        Blocks.RED_MUSHROOM.setTranslationKey(Subaquatic.MODID + ".red_mushroom");
        Blocks.RED_MUSHROOM_BLOCK.setTranslationKey(Blocks.RED_MUSHROOM.translationKey);

        Blocks.FLOWING_WATER.setLightOpacity(2);
        Blocks.WATER.setLightOpacity(2);
        Blocks.WATERLILY.setSoundType(SubaquaticSounds.WET_GRASS);
    }

    @SubscribeEvent
    static void modifyLootTables(@Nonnull LootTableLoadEvent event) throws NullPointerException { //should never throw
        if("minecraft:gameplay/fishing/fish".equals(event.getName().toString())) event.getTable().getPool("main").addEntry(
                new LootEntryItem(SubaquaticItems.COD, 30, 1, new LootFunction[0], new LootCondition[0], "subaquatic:cod"));
    }

    @SubscribeEvent
    static void syncBoatContainers(@Nonnull PlayerEvent.StartTracking event) {
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            final IBoatType cap = IBoatType.get(event.getTarget());
            if(cap != null) Subaquatic.WRAPPER.sendTo(
                    new SMessageBoatType(cap.getType(), event.getTarget()), (EntityPlayerMP)event.getEntityPlayer());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void realisticFishing(@Nonnull ItemFishedEvent event) {
        if(SubaquaticConfigHandler.Server.Item.realisticFishing) {
            for(final Iterator<ItemStack> it = event.getDrops().iterator(); it.hasNext();) {
                final OnGetEntityFromFishingEvent entityEvent = new OnGetEntityFromFishingEvent(it.next(), event.getHookEntity(), event.getRodDamage());
                if(MinecraftForge.EVENT_BUS.post(entityEvent)) {
                    it.remove();
                    event.setCanceled(true);
                    event.damageRodBy(entityEvent.rodDamage);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void getEntityFromFishing(@Nonnull OnGetEntityFromFishingEvent event) {
        if(event.itemToFish.getItem() == SubaquaticItems.COD) event.spawnEntity(new EntityCod(event.getWorld()));
        else if(event.itemToFish.getItem() == Items.FISH) switch(event.itemToFish.getMetadata()) {
            case 0: event.spawnEntity(new EntityFish(event.getWorld()));
                break;
            case 1: event.spawnEntity(new EntitySalmon(event.getWorld()));
                break;
            case 2: event.spawnEntity(new EntityTropicalFish(event.getWorld()));
                break;
            default: event.spawnEntity(new EntityPufferfish(event.getWorld()));
        }
    }
}
