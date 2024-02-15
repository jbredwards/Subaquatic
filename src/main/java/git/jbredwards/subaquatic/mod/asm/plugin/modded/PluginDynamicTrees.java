/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Fix dynamic trees water roots not using the correct water textures
 * @author jbred
 *
 */
public final class PluginDynamicTrees implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * Constructor:
         * New code:
         * //Fix dynamic trees water roots not using the correct water textures
         * ASMOverride
         * public ModelRootyWater(IBakedModel rootsModel)
         * {
         *     super();
         *     this.rootsModel = rootsModel;
         *     this.stillWaterTexture = (TextureAtlasSprite)ModelLoader.defaultTextureGetter().apply(FluidRegistry.WATER.getStill());
         *     this.flowWaterTexture = (TextureAtlasSprite)ModelLoader.defaultTextureGetter().apply(FluidRegistry.WATER.getFlowing());
         * }
         */
        overrideMethod(classNode, method -> method.name.equals("<init>"), null, null, generator -> {
            generator.visitVarInsn(ALOAD, 0);
            generator.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

            generator.visitVarInsn(ALOAD, 0);
            generator.visitVarInsn(ALOAD, 1);
            generator.visitFieldInsn(PUTFIELD, "com/ferreusveritas/dynamictrees/models/ModelRootyWater", "rootsModel", "Lnet/minecraft/client/renderer/block/model/IBakedModel;");

            generator.visitVarInsn(ALOAD, 0);
            generator.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/model/ModelLoader", "defaultTextureGetter", "()Ljava/util/function/Function;", false);
            generator.visitFieldInsn(GETSTATIC, "net/minecraftforge/fluids/FluidRegistry", "WATER", "Lnet/minecraftforge/fluids/Fluid;");
            generator.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/Fluid", "getStill", "()Lnet/minecraft/util/ResourceLocation;", false);
            generator.visitMethodInsn(INVOKEINTERFACE, "java/util/function/Function", "apply", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            generator.visitTypeInsn(CHECKCAST, "net/minecraft/client/renderer/texture/TextureAtlasSprite");
            generator.visitFieldInsn(PUTFIELD, "com/ferreusveritas/dynamictrees/models/ModelRootyWater", "stillWaterTexture", "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;");

            generator.visitVarInsn(ALOAD, 0);
            generator.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/model/ModelLoader", "defaultTextureGetter", "()Ljava/util/function/Function;", false);
            generator.visitFieldInsn(GETSTATIC, "net/minecraftforge/fluids/FluidRegistry", "WATER", "Lnet/minecraftforge/fluids/Fluid;");
            generator.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/Fluid", "getFlowing", "()Lnet/minecraft/util/ResourceLocation;", false);
            generator.visitMethodInsn(INVOKEINTERFACE, "java/util/function/Function", "apply", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            generator.visitTypeInsn(CHECKCAST, "net/minecraft/client/renderer/texture/TextureAtlasSprite");
            generator.visitFieldInsn(PUTFIELD, "com/ferreusveritas/dynamictrees/models/ModelRootyWater", "flowWaterTexture", "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;");
        });

        return false;
    }
}
