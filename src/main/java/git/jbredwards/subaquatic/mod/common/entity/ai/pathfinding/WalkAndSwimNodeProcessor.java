/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.pathfinding;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

/**
 *
 * @author jbred
 *
 */
public class WalkAndSwimNodeProcessor extends WalkNodeProcessor
{
    @Nonnull
    public static final PathNodeType WATER_BORDER = Objects.requireNonNull(EnumHelper.addEnum(PathNodeType.class, Subaquatic.MODID + "_water_border", new Class<?>[] {float.class}, 8f));

    protected final boolean prefersShallowSwimming;
    protected float avoidsWalking, avoidsWaterBorder;

    public WalkAndSwimNodeProcessor(final boolean prefersShallowSwimmingIn) {
        prefersShallowSwimming = prefersShallowSwimmingIn;
    }

    @Override
    public void init(@Nonnull final IBlockAccess sourceIn, @Nonnull final EntityLiving mob) {
        super.init(sourceIn, mob);
        mob.setPathPriority(PathNodeType.WATER, 0);

        avoidsWalking = mob.getPathPriority(PathNodeType.WALKABLE);
        mob.setPathPriority(PathNodeType.WALKABLE, 6);

        avoidsWaterBorder = mob.getPathPriority(WATER_BORDER);
        mob.setPathPriority(WATER_BORDER, 4);
    }

    @Override
    public void postProcess() {
        entity.setPathPriority(PathNodeType.WALKABLE, avoidsWalking);
        entity.setPathPriority(WATER_BORDER, avoidsWaterBorder);
        super.postProcess();
    }

    @Nonnull
    @Override
    public PathPoint getStart() {
        return getPathPointToCoords(entity.getEntityBoundingBox().minX, entity.getEntityBoundingBox().minY, entity.getEntityBoundingBox().minZ);
    }

    @Nonnull
    @Override
    public PathPoint getPathPointToCoords(final double x, final double y, final double z) {
        return openPoint(MathHelper.floor(x), MathHelper.floor(y + 0.5), MathHelper.floor(z));
    }

    @Override
    public int findPathOptions(@Nonnull final PathPoint[] pathOptions, @Nonnull final PathPoint currentPoint, @Nonnull final PathPoint targetPoint, final float maxDistance) {
        final double currentBlockHeight = getBlockHeightAt(new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z));
        int options = 0;

