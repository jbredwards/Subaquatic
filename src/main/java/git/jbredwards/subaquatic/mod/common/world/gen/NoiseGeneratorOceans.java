/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen;

import net.minecraft.world.gen.NoiseGeneratorImproved;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Used when gathering ocean biomes.
 * @author jbred
 *
 */
public class NoiseGeneratorOceans extends NoiseGeneratorImproved
{
    public NoiseGeneratorOceans(@Nonnull Random rand) { super(rand); }
    public double getValue(double xIn, double yIn, double zIn) {
        double x = xCoord + xIn;
        double y = yCoord + yIn;
        double z = zCoord + zIn;

        int xi = (int)x;
        if(x < xi) --xi;

        int yi = (int)y;
        if(y < yi) --yi;

        int zi = (int)z;
        if(z < zi) --zi;

        x = x - xi;
        y = y - yi;
        z = z - zi;

        final double gradX = x * x * x * (x * (x * 6 - 15) + 10);
        final double gradY = y * y * y * (y * (y * 6 - 15) + 10);
        final double gradZ = z * z * z * (z * (z * 6 - 15) + 10);
        final int k1 = permutations[xi & 255] + (yi & 255);
        final int l1 = permutations[k1] + (zi & 255);
        final int i2 = permutations[k1 + 1] + (zi & 255);
        final int j2 = permutations[(xi & 255) + 1] + (yi & 255);
        final int k2 = permutations[j2] + (zi & 255);
        final int l2 = permutations[j2 + 1] + (zi & 255);
        return lerp(gradZ,
                lerp(gradY,
                        lerp(gradX, grad(permutations[l1], x, y, z), grad(permutations[k2], x - 1, y, z)),
                        lerp(gradX, grad(permutations[i2], x, y - 1, z), grad(permutations[l2], x - 1, y - 1, z))),
                lerp(gradY,
                        lerp(gradX, grad(permutations[l1 + 1], x, y, z - 1), grad(permutations[k2 + 1], x - 1, y, z - 1)),
                        lerp(gradX, grad(permutations[i2 + 1], x, y - 1, z - 1), grad(permutations[l2 + 1], x - 1, y - 1, z - 1))));
    }
}
