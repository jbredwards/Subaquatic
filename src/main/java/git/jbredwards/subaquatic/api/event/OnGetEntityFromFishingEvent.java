/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

/**
 * Called by this mod's {@link git.jbredwards.subaquatic.mod.common.EventHandler#realisticFishing EventHandler},
 * and is fired serverside only. This is used to spawn entities in place of items when fishing (enabled by default in the config).
 *
 * @since 1.1.0
 * @author jbred
 *
 */
@Cancelable
public class OnGetEntityFromFishingEvent extends Event
{
    @Nonnull public final ItemStack itemToFish;
    @Nonnull public final EntityFishHook hook;
    @Nonnull public final EntityPlayer player;
    public int rodDamage; //the amount of durability the fishing rod will lose
    public int compactFishingLvl; //the level of this mod's compact fishing enchantment on the fishing rod

    public OnGetEntityFromFishingEvent(@Nonnull ItemStack itemToFishIn, @Nonnull EntityFishHook hookIn, int rodDamageIn, int compactFishingLvlIn) {
        itemToFish = itemToFishIn;
        hook = hookIn;
        player = hookIn.getAngler();
        rodDamage = rodDamageIn;
        compactFishingLvl = compactFishingLvlIn;
    }

    /**
     * Spawns and positions the entity to be fished, if the compact fishing enchantment is not present.
     */
    public void spawnEntityOrTryCompact(@Nonnull Entity entity) {
        if(compactFishingLvl != 0 && entity instanceof EntityLivingBase) {
            final float maxHealth = ((EntityLivingBase)entity).getMaxHealth();
            final int maxDamage = (1 << compactFishingLvl) + 1;

            rodDamage += Math.min(compactFishingLvl, MathHelper.ceil(Math.log(maxHealth)));
            if(maxHealth > maxDamage) spawnEntity(entity, maxDamage);
        }

        else spawnEntity(entity, 1);
    }

    /**
     * Spawns and positions the entity to be fished, applies the correct motion, increments the player's fishing stat,
     * and deals `damageDealt` damage (having a hook in your lip hurts).
     */
    public void spawnEntity(@Nonnull Entity entity, int damageDealt) {
        entity.setPosition(hook.posX, hook.posY, hook.posZ);
        if(getWorld().spawnEntity(entity)) {
            if(damageDealt > 0) entity.attackEntityFrom(DamageSource.causeIndirectDamage(hook, player), damageDealt);
            applyMotion(entity);

            setCanceled(true);
            if(entity instanceof EntityLiving) player.addStat(StatList.FISH_CAUGHT, 1);
        }
    }

    /**
     * Applies the motion of being fished.
     */
    public void applyMotion(@Nonnull Entity entity) {
        final double diffX = (player.posX - hook.posX) * 0.125;
        final double diffY = (player.posY - hook.posY) * 0.125;
        final double diffZ = (player.posZ - hook.posZ) * 0.125;
        entity.motionX = diffX;
        entity.motionY = diffY + Math.pow(diffX * diffX + diffY * diffY + diffZ * diffZ, 0.25) * 0.5;
        entity.motionZ = diffZ;
    }

    @Nonnull
    public World getWorld() { return hook.getEntityWorld(); }
}
