package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.common.potion.PotionBase;
import git.jbredwards.subaquatic.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * stores all of this mod's potion effects
 * @author jbred
 *
 */
public enum ModPotions
{
    ;

    //potion init
    public static final List<PotionBase> INIT = new ArrayList<>();

    //potions
    public static final PotionBase CONDUIT = register("conduit", new PotionBase(false, 1950417));

    //prepares the potion for registry
    public static <P extends PotionBase> P register(String name, P potion) {
        INIT.add(potion);
        potion.setRegistryName(name).setPotionName(Constants.MODID + ".effects." + name);
        potion.setIconIndex((INIT.size() - 1) % 14, (INIT.size() - 1) % 3);
        if(!potion.isBadEffect()) potion.setBeneficial();

        return potion;
    }
}
