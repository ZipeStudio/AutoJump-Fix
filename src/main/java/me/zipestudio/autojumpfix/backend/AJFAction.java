package me.zipestudio.autojumpfix.backend;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AJFAction {

    private static final double PREDICTION_MULT = 6;

    private AJFAction() {
    }

    public static boolean autojumpPlayer(LocalPlayer player, float dx, float dz) {

        if (!player.input.hasForwardImpulse()) return false;
        float jumpHeight = 1.2F;

        //?if >=1.21.5 {
        /*MobEffectInstance mobEffectInstance = player.getEffect(MobEffects.JUMP_BOOST);
        *///?} else {
        MobEffectInstance mobEffectInstance = player.getEffect(MobEffects.JUMP);
        //?}

        if (mobEffectInstance != null) {
            jumpHeight += (float) (mobEffectInstance.getAmplifier() + 1) * 0.75F;
        }

        Level world = getPlayerWorld(player);

        double bpt = Mth.clamp(Math.sqrt((dx * dx) + (dz * dz)), 0.001, 0.8); // Current speed in blocks per tick; Clamped to reasonable values for aproximating next location
        if (bpt < 0.2) bpt *= 0.7; // Fixes ice + iron bar edge case
        AABB currentAABB = player.getBoundingBox();
        float yawRad = -player.getViewYRot(0) * (float) (Math.PI / 180);
        double yawDeltaX = Mth.sin(yawRad);
        double yawDeltaZ = Mth.cos(yawRad);
        double predictionX = yawDeltaX * bpt * PREDICTION_MULT;
        double predictionZ = yawDeltaZ * bpt * PREDICTION_MULT;
        AABB predictionAABB = currentAABB.move(predictionX, 0, predictionZ);
        int minX = Mth.floor(predictionAABB.minX);
        int minY = Mth.floor(predictionAABB.minY);
        int minZ = Mth.floor(predictionAABB.minZ);
        int maxX = Mth.floor(predictionAABB.maxX);
        int maxY = Mth.floor(predictionAABB.maxY);
        int maxZ = Mth.floor(predictionAABB.maxZ);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                for (int k = minZ; k <= maxZ; k++) {
                    pos.set(i, j, k);
                    VoxelShape jumpTargetShape = world.getBlockState(pos).getCollisionShape(world, pos).move(i, j, k);
                    if (jumpTargetShape.isEmpty()) continue;
                    double playerAngle = mcDeg2NormalDeg((yawRad * (-180 / Math.PI)));
                    double ydiff = getCollisionY(angleToDirection(playerAngle).getOpposite(), jumpTargetShape) - player.getY();

                    float stepHeight =
                    //? if >=1.19.4 {
                    /*player.maxUpStep();
                    *///?} else {
                    player.maxUpStep;
                    //?}

                    if (ydiff > stepHeight + 0.001 && ydiff < jumpHeight) {
                        double playerToBlockAngle = calcAngle(player.getX(), player.getZ(), i + 0.5, k + 0.5);
                        if (!hasHeadSpace(player, currentAABB, jumpHeight, pos)) continue;
                        if (Math.abs(angleDiff(playerToBlockAngle, playerAngle)) < 10 || Math.floorMod((int) playerAngle, 90) < 10 || Math.floorMod((int) playerAngle, 90) > 80) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasHeadSpace(LocalPlayer player, AABB playerAABB, float jumpHeight, BlockPos target) {
        int minX = Mth.floor(Math.min(playerAABB.minX, target.getX()));
        int minY = Mth.floor(player.getY() + jumpHeight);
        int minZ = Mth.floor(Math.min(playerAABB.minZ, target.getZ()));
        int maxX = Mth.floor(Math.max(playerAABB.maxX, target.getX()));

        int maxY = Mth.floor(player.getY() + playerAABB.getYsize() + jumpHeight);
        int maxZ = Mth.floor(Math.max(playerAABB.maxZ, target.getZ()));

        Level world = getPlayerWorld(player);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                for (int k = minZ; k <= maxZ; k++) {
                    pos.set(i, j, k);
                    VoxelShape blockingShape = world.getBlockState(pos).getCollisionShape(world, pos).move(i, j, k);
                    if (blockingShape.min(Direction.Axis.Y) - player.getY() < jumpHeight + 1.7) return false;
                }
            }
        }

        return true;
    }

    public static double getCollisionY(Direction side, VoxelShape shape) {
        boolean positiveDirection = side.getStepX() + side.getStepZ() > 0;
        double maxDir = Double.NaN;
        double maxY = Double.NaN;
        for (AABB aabb : shape.toAabbs()) {
            if (aabb.maxY > maxY || Double.isNaN(maxDir)) {
                if (positiveDirection) {
                    if (Double.isNaN(maxDir) || aabb.max(side.getAxis()) >= maxDir) {
                        maxDir = aabb.max(side.getAxis());
                        maxY = aabb.maxY;
                    }
                } else {
                    if (Double.isNaN(maxDir) || aabb.min(side.getAxis()) <= maxDir) {
                        maxDir = aabb.min(side.getAxis());
                        maxY = aabb.maxY;
                    }
                }
            }
        }
        return maxY;
    }

    public static Level getPlayerWorld(LocalPlayer player) {

        //? if >=1.20 {
        /*return player.level();
        *///?} else {
        return player.level;
        //?}
    }

    public static Direction angleToDirection(double deg) {
        if (deg > 0 && deg < 45) {
            return Direction.NORTH;
        } else if (deg >= 45 && deg < 135) {
            return Direction.EAST;
        } else if (deg >= 135 && deg < 225) {
            return Direction.SOUTH;
        } else if (deg >= 225 && deg < 315) {
            return Direction.WEST;
        } else {
            return Direction.NORTH;
        }
    }

    public static double mcDeg2NormalDeg(double a) {
        a += 180;
        while (a < 0) a += 360;
        while (a > 360) a -= 360;
        return a;
    }

    public static double calcAngle(double x, double y, double x1, double y1) {
        return Mth.atan2(x - x1, y1 - y) * 180 / Math.PI + 180;
    }

    /**
     * Gets the diffrence between 2 degree angles
     *
     * @param a 0 <= a <= 360
     * @param b 0 <= b <= 360
     * @return diffrence
     */
    public static double angleDiff(double a, double b) {
        double difference = a - b;
        while (difference < -180) difference += 360;
        while (difference > 180) difference -= 360;
        return difference;
    }
}