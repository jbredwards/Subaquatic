package git.jbredwards.subaquatic.mod.client.config;

import git.jbredwards.fluidlogged_api.mod.client.config.gui.GuiComponentConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiEditArray;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public class SubaquaticGuiConfig extends GuiComponentConfig
{
    public SubaquaticGuiConfig(@Nonnull final GuiScreen parentScreen, @Nonnull final List<IConfigElement> configElements, @Nonnull final String modID,
                              final boolean allRequireWorldRestart, final boolean allRequireMcRestart, @Nonnull final String title, @Nullable final String titleLine2) {
        super(parentScreen, configElements, modID, allRequireWorldRestart, allRequireMcRestart, title, titleLine2);
        childScreenBuilder = SubaquaticGuiConfig::new;
    }

    @Nonnull
    @Override
    protected GuiConfigEntries.IConfigEntry createConfigEntry(@Nonnull final GuiConfigEntries.IConfigEntry original) {
        return original.getConfigElement().isList() ? new GuiConfigEntries.ArrayEntry(this, this.entryList, original.getConfigElement()) {
            @Override
            public void valueButtonPressed(final int slotIndex) {
                mc.displayGuiScreen(new GuiEditArray(owningScreen, configElement, slotIndex, currentValues, enabled()) {
                    @Nonnull
                    @Override
                    protected GuiEditArrayEntries createEditArrayEntries() {
                        return new GuiEditArrayEntries(this, mc, configElement, beforeValues, currentValues) {
                            @Override
                            protected void overlayBackground(final int startY, final int endY, final int startAlpha, final int endAlpha) {
                                if(isWorldRunning) drawGradientRect(left, endY, left + width, startY, -1072689136, -804253680);
                                else super.overlayBackground(startY, endY, startAlpha, endAlpha);
                            }

                            @Override
                            protected void drawContainerBackground(@Nonnull final Tessellator tessellator) {
                                if(isWorldRunning) drawGradientRect(right, bottom, left, top, -1072689136, -804253680);
                                else super.drawContainerBackground(tessellator);
                            }

                            @Override
                            protected void drawSelectionBox(final int insideLeft, final int insideTop, final int mouseXIn, final int mouseYIn, final float partialTicks) {
                                final double scaleH = mc.displayHeight / new ScaledResolution(mc).getScaledHeight_double();
                                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                                GL11.glScissor(0, (int)(mc.displayHeight - (bottom * scaleH)), mc.displayWidth, (int)((bottom - top) * scaleH));
                                super.drawSelectionBox(insideLeft, insideTop, mouseXIn, mouseYIn, partialTicks);
                                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                            }
                        };
                    }
                });
            }
        }: super.createConfigEntry(original);
    }
}
