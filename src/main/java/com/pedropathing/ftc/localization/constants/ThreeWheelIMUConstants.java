package com.pedropathing.ftc.localization.constants;

import org.wpilib.hardware.imu.OnboardIMU;

import com.pedropathing.ftc.localization.CustomIMU;
import com.pedropathing.ftc.localization.SystemCoreEncoder;
import com.pedropathing.ftc.localization.SystemCoreIMU;

/**
 * This is the ThreeWheelIMUConstants class. It holds many constants and parameters for the Three Wheel + IMU Localizer.
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 12/24/2024
 */

public class ThreeWheelIMUConstants {

    /** The number of inches per tick of the encoder for forward movement
     * Default Value: .001989436789 */
    public double forwardTicksToInches = .001989436789;

    /** The number of inches per tick of the encoder for lateral movement (strafing)
     * Default Value: .001989436789 */
    public double strafeTicksToInches = .001989436789;

    /** The number of inches per tick of the encoder for turning
     * Default Value: .001989436789 */
    public double turnTicksToInches = .001989436789;

    /** The Y Offset of the Left Encoder (Deadwheel) from the center of the robot
     * Default Value: 1 */
    public double leftPodY = 1;

    /** The Y Offset of the Right Encoder (Deadwheel) from the center of the robot
     * Default Value: -1 */
    public double rightPodY = -1;

    /** The X Offset of the Strafe Encoder (Deadwheel) from the center of the robot
     * Default Value: -2.5 */
    public double strafePodX = -2.5;

    /** The Orientation of the Control Hub (for IMU) on the Robot
     * TODO:KBF this doesn't support 'reversed' mounting, which is an issue for FTC
     * cuz students regularly mount stuff in weird orientations because 18"^3 is a
     * fun design challenge :)
     *  */
    public OnboardIMU.MountOrientation IMU_Orientation =  OnboardIMU.MountOrientation.FLAT;

    /** The direction of the Left Encoder
     * Default Value: Encoder.REVERSE */
    public double leftEncoderDirection = SystemCoreEncoder.REVERSE;

    /** The direction of the Right Encoder
     * Default Value: Encoder.FORWARD */
    public double rightEncoderDirection = SystemCoreEncoder.REVERSE;

    /** The direction of the Strafe Encoder
     * Default Value: Encoder.FORWARD */
    public double strafeEncoderDirection = SystemCoreEncoder.FORWARD;

    /**
     * This is the IMU that will be used for localization.
     */
    public CustomIMU imu = new SystemCoreIMU();

    /**
     * This creates a new ThreeWheelIMUConstants with default values.
     */
    public ThreeWheelIMUConstants() {
        defaults();
    }

    public ThreeWheelIMUConstants forwardTicksToInches(double forwardTicksToInches) {
        this.forwardTicksToInches = forwardTicksToInches;
        return this;
    }

    public ThreeWheelIMUConstants strafeTicksToInches(double strafeTicksToInches) {
        this.strafeTicksToInches = strafeTicksToInches;
        return this;
    }

    public ThreeWheelIMUConstants turnTicksToInches(double turnTicksToInches) {
        this.turnTicksToInches = turnTicksToInches;
        return this;
    }

    public ThreeWheelIMUConstants leftPodY(double leftPodY) {
        this.leftPodY = leftPodY;
        return this;
    }

    public ThreeWheelIMUConstants rightPodY(double rightPodY) {
        this.rightPodY = rightPodY;
        return this;
    }

    public ThreeWheelIMUConstants strafePodX(double strafePodX) {
        this.strafePodX = strafePodX;
        return this;
    }

    public ThreeWheelIMUConstants IMU_Orientation(OnboardIMU.MountOrientation IMU_Orientation) {
        this.IMU_Orientation = IMU_Orientation;
        return this;
    }

    public ThreeWheelIMUConstants leftEncoderDirection(double leftEncoderDirection) {
        this.leftEncoderDirection = leftEncoderDirection;
        return this;
    }

    public ThreeWheelIMUConstants rightEncoderDirection(double rightEncoderDirection) {
        this.rightEncoderDirection = rightEncoderDirection;
        return this;
    }

    public ThreeWheelIMUConstants strafeEncoderDirection(double strafeEncoderDirection) {
        this.strafeEncoderDirection = strafeEncoderDirection;
        return this;
    }

    public ThreeWheelIMUConstants customIMU(CustomIMU customIMU) {
        this.imu = customIMU;
        return this;
    }

    public void defaults() {
        forwardTicksToInches = .001989436789;
        strafeTicksToInches = .001989436789;
        turnTicksToInches = .001989436789;
        leftPodY = 1;
        rightPodY = -1;
        strafePodX = -2.5;
        IMU_Orientation = OnboardIMU.MountOrientation.FLAT;
        leftEncoderDirection = SystemCoreEncoder.REVERSE;
        rightEncoderDirection = SystemCoreEncoder.REVERSE;
        strafeEncoderDirection = SystemCoreEncoder.FORWARD;
        // TODO:KBF Need to factor this out somehow
        imu = new SystemCoreIMU();
    }
}
