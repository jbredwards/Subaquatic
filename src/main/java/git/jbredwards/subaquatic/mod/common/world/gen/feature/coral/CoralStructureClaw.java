package git.jbredwards.subaquatic.mod.common.world.gen.feature.coral;

import com.google.common.collect.Lists;
import git.jbredwards.subaquatic.mod.common.block.BlockCoral;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum CoralStructureClaw implements ICoralStructure
{
    INSTANCE;

    @Override
    public void generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockCoral coral) {
        if(placeCoralBlock(world, rand, pos, coral)) {
            final EnumFacing primarySide = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];
            final List<EnumFacing> sidesToGen = Lists.newArrayList(primarySide, primarySide.rotateY(), primarySide.rotateYCCW());
            Collections.shuffle(sidesToGen);

            sidesToGen.subList(0, rand.nextInt(2) + 2).forEach(side -> {
                final BlockPos.MutableBlockPos posToGen = new BlockPos.MutableBlockPos(pos);
                posToGen.move(side);

                int amountToGen;
                if(side == primarySide) amountToGen = rand.nextInt(3) + 2;
                else {
                    posToGen.move(EnumFacing.UP);
                    if(rand.nextBoolean()) side = EnumFacing.UP;
                    amountToGen = rand.nextInt(3) + 3;
                }

                final int max = rand.nextInt(2) + 1;
                for(int i = 0; i < max && placeCoralBlock(world, rand, posToGen.toImmutable(), coral); i++)
                    posToGen.move(side);

                posToGen.move(side.getOpposite());
                posToGen.move(EnumFacing.UP);

                for(int i = 0; i < amountToGen; i++) {
                    posToGen.move(primarySide);
                    if(!placeCoralBlock(world, rand, posToGen.toImmutable(), coral)) break;
                    if(rand.nextFloat() < 0.25) posToGen.move(EnumFacing.UP);
                }
            });
        }
    }
}
