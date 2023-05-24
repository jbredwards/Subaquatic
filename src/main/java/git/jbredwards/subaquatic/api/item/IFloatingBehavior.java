package git.jbredwards.subaquatic.api.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;

import javax.annotation.Nonnull;

/**
 * Items should implement this if they have custom floating behavior that differs from vanilla 1.13+.
 *
 * @since 1.1.0
 * @author jbred
 *
 */
public interface IFloatingBehavior
{
    default boolean canEntityFloat(@Nonnull EntityItem entityItem, boolean configEnabled) {
        return configEnabled && entityItem.isInsideOfMaterial(Material.WATER);
    }

    default void doEntityFloat(@Nonnull EntityItem entityItem) {
        if(entityItem.motionY < 0.06) entityItem.motionY += 5.0E-4;

        entityItem.motionX *= 0.99;
        entityItem.motionZ *= 0.99;
    }
}
