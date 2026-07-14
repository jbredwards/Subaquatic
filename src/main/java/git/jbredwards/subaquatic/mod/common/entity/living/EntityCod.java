/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.ocean_api.api.entity.AbstractGroupFish;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticLootTables;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityCod extends AbstractGroupFish
{
    public EntityCod(@Nonnull World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.3f);
    }

    @Nonnull
    @Override
    protected ResourceLocation getLootTable() { return SubaquaticLootTables.ENTITIES_COD; }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() { return SubaquaticSounds.ENTITY_COD_DEATH; }

    @Nullable
    @Override
    public SoundEvent getFlopSound() { return SubaquaticSounds.ENTITY_COD_FLOP; }

    @Nonnull
    @Override
    protected SoundEvent getSwimSound() { return SubaquaticSounds.ENTITY_FISH_SWIM; }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) { return SubaquaticSounds.ENTITY_COD_HURT; }
}
