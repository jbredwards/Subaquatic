package git.jbredwards.subaquatic.mod.common;

import git.jbredwards.subaquatic.api.event.OnGetEntityFromFishingEvent;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.capability.ICompactFishing;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import git.jbredwards.subaquatic.mod.common.message.SMessageBoatType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void realisticFishing(@Nonnull ItemFishedEvent event) {
        if(SubaquaticConfigHandler.Server.Item.realisticFishing) {
            final ICompactFishing cap = ICompactFishing.get(event.getHookEntity());
            final int compactFishingLvl = cap != null ? cap.getLevel() : 0;
            int rodDamage = 0;

            for(final Iterator<ItemStack> it = event.getDrops().iterator(); it.hasNext();) {
                final OnGetEntityFromFishingEvent entityEvent = new OnGetEntityFromFishingEvent(it.next(), event.getHookEntity(), event.getRodDamage(), compactFishingLvl);
                if(MinecraftForge.EVENT_BUS.post(entityEvent)) {
                    it.remove();
                    event.setCanceled(true);
                }

                rodDamage += entityEvent.rodDamage;
            }

            if(rodDamage != 0) event.damageRodBy(rodDamage);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void realisticFishingResult(@Nonnull OnGetEntityFromFishingEvent event) {
        if(event.itemToFish.getItem() == SubaquaticItems.COD) event.spawnEntityOrTryCompact(new EntityCod(event.getWorld()));
        else if(event.itemToFish.getItem() == Items.FISH) switch(event.itemToFish.getMetadata()) {
            case 0: event.spawnEntityOrTryCompact(new EntityFish(event.getWorld()));
                break;
            case 1: event.spawnEntityOrTryCompact(new EntitySalmon(event.getWorld()));
                break;
            case 2: event.spawnEntityOrTryCompact(new EntityTropicalFish(event.getWorld()));
                break;
            default: event.spawnEntityOrTryCompact(new EntityPufferfish(event.getWorld()));
        }
    }

    @SubscribeEvent
    static void syncBoatContainers(@Nonnull PlayerEvent.StartTracking event) {
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            final IBoatType cap = IBoatType.get(event.getTarget());
            if(cap != null) Subaquatic.WRAPPER.sendTo(
                    new SMessageBoatType(cap.getType(), event.getTarget()), (EntityPlayerMP)event.getEntityPlayer());
        }
    }

    /*@SubscribeEvent
    static void underwaterBonemeal(@Nonnull BonemealEvent event) {
        final Block block = event.getBlock().getBlock();
        if(event.getBlock().isFullCube() && !Block.isEqualTo(block, Blocks.MAGMA)) {
            //only do default behavior if the block is not growable
            final World world = event.getWorld();
            final BlockPos pos = event.getPos();
            if(block instanceof IGrowable && ((IGrowable)block).canGrow(world, pos, event.getBlock(), world.isRemote))
                return;

            //check for water above
            final IBlockState above = world.getBlockState(pos.up());
            if(above.getBlock().isReplaceable(world, pos.up()) && FluidloggedUtils.getFluidOrReal(world, pos.up(), above).getMaterial() == Material.WATER) {
                if(!world.isRemote) {
                    final Random rand = world.rand;
                    //copied from BlockGrass
                    BlockPos plantPos = pos.up();
                    for(int i = 0; i < 128; ++i) {
                        int j = 0;
                        while(true) {
                            if(j >= i / 16) {
                                if(FluidloggedUtils.getFluidOrReal(world, plantPos).getMaterial() == Material.WATER) {
                                    //plant sea flora if possible
                                    if(world.rand.nextInt(8) == 0) {
                                        final Biome biome = world.getBiome(plantPos);
                                        if(biome instanceof ISeaFloraBiome) ((ISeaFloraBiome)biome).plantSeaFlower(world, rand, plantPos);
                                    }

                                    //plant seagrass is possible
                                    else if(world.getBlockState(plantPos.down()).isTopSolid() && SubaquaticBlocks.SEAGRASS.canPlaceBlockAt(world, plantPos))
                                        world.setBlockState(plantPos, SubaquaticBlocks.SEAGRASS.getDefaultState());
                                }

                                break;
                            }

                            plantPos = plantPos.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                            if(world.getBlockState(plantPos.down()) != event.getBlock() || world.getBlockState(plantPos).isNormalCube())
                                break;

                            ++j;
                        }
                    }
                }

                event.setResult(Event.Result.ALLOW);
            }
        }
    }*/
}
