/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityBogged extends AbstractSkeleton
{
    public EntityBogged(@Nonnull final World worldIn) { super(worldIn); }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16);
    }

    @Nonnull
    @Override
    protected EntityArrow getArrow(final float distanceFactor) {
        @Nonnull final EntityArrow arrow = super.getArrow(distanceFactor);
        if(arrow instanceof EntityTippedArrow) ((EntityTippedArrow)arrow).addEffect(new PotionEffect(MobEffects.POISON, 100));
        return arrow;
    }

    @Override
    public void setCombatTask() {
        super.setCombatTask();
        aiArrowAttack.setAttackCooldown(world.getDifficulty() == EnumDifficulty.HARD ? 50 : 70);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() { return new ResourceLocation(Subaquatic.MODID, "entities/bogged");}

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return SubaquaticSounds.ENTITY_BOGGED_AMBIENT; }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return SubaquaticSounds.ENTITY_BOGGED_DEATH; }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull final DamageSource damageSourceIn) { return SubaquaticSounds.ENTITY_BOGGED_HURT; }

    @Nonnull
    @Override
    protected SoundEvent getStepSound() { return SubaquaticSounds.ENTITY_BOGGED_STEP; }
}