        @Nullable final PathPoint pathS = getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z + 1, 1, currentBlockHeight);
        @Nullable final PathPoint pathW = getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z, 1, currentBlockHeight);
        @Nullable final PathPoint pathE = getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z, 1, currentBlockHeight);
        @Nullable final PathPoint pathN = getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z - 1, 1, currentBlockHeight);
        @Nullable final PathPoint pathU = getSafePoint(currentPoint.x, currentPoint.y + 1, currentPoint.z, 0, currentBlockHeight);
        @Nullable final PathPoint pathD = getSafePoint(currentPoint.x, currentPoint.y - 1, currentPoint.z, 1, currentBlockHeight);

        if(pathS != null && !pathS.visited && pathS.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathS;
        if(pathW != null && !pathW.visited && pathW.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathW;
        if(pathE != null && !pathE.visited && pathE.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathE;
        if(pathN != null && !pathN.visited && pathN.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathN;
        if(pathU != null && !pathU.visited && pathU.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathU;
        if(pathD != null && !pathD.visited && pathD.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathD;

        final boolean noPathN = pathN == null || pathN.nodeType == PathNodeType.OPEN || pathN.costMalus != 0;
        final boolean noPathS = pathS == null || pathS.nodeType == PathNodeType.OPEN || pathS.costMalus != 0;
        final boolean noPathE = pathE == null || pathE.nodeType == PathNodeType.OPEN || pathE.costMalus != 0;
        final boolean noPathW = pathW == null || pathW.nodeType == PathNodeType.OPEN || pathW.costMalus != 0;

        if(noPathN && noPathW) {
            @Nullable final PathPoint pathNW = getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z - 1, 1, currentBlockHeight);
            if(pathNW != null && !pathNW.visited && pathNW.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathNW;
        }

        if(noPathN && noPathE) {
            @Nullable final PathPoint pathNE = getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z - 1, 1, currentBlockHeight);
            if(pathNE != null && !pathNE.visited && pathNE.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathNE;
        }

        if(noPathS && noPathW) {
            @Nullable final PathPoint pathSW = getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z + 1, 1, currentBlockHeight);
            if(pathSW != null && !pathSW.visited && pathSW.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathSW;
        }

        if(noPathS && noPathE) {
            @Nullable final PathPoint pathSE = getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z + 1, 1, currentBlockHeight);
            if(pathSE != null && !pathSE.visited && pathSE.distanceTo(targetPoint) < maxDistance) pathOptions[options++] = pathSE;
        }

        return options;
    }

    double getBlockHeightAt(@Nonnull final BlockPos pos) {
        if(!entity.isInWater()) {
            @Nullable final AxisAlignedBB collision = blockaccess.getBlockState(pos.down()).getCollisionBoundingBox(blockaccess, pos.down());
            return collision == null ? pos.getY() : pos.getY() + collision.maxY;
        }

        else return pos.getY() + 0.5;
    }

    @Nullable
    PathPoint getSafePoint(final int x, final int y, final int z, final int heightToOffset, final double blockHeight) {
        @Nullable PathPoint safePoint = null;
        if(getBlockHeightAt(new BlockPos(x, y, z)) - blockHeight > 1.125) return null;

        @Nonnull PathNodeType pathType = getPathNodeType(blockaccess, x, y, z, entity, entitySizeX, entitySizeY, entitySizeZ, false, false);
        float pathPriority = entity.getPathPriority(pathType);

        final double entityRadius = entity.width / 2;
        if(pathPriority >= 0) {
            safePoint = openPoint(x, y, z);
            safePoint.nodeType = pathType;
            safePoint.costMalus = Math.max(safePoint.costMalus, pathPriority);
        }

        if(pathType != PathNodeType.WATER && pathType != PathNodeType.WALKABLE) {
            if(safePoint == null && heightToOffset > 0 && pathType != PathNodeType.FENCE && pathType != PathNodeType.TRAPDOOR) safePoint = getSafePoint(x, y + 1, z, heightToOffset - 1, blockHeight);
            if(pathType == PathNodeType.OPEN) {
                @Nonnull final AxisAlignedBB entityAABB = new AxisAlignedBB(x - entityRadius + 0.5, y + 0.001, z - entityRadius + 0.5, x + entityRadius + 0.5, y + entity.height, z + entityRadius + 0.5);
                if(!entity.world.collidesWithAnyBlock(entityAABB)) return null;

                @Nonnull final PathNodeType pathTypeBelow = getPathNodeType(blockaccess, x, y - 1, z, entity, entitySizeX, entitySizeY, entitySizeZ, false, false);
                if(pathTypeBelow == PathNodeType.BLOCKED) {
                    safePoint = openPoint(x, y, z);
                    safePoint.nodeType = PathNodeType.WALKABLE;
                    safePoint.costMalus = Math.max(safePoint.costMalus, pathPriority);
                    return safePoint;
                }

                if(pathTypeBelow == PathNodeType.WATER) {
                    safePoint = openPoint(x, y, z);
                    safePoint.nodeType = PathNodeType.WATER;
                    safePoint.costMalus = Math.max(safePoint.costMalus, pathPriority);
                    return safePoint;
                }

                int currFallHeight = 0;
                for(int i = y; i > 0 && pathType == PathNodeType.OPEN; i--) {
                    if(currFallHeight++ >= entity.getMaxFallHeight()) return null;

                    pathType = getPathNodeType(blockaccess, x, y, z, entity, entitySizeX, entitySizeY, entitySizeZ, false, false);
                    pathPriority = entity.getPathPriority(pathType);

                    if(pathType != PathNodeType.OPEN && pathPriority >= 0) {
                        safePoint = openPoint(x, y, z);
                        safePoint.nodeType = pathType;
                        safePoint.costMalus = Math.max(safePoint.costMalus, pathPriority);
                        break;
                    }

                    if(pathPriority < 0) return null;
                }
            }

        }

        else if(y < entity.world.getSeaLevel() - 10 && prefersShallowSwimming && safePoint != null) safePoint.costMalus++;
        return safePoint;
    }

    @Nonnull
    @Override
    public PathNodeType getPathNodeType(@Nonnull final IBlockAccess world, final int originX, final int originY, final int originZ, final int xSize, final int ySize, final int zSize, final boolean canOpenDoorsIn, final boolean canEnterDoorsIn, @Nonnull final EnumSet<PathNodeType> pathTypes, @Nonnull final PathNodeType startingType, @Nonnull final BlockPos pos) {
        final boolean nonRail = !(world.getBlockState(pos).getBlock() instanceof BlockRailBase) && !(world.getBlockState(pos.down()).getBlock() instanceof BlockRailBase);
        @Nonnull PathNodeType pathType = startingType;

        for(int offsetX = 0; offsetX < xSize; offsetX++) {
            for(int offsetY = 0; offsetY < ySize; offsetY++) {
                for(int offsetZ = 0; offsetZ < zSize; offsetZ++) {
                    final int x = offsetX + originX;
                    final int y = offsetY + originY;
                    final int z = offsetZ + originZ;

                    @Nonnull PathNodeType offsetPathType = getPathNodeType(world, x, y, z);
                    if(offsetPathType == PathNodeType.RAIL && nonRail) offsetPathType = PathNodeType.FENCE;
                    if(offsetPathType == PathNodeType.DOOR_OPEN || offsetPathType == PathNodeType.DOOR_WOOD_CLOSED || offsetPathType == PathNodeType.DOOR_IRON_CLOSED)
                        offsetPathType = PathNodeType.BLOCKED;

                    if(offsetX == 0 && offsetY == 0 && offsetZ == 0) pathType = offsetPathType;
                    pathTypes.add(offsetPathType);
                }
            }
        }

        return pathType;
    }

    @Nonnull
    @Override
    public PathNodeType getPathNodeType(@Nonnull final IBlockAccess world, final int x, final int y, final int z) {
        @Nonnull PathNodeType pathType = getPathNodeTypeRaw(world, x, y, z);

        if(pathType == PathNodeType.WATER) {
            for(EnumFacing enumfacing : EnumFacing.values()) {
                @Nonnull final PathNodeType offsetPathType = getPathNodeTypeRaw(world, x + enumfacing.getXOffset(), y + enumfacing.getYOffset(), z + enumfacing.getZOffset());
                if(offsetPathType == PathNodeType.BLOCKED) return WATER_BORDER;
            }

            return PathNodeType.WATER;
        }

        else if(pathType == PathNodeType.OPEN && y >= 1) {
            @Nonnull final PathNodeType pathTypeBelow = getPathNodeTypeRaw(world, x, y - 1, z);
            if(pathTypeBelow != PathNodeType.WALKABLE && pathTypeBelow != PathNodeType.OPEN && pathTypeBelow != PathNodeType.LAVA) pathType = PathNodeType.WALKABLE;
            if(pathTypeBelow == PathNodeType.DAMAGE_FIRE || world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.MAGMA) pathType = PathNodeType.DAMAGE_FIRE;
            if(pathTypeBelow == PathNodeType.DAMAGE_CACTUS) pathType = PathNodeType.DAMAGE_CACTUS;
        }

        return checkNeighborBlocks(world, x, y, z, pathType);
    }

    @Nonnull
    @Override
    protected PathNodeType getPathNodeTypeRaw(@Nonnull final IBlockAccess world, final int x, final int y, final int z) {
        @Nonnull final BlockPos pos = new BlockPos(x, y, z);
        if(world.isAirBlock(pos)) return PathNodeType.OPEN;

        @Nullable final PathNodeType type = super.getPathNodeTypeRaw(world, x, y, z);
        if(type != PathNodeType.OPEN && type != PathNodeType.BLOCKED) return type;

        @Nonnull final FluidState fluidState = FluidState.get(world, pos);
        if(fluidState.getMaterial() == Material.WATER) return PathNodeType.WATER;
        else if(fluidState.getMaterial() == Material.LAVA) return PathNodeType.LAVA;
        else return type;
    }
}
