package com.pedropathing.ftc.localization.constants;

import com.pedropathing.ftc.localization.SCEncoder;
import org.wpilib.hardware.imu.OnboardIMU;

/**
 * This is the TwoWheelConstants class. It holds many constants and parameters for the Two Wheel Localizer.
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 12/24/2024
 */

public class TwoWheelConstants {

    /** The number of inches per tick of the encoder for forward movement
     * Default Value: .001989436789 */
    public double forwardTicksToInches = .001989436789;

    /** The number of inches per tick of the encoder for lateral movement (strafing)
     * Default Value: .001989436789 */
    public double strafeTicksToInches = .001989436789;

    /** The y offset of the forward encoder (Deadwheel) from the center of the robot
     * Default Value: 1 */
    public double forwardPodY = 1;

    /** The x offset of the strafe encoder (Deadwheel) from the center of the robot
     * Default Value: -2.5 */
    public double strafePodX = -2.5;

    /** The Orientation of the IMU on the robot
     * Default Value: FLAT
     */
    public OnboardIMU.MountOrientation IMU_Orientation = OnboardIMU.MountOrientation.FLAT;

    /** The direction of the forward encoder
     * Default Value: Encoder.REVERSE */
    public double forwardEncoderDirection = SCEncoder.REVERSE;

    /** The direction of the strafe encoder
     * Default Value: Encoder.FORWARD */
    public double strafeEncoderDirection = SCEncoder.FORWARD;

    /**
     * This creates a new TwoWheelConstants with default values.
     */
    public TwoWheelConstants() {
        defaults();
    }

    public TwoWheelConstants forwardTicksToInches(double forwardTicksToInches) {
        this.forwardTicksToInches = forwardTicksToInches;
        return this;
    }

    public TwoWheelConstants strafeTicksToInches(double strafeTicksToInches) {
        this.strafeTicksToInches = strafeTicksToInches;
        return this;
    }

    public TwoWheelConstants forwardPodY(double forwardPodY) {
        this.forwardPodY = forwardPodY;
        return this;
    }

    public TwoWheelConstants strafePodX(double strafePodX) {
        this.strafePodX = strafePodX;
        return this;
    }

    public TwoWheelConstants IMU_Orientation(OnboardIMU.MountOrientation IMU_Orientation) {
        this.IMU_Orientation = IMU_Orientation;
        return this;
    }

    public TwoWheelConstants forwardEncoderDirection(double forwardEncoderDirection) {
        this.forwardEncoderDirection = forwardEncoderDirection;
        return this;
    }

    public TwoWheelConstants strafeEncoderDirection(double strafeEncoderDirection) {
        this.strafeEncoderDirection = strafeEncoderDirection;
        return this;
    }

    /**
     * This sets the default values for the this.
     */
    public void defaults() {
        forwardTicksToInches = .001989436789;
        strafeTicksToInches = .001989436789;
        forwardPodY = 1;
        strafePodX = -2.5;
        IMU_Orientation = OnboardIMU.MountOrientation.FLAT;
        forwardEncoderDirection = SCEncoder.REVERSE;
        strafeEncoderDirection = SCEncoder.FORWARD;
    }
}
