package git.jbredwards.subaquatic.mod.client.config;

import git.jbredwards.fluidlogged_api.mod.client.config.element.ToggleableConfigElement;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.IConfigElement;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticGuiFactory extends DefaultGuiFactory
{
    public SubaquaticGuiFactory() {
        super(Subaquatic.MODID, Subaquatic.NAME);
    }

    @Nonnull
    @Override
    public GuiScreen createConfigGui(@Nonnull final GuiScreen parentScreen) {
        @Nonnull final List<IConfigElement> configElements = new ArrayList<>(ConfigElement.from(SubaquaticConfigHandler.class).getChildElements());

        configElements.replaceAll(element -> new ToggleableConfigElement(element, ToggleableConfigElement::new));
        return new SubaquaticGuiConfig(parentScreen, configElements, modid, false, false, I18n.format("configgui." + modid), null);
    }
}
