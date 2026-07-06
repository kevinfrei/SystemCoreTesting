package com.pedropathing.ftc;

import com.pedropathing.geometry.CoordinateSystem;
import com.pedropathing.geometry.Pose;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.units.Units;

public class PoseConverter {

    /**
     * Converts a Pose to a Pose2D in the desired coordinate system.
     *
     * @param pose the Pose object
     * @param desiredCoordinateSystem the desired coordinate system
     * @return a Pose2D object with the x and y coordinates in inches and the heading in radians
     */
    public static Pose2d poseToPose2D(Pose pose, CoordinateSystem desiredCoordinateSystem) {
        return new Pose2d(
            pose.getAsCoordinateSystem(desiredCoordinateSystem).getX(),
            pose.getAsCoordinateSystem(desiredCoordinateSystem).getY(),
            Rotation2d.fromRadians(pose.getAsCoordinateSystem(desiredCoordinateSystem).getHeading())
        );
    }

    /**
     * Returns a pose from a Pose2D and a coordinate system.
     *
     * @param pose2d the Pose2D object
     * @param coordinateSystem the coordinate system
     * @return a Pose object with the x and y coordinates in inches and the heading in radians
     */
    public static Pose pose2DToPose(Pose2d pose2d, CoordinateSystem coordinateSystem) {
        return new Pose(
            pose2d.getMeasureX().in(Units.Inches),
            pose2d.getMeasureY().in(Units.Inches),
            pose2d.getRotation().getRadians(),
            coordinateSystem
        );
    }
}
