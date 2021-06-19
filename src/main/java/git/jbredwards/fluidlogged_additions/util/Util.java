package git.jbredwards.fluidlogged_additions.util;

/**
 * helpful misc functions
 * @author jbred
 *
 */
public enum Util
{
    ;

    //returns the closest value
    public static float closest(float value, float... values) {
        float dif = Integer.MAX_VALUE;
        float closest = values[0];

        for(float current : values) {
            float compare = Math.abs(value - current);

            //updates the closest
            if(compare < dif) {
                dif = compare;
                closest = current;
            }
        }

        return closest;
    }
}
