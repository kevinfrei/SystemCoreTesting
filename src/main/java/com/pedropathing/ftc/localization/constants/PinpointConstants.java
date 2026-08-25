package com.pedropathing.ftc.localization.constants;

import first.support.GoBildaPinpoint;
import java.util.OptionalDouble;
import org.wpilib.units.DistanceUnit;
import org.wpilib.units.Units;

/**
 * This is the PinpointConstants class. It holds many constants and parameters for the Pinpoint Localizer.
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 12/24/2024
 */
public class PinpointConstants {

    /** The Y Offset of the Forward Encoder (Deadwheel) from the center of the robot in DistanceUnit
     * @see #distanceUnit
     * Default Value: 1 */
    public double forwardPodY = 1;

    /** The X Offset of the Strafe Encoder (Deadwheel) from the center of the robot in DistanceUnit
     * @see #distanceUnit
     * Default Value: -2.5 */
    public double strafePodX = -2.5;

    /** The Unit of Distance that the Pinpoint uses to measure distance
     * Default Value: DistanceUnit.INCH */
    public DistanceUnit distanceUnit = Units.Inch;

    /** Custom Yaw Scalar for the Pinpoint (overrides the calibration of the Pinpoint) */
    public OptionalDouble yawScalar = OptionalDouble.empty();

    /** The Encoder Resolution for the Pinpoint. Used by default, but can be changed to a custom resolution.
     * Default Value: GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD */
    public GoBildaPinpoint.GoBildaOdometryPods encoderResolution =
        GoBildaPinpoint.GoBildaOdometryPods.goBILDA_4_BAR_POD;

    /** The Encoder Resolution for the Pinpoint. Unused by default, but can be used if you want to use a custom encoder resolution. */
    public OptionalDouble customEncoderResolution = OptionalDouble.empty();

    /** The Encoder Direction for the Forward Encoder (Deadwheel)
     * Default Value: GoBildaPinpointDriver.EncoderDirection.REVERSED */
    public GoBildaPinpoint.EncoderDirection forwardEncoderDirection =
        GoBildaPinpoint.EncoderDirection.REVERSED;

    /** The Encoder Direction for the Strafe Encoder (Deadwheel)
     * Default Value: GoBildaPinpointDriver.EncoderDirection.FORWARD */
    public GoBildaPinpoint.EncoderDirection strafeEncoderDirection =
        GoBildaPinpoint.EncoderDirection.FORWARD;

    /**
     * This creates a new PinpointConstants with default values.
     */
    public PinpointConstants() {
        defaults();
    }

    public PinpointConstants forwardPodY(double forwardPodY) {
        this.forwardPodY = forwardPodY;
        return this;
    }

    public PinpointConstants strafePodX(double strafePodX) {
        this.strafePodX = strafePodX;
        return this;
    }

    public PinpointConstants distanceUnit(DistanceUnit distanceUnit) {
        this.distanceUnit = distanceUnit;
        return this;
    }

    public PinpointConstants yawScalar(double yawScalar) {
        this.yawScalar = OptionalDouble.of(yawScalar);
        return this;
    }

    public PinpointConstants encoderResolution(
        GoBildaPinpoint.GoBildaOdometryPods encoderResolution
    ) {
        this.encoderResolution = encoderResolution;
        return this;
    }

    public PinpointConstants customEncoderResolution(double customEncoderResolution) {
        this.customEncoderResolution = OptionalDouble.of(customEncoderResolution);
        return this;
    }

    public PinpointConstants forwardEncoderDirection(
        GoBildaPinpoint.EncoderDirection forwardEncoderDirection
    ) {
        this.forwardEncoderDirection = forwardEncoderDirection;
        return this;
    }

    public PinpointConstants strafeEncoderDirection(
        GoBildaPinpoint.EncoderDirection strafeEncoderDirection
    ) {
        this.strafeEncoderDirection = strafeEncoderDirection;
        return this;
    }

    public void defaults() {
        forwardPodY = 1;
        strafePodX = -2.5;
        distanceUnit = Units.Inch;
        yawScalar = OptionalDouble.empty();
        encoderResolution = GoBildaPinpoint.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        customEncoderResolution = OptionalDouble.empty();
        forwardEncoderDirection = GoBildaPinpoint.EncoderDirection.REVERSED;
        strafeEncoderDirection = GoBildaPinpoint.EncoderDirection.FORWARD;
    }
}
