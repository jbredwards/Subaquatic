package git.jbredwards.subaquatic.mod.asm.plugin.forge;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.entity.IConditionalProfession;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Add IConditionalProfession functionality
 * @author jbred
 *
 */
public final class PluginVillagerRegistry implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("setRandomProfession"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * setRandomProfession: (changes are around line 318)
         * Old code:
         * entity.setProfession((VillagerRegistry.VillagerProfession)INSTANCE.REGISTRY.getRandomObject(rand));
         *
         * New code:
         * //add IConditionalProfession functionality
         * entity.setProfession((VillagerRegistry.VillagerProfession)Hooks.checkIsProfessionEnabled(INSTANCE.REGISTRY.getRandomObject(rand), entity, rand));
         */
        if(checkMethod(insn, obfuscated ? "func_186801_a" : "getRandomObject")) {
            instructions.insert(insn, genMethodNode("checkIsProfessionEnabled", "(Ljava/lang/Object;Lnet/minecraft/entity/Entity;Ljava/util/Random;)Ljava/lang/Object;"));
            instructions.insert(insn, new VarInsnNode(ALOAD, 1));
            instructions.insert(insn, new VarInsnNode(ALOAD, 0));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static Object checkIsProfessionEnabled(@Nonnull Object profession, @Nonnull Entity entity, @Nonnull Random rand) {
            if(profession instanceof IConditionalProfession && !((IConditionalProfession)profession).isProfessionApplicableTo(entity)) {
                final VillagerRegistry.VillagerProfession[] professions = ForgeRegistries.VILLAGER_PROFESSIONS.getValuesCollection().stream()
                        .filter(p -> !(p instanceof IConditionalProfession) || ((IConditionalProfession)p).isProfessionApplicableTo(entity))
                        .toArray(VillagerRegistry.VillagerProfession[]::new);

                return professions[rand.nextInt(professions.length)];
            }

            return profession;
        }
    }
}
