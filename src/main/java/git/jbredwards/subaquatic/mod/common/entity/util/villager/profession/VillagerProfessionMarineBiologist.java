package git.jbredwards.subaquatic.mod.common.entity.util.villager.profession;

import git.jbredwards.subaquatic.api.entity.IConditionalProfession;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticProfessions;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public class VillagerProfessionMarineBiologist extends VillagerRegistry.VillagerProfession implements IConditionalProfession
{
    public VillagerProfessionMarineBiologist(@Nonnull String name, @Nonnull String texture, @Nonnull String zombie) {
        super(name, texture, zombie);
    }

    @Override
    public boolean isProfessionApplicableTo(@Nonnull Entity entity) {
        if(entity instanceof EntityVillager) return SubaquaticConfigHandler.Server.Entity.villagerMarineBiologist;
        else if(entity instanceof EntityZombieVillager) return SubaquaticConfigHandler.Server.Entity.zombieVillagerMarineBiologist;
        else return true;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void canBreatheUnderwater(@Nonnull LivingAttackEvent event) {
        if(event.getSource() == DamageSource.DROWN
        && event.getEntity() instanceof EntityVillager
        && ((EntityVillager)event.getEntity()).getProfessionForge() == SubaquaticProfessions.MARINE_BIOLOGIST
        && event.getEntity().isInsideOfMaterial(Material.WATER)) event.setCanceled(true);
    }
}
