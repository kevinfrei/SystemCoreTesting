package com.pedropathing.ftc.localization;

/**
 * This is the Encoder class. This tracks the position of a motor of class DcMotorEx. The motor
 * must have an encoder attached. It can also get changes in position.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 4/2/2024
 */
public class SystemCoreEncoder implements Encoder {
    private final org.wpilib.hardware.rotation.Encoder hw_encoder;
    private double previousPosition;
    private double currentPosition;
    private double multiplier;

    public final static double FORWARD = 1, REVERSE = -1;

    /**
     * This creates a new Encoder from a DcMotorEx.
     *
     * @param encoder the encoder this will be tracking
     */
    public SystemCoreEncoder(org.wpilib.hardware.rotation.Encoder encoder) {
        hw_encoder = encoder;
        multiplier = FORWARD;
        reset();
    }

    /**
     * This sets the direction/multiplier of the Encoder. Setting 1 or -1 will make the Encoder track
     * forward or in reverse, respectively. Any multiple of either one will scale the Encoder's output
     * by that amount.
     *
     * @param setMultiplier the multiplier/direction to set
     */
    public void setMultiplier(double setMultiplier) {
        multiplier = setMultiplier;
    }

    /**
     * This resets the Encoder's position and the current and previous position in the code.
     */
    public void reset() {
        double pos = hw_encoder.getDistance();
        previousPosition = pos;
        currentPosition = pos;
    }

    /**
     * This updates the Encoder's tracked current position and previous position.
     */
    public void update() {
        previousPosition = currentPosition;
        currentPosition = hw_encoder.getDistance();
    }

    /**
     * This returns the multiplier/direction of the Encoder.
     *
     * @return returns the multiplier
     */
    public double getMultiplier() {
        return multiplier * (hw_encoder.getDirection() ? 1 : -1);
    }

    /**
     * This returns the change in position from the previous position to the current position. One
     * important thing to note is that this encoder does not track velocity, only change in position.
     * This is because I am using a pose exponential method of localization, which doesn't need the
     * velocity of the encoders. Velocity of the robot is calculated in the localizer using an elapsed
     * time timer there.
     *
     * @return returns the change in position of the Encoder
     */
    public double getDeltaPosition() {
        return getMultiplier() * (currentPosition - previousPosition);
    }
}
