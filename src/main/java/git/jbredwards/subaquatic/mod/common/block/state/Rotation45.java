package git.jbredwards.subaquatic.mod.common.block.state;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public enum Rotation45 implements IStringSerializable
{
    DEG_0  ("0",   0),
    DEG_45 ("45",  45),
    DEG_90 ("90",  90),
    DEG_135("135", 135),
    DEG_180("180", 180),
    DEG_225("225", 225),
    DEG_270("270", 270),
    DEG_315("315", 315);

    @Nonnull
    public final String name;
    public final int angle;

    Rotation45(@Nonnull String nameIn, int angleIn) {
        name = nameIn;
        angle = angleIn;
    }

    @Nonnull
    @Override
    public String getName() { return name; }

    @Nonnull
    public Rotation45 rotate(Rotation rotationIn) { return rotate(fromRotation(rotationIn)); }

    @Nonnull
    public Rotation45 rotate(Rotation45 rotationIn) { return rotate(rotationIn.angle); }

    @Nonnull
    public Rotation45 rotate(float rotationIn) { return fromRotation(angle + rotationIn); }

    @Nonnull
    public static Rotation45 fromRotation(float rotationIn) {
        //angle must be between 0 - 360.
        rotationIn %= 360;
        //gets the Rot from the closest
        switch((int)(rotationIn / 45 + 0.5) * 45) {
            case 45:  return DEG_45;
            case 90:  return DEG_90;
            case 135: return DEG_135;
            case 180: return DEG_180;
            case 225: return DEG_225;
            case 270: return DEG_270;
            case 315: return DEG_315;
            default:  return DEG_0;
        }
    }

    @Nonnull
    public static Rotation45 fromRotation(@Nonnull Rotation rotationIn) {
        switch(rotationIn) {
            case CLOCKWISE_90:        return DEG_90;
            case CLOCKWISE_180:       return DEG_180;
            case COUNTERCLOCKWISE_90: return DEG_270;
            default:                  return DEG_0;
        }
    }

    @Nonnull
    public Rotation45 mirror(@Nonnull Mirror mirror) {
        switch(mirror) {
            case LEFT_RIGHT: {
                switch(this) {
                    case DEG_45:  return DEG_315;
                    case DEG_90:  return DEG_270;
                    case DEG_135: return DEG_225;
                    case DEG_225: return DEG_135;
                    case DEG_315: return DEG_45;
                    default:      return this;
                }
            }
            case FRONT_BACK: {
                switch(this) {
                    case DEG_0:   return DEG_180;
                    case DEG_45:  return DEG_135;
                    case DEG_135: return DEG_45;
                    case DEG_180: return DEG_0;
                    case DEG_225: return DEG_315;
                    case DEG_315: return DEG_225;
                    default:      return this;
                }
            }
            //no mirror
            default: return this;
        }
    }
}
