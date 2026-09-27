package me.zipestudio.autojumpfix.backend;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
//? if >=1.20.5 {
import net.minecraft.world.entity.ai.attributes.Attributes;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AJFAction {

    private static final double PREDICTION_MULT = 6;

    private static final double JUMP_POWER = 0.42;
    private static final double JUMP_BOOST_POWER_PER_LEVEL = 0.1;
    private static final double GRAVITY = 0.08;
    private static final double DRAG = 0.98;
    private static final double JUMP_HEIGHT_MARGIN = 0.05;
    private static final int MAX_JUMP_TICKS = 100;

    private AJFAction() {
    }

    public static boolean autojumpPlayer(LocalPlayer player, float dx, float dz, float blockJumpFactor) {

        if (!player.input.hasForwardImpulse()) return false;
        float jumpHeight = maxJumpHeight(player, blockJumpFactor);

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
        int minX = Mth.floor(Math.min(currentAABB.minX, predictionAABB.minX));
        int minY = Mth.floor(Math.min(currentAABB.minY, predictionAABB.minY));
        int minZ = Mth.floor(Math.min(currentAABB.minZ, predictionAABB.minZ));
        int maxX = Mth.floor(Math.max(currentAABB.maxX, predictionAABB.maxX));
        int maxY = Mth.floor(Math.max(currentAABB.maxY, predictionAABB.maxY));
        int maxZ = Mth.floor(Math.max(currentAABB.maxZ, predictionAABB.maxZ));

        double playerAngle = mcDeg2NormalDeg((yawRad * (-180 / Math.PI)));
        boolean axisAligned = Math.floorMod((int) playerAngle, 90) < 10 || Math.floorMod((int) playerAngle, 90) > 80;

        float stepHeight =
        //? if >=1.19.4 {
        player.maxUpStep();
        //?} else {
        /*player.maxUpStep;
        *///?}

        List<int[]> columns = new ArrayList<>();
        for (int i = minX; i <= maxX; i++) {
            for (int k = minZ; k <= maxZ; k++) {
                double playerToBlockAngle = calcAngle(player.getX(), player.getZ(), i + 0.5, k + 0.5);
                if (!axisAligned && Math.abs(angleDiff(playerToBlockAngle, playerAngle)) >= 10) continue;
                columns.add(new int[]{i, k});
            }
        }
        columns.sort(Comparator.comparingDouble(c -> squaredHorizontalDistance(player, c[0], c[1])));

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        double climbedY = player.getY();

        for (int[] column : columns) {
            int i = column[0];
            int k = column[1];

            List<Double> surfaces = new ArrayList<>();
            for (int j = minY; j <= maxY; j++) {
                pos.set(i, j, k);
                VoxelShape shape = world.getBlockState(pos).getCollisionShape(world, pos).move(i, j, k);
                if (shape.isEmpty()) continue;
                for (AABB aabb : shape.toAabbs()) surfaces.add(aabb.maxY);
            }
            if (surfaces.isEmpty()) continue;
            surfaces.sort(Double::compare);

            pos.set(i, minY, k);
            for (double surfaceY : surfaces) {
                double ydiff = surfaceY - climbedY;
                if (ydiff <= 0.001) continue;
                if (ydiff >= jumpHeight) break;
                if (!hasHeadSpace(player, currentAABB, surfaceY, pos)) continue;
                if (ydiff <= stepHeight + 0.001) {
                    climbedY = surfaceY;
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public static double squaredHorizontalDistance(LocalPlayer player, int x, int z) {
        double dx = (x + 0.5) - player.getX();
        double dz = (z + 0.5) - player.getZ();
        return (dx * dx) + (dz * dz);
    }

    public static float maxJumpHeight(LocalPlayer player, float blockJumpFactor) {

        //?if >=1.21.5 {
        MobEffectInstance mobEffectInstance = player.getEffect(MobEffects.JUMP_BOOST);
        //?} else {
        /*MobEffectInstance mobEffectInstance = player.getEffect(MobEffects.JUMP);
        *///?}

        //? if >=1.20.5 {
        double velocity = player.getAttributeValue(Attributes.JUMP_STRENGTH) * blockJumpFactor;
        double gravity = player.getAttributeValue(Attributes.GRAVITY);
        //?} else {
        /*double velocity = JUMP_POWER * blockJumpFactor;
        double gravity = GRAVITY;
        *///?}

        if (mobEffectInstance != null) {
            velocity += (mobEffectInstance.getAmplifier() + 1) * JUMP_BOOST_POWER_PER_LEVEL;
        }

        double height = 0;
        for (int tick = 0; velocity > 0 && tick < MAX_JUMP_TICKS; tick++) {
            height += velocity;
            velocity = (velocity - gravity) * DRAG;
        }

        return (float) (height - JUMP_HEIGHT_MARGIN);
    }

    public static boolean hasHeadSpace(LocalPlayer player, AABB playerAABB, double landingY, BlockPos target) {
        double headroomY = landingY + playerAABB.getYsize();

        int minX = Mth.floor(Math.min(playerAABB.minX, target.getX()));
        int minY = Mth.floor(playerAABB.maxY);
        int minZ = Mth.floor(Math.min(playerAABB.minZ, target.getZ()));
        int maxX = Mth.floor(Math.max(playerAABB.maxX, target.getX()));

        int maxY = Mth.floor(headroomY);
        int maxZ = Mth.floor(Math.max(playerAABB.maxZ, target.getZ()));

        Level world = getPlayerWorld(player);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                for (int k = minZ; k <= maxZ; k++) {
                    pos.set(i, j, k);
                    VoxelShape blockingShape = world.getBlockState(pos).getCollisionShape(world, pos).move(i, j, k);
                    if (blockingShape.isEmpty()) continue;
                    if (blockingShape.max(Direction.Axis.Y) > landingY && blockingShape.min(Direction.Axis.Y) < headroomY) return false;
                }
            }
        }

        return true;
    }

    public static Level getPlayerWorld(LocalPlayer player) {

        //? if >=1.20 {
        return player.level();
        //?} else {
        /*return player.level;
        *///?}
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