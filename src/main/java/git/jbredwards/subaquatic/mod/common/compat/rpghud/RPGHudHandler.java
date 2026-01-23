/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.compat.rpghud;

import net.spellcraftgaming.rpghud.gui.hud.element.HudElementType;
import net.spellcraftgaming.rpghud.main.ModRPGHud;
import net.spellcraftgaming.rpghud.settings.Settings;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class RPGHudHandler
{
    public static boolean shouldRenderVanilla() {
        @Nonnull final Settings settings = ModRPGHud.instance.settings;
        if(settings.doesSettingExist("prevent_event_air") && settings.getBoolValue("prevent_event_air")) return false;

        else if(ModRPGHud.instance.getActiveHud().isVanillaElement(HudElementType.AIR)) return true;
        else return settings.doesSettingExist("render_vanilla_air") && settings.getBoolValue("render_vanilla_air");
    }
}
