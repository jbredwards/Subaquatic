/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public final class AirBubbleRenderer
{
    @Nonnull public static Predicate<Entity> IN_WATER = entity -> entity.isInsideOfMaterial(Material.WATER);
    @Nonnull public static ToIntFunction<Entity> MAX_AIR = entity -> 300;
    @Nonnull public static ResourceLocation
            BUBBLE = new ResourceLocation(Subaquatic.MODID, "textures/gui/hud/air_bubble.png"),
            BUBBLE_POPPING = new ResourceLocation(Subaquatic.MODID, "textures/gui/hud/air_bubble_pop.png"),
            BUBBLE_EMPTY = new ResourceLocation(Subaquatic.MODID, "textures/gui/hud/air_bubble_empty.png");

    private static boolean popSoundReady = true, popTexReady;
    private static int prevAir = Integer.MAX_VALUE;

    public static void renderAirBubbles(@Nonnull final ScaledResolution res, @Nonnull final Minecraft mc) {
        @Nonnull final Entity entity = Objects.requireNonNull(mc.getRenderViewEntity());

        final int maxAir = MAX_AIR.applyAsInt(entity);
        final int air = MathHelper.clamp(entity.getAir(), 0, maxAir);
        if(prevAir != air) {
            // Only render popped air bubble when oxygen is depleting.
            popTexReady = prevAir > air;
            prevAir = air;
        }

        if(air < maxAir || IN_WATER.test(entity)) {
            GlStateManager.enableBlend();
            final int left = res.getScaledWidth() / 2 + 91;
            final int top = res.getScaledHeight() - GuiIngameForge.right_height;

            final int next = MathHelper.ceil((air - 2) * 10f / maxAir);
            final int remaining = MathHelper.ceil(air * 10f / maxAir);
            final int depleted = 10 - remaining;

            if(next == remaining) popSoundReady = true;
            for(int i = 0; i < 10; ++i) {
                int bob = 0;

                // Render air bubble pop + sound.
                if(popTexReady && next != remaining && i == remaining - 1) {
                    mc.getTextureManager().bindTexture(BUBBLE_POPPING);
                    if(popSoundReady) {
                        popSoundReady = false;
                        entity.playSound(SubaquaticSounds.GUI_BUBBLE_POP, 0.5f + 0.1f * Math.max(0, depleted - 2), 1f + 0.1f * Math.max(0, depleted - 4));
                    }
                }

                // Render empty air bubbles + apply bobbing when fully depleted.
                else if(i > 9 - depleted) {
                    mc.getTextureManager().bindTexture(BUBBLE_EMPTY);
                    if(depleted == 10 && (mc.ingameGUI.getUpdateCounter() & 1) != 0) bob = mc.ingameGUI.rand.nextInt(2);
                }

                // Normal air bubble rendering.
                else if(i < next) mc.getTextureManager().bindTexture(BUBBLE);

                // Do texture render.
                @Nonnull final Tessellator tessellator = Tessellator.getInstance();
                @Nonnull final BufferBuilder buf = tessellator.getBuffer();
                final int x = left - i * 8 - 9;
                final int y = top + bob;
                buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                buf.pos(x,     y + 9, mc.ingameGUI.zLevel).tex(0, 1).endVertex();
                buf.pos(x + 9, y + 9, mc.ingameGUI.zLevel).tex(1, 1).endVertex();
                buf.pos(x + 9, y,     mc.ingameGUI.zLevel).tex(1, 0).endVertex();
                buf.pos(x,     y,     mc.ingameGUI.zLevel).tex(0, 0).endVertex();
                tessellator.draw();
            }

            mc.getTextureManager().bindTexture(Gui.ICONS);
            GuiIngameForge.right_height += 10;
            GlStateManager.disableBlend();
        }
    }
}
