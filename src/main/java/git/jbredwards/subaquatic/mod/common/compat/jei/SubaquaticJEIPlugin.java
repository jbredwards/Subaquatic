package git.jbredwards.subaquatic.mod.common.compat.jei;

import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@JEIPlugin
public final class SubaquaticJEIPlugin implements IModPlugin
{
    @Nonnull
    final ISubtypeRegistry.ISubtypeInterpreter boatContainerInterpreter = stack -> {
        final IBoatType cap = IBoatType.get(stack);
        return cap != null ? cap.getType().serializeNBT().toString() : "";
    };

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.CHEST_BOAT, boatContainerInterpreter);
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.ENDER_CHEST_BOAT, boatContainerInterpreter);
    }
}
