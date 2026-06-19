package com.pedropathing.ftc.localization.constants;


import com.pedropathing.ftc.localization.SystemCoreEncoder;

/**
 * This is the ThreeWheelConstants class. It holds many constants and parameters for the Three Wheel Localizer.
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 12/24/2024
 */


public class ThreeWheelConstants {

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

    /** The direction of the Left Encoder
     * Default Value: Encoder.REVERSE */
    public double leftEncoderDirection = SystemCoreEncoder.REVERSE;

    /** The direction of the Right Encoder
     * Default Value: Encoder.REVERSE */
    public double rightEncoderDirection = SystemCoreEncoder.REVERSE;

    /** The direction of the Strafe Encoder
     * Default Value: Encoder.FORWARD */
    public double strafeEncoderDirection = SystemCoreEncoder.FORWARD;

    /**
     * This creates a new ThreeWheelConstants with default values.
     */
    public ThreeWheelConstants() {
        defaults();
    }

    public ThreeWheelConstants forwardTicksToInches(double forwardTicksToInches) {
        this.forwardTicksToInches = forwardTicksToInches;
        return this;
    }

    public ThreeWheelConstants strafeTicksToInches(double strafeTicksToInches) {
        this.strafeTicksToInches = strafeTicksToInches;
        return this;
    }

    public ThreeWheelConstants turnTicksToInches(double turnTicksToInches) {
        this.turnTicksToInches = turnTicksToInches;
        return this;
    }

    public ThreeWheelConstants leftPodY(double leftPodY) {
        this.leftPodY = leftPodY;
        return this;
    }

    public ThreeWheelConstants rightPodY(double rightPodY) {
        this.rightPodY = rightPodY;
        return this;
    }

    public ThreeWheelConstants strafePodX(double strafePodX) {
        this.strafePodX = strafePodX;
        return this;
    }

    public ThreeWheelConstants leftEncoderDirection(double leftEncoderDirection) {
        this.leftEncoderDirection = leftEncoderDirection;
        return this;
    }

    public ThreeWheelConstants rightEncoderDirection(double rightEncoderDirection) {
        this.rightEncoderDirection = rightEncoderDirection;
        return this;
    }

    public ThreeWheelConstants strafeEncoderDirection(double strafeEncoderDirection) {
        this.strafeEncoderDirection = strafeEncoderDirection;
        return this;
    }

    public void defaults() {
        forwardTicksToInches = .001989436789;
        strafeTicksToInches = .001989436789;
        turnTicksToInches = .001989436789;
        leftPodY = 1;
        rightPodY = -1;
        strafePodX = -2.5;
        leftEncoderDirection = SystemCoreEncoder.REVERSE;
        rightEncoderDirection = SystemCoreEncoder.REVERSE;
        strafeEncoderDirection = SystemCoreEncoder.FORWARD;
    }
}
