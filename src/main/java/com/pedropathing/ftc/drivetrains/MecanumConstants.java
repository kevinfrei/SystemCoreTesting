package com.pedropathing.ftc.drivetrains;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;

public class MecanumConstants {
    /** The Forward Velocity of the Robot - Different for each robot
     *  Default Value: 81.34056 */
    public  double xVelocity = 81.34056;

    /** The Lateral Velocity of the Robot - Different for each robot
     *  Default Value: 65.43028 */
    public  double yVelocity = 65.43028;

    private  double[] convertToPolar = Pose.cartesianToPolar(xVelocity, -yVelocity);

    /** The actual drive vector for the front left wheel, if the robot is facing a heading of 0 radians with the wheel centered at (0,0)
     *  Default Value: new Vector(convertToPolar[0], convertToPolar[1])
     * @implNote This vector should not be changed, but only accessed.
     */
    public  Vector frontLeftVector = new Vector(convertToPolar[0], convertToPolar[1]).normalize();
    public  double maxPower = 1;
    public  boolean leftFrontMotorInverted = true;
    public  boolean leftRearMotorInverted = true;
    public  boolean rightFrontMotorInverted = false;
    public  boolean rightRearMotorInverted = false;
    public  double motorCachingThreshold = 0.01;
    public  boolean useBrakeModeInTeleOp = false;
    public  boolean useVoltageCompensation = false;
    public  double nominalVoltage = 12.0;
    public  double staticFrictionCoefficient = 0.1;

    public MecanumConstants() {
        defaults();
    }

    public MecanumConstants xVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
        return this;
    }

    public MecanumConstants yVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
        return this;
    }

    public MecanumConstants maxPower(double maxPower) {
        this.maxPower = maxPower;
        return this;
    }

    public MecanumConstants leftFrontMotorInverted(boolean leftFrontMotorInverted) {
        this.leftFrontMotorInverted = leftFrontMotorInverted;
        return this;
    }

    public MecanumConstants leftRearMotorInverted(boolean leftRearMotorInverted) {
        this.leftRearMotorInverted = leftRearMotorInverted;
        return this;
    }

    public MecanumConstants rightFrontMotorInverted(boolean rightFrontMotorInverted) {
        this.rightFrontMotorInverted = rightFrontMotorInverted;
        return this;
    }

    public MecanumConstants rightRearMotorDirection(boolean rightRearMotorDirection) {
        this.rightRearMotorInverted = rightRearMotorDirection;
        return this;
    }

    public MecanumConstants motorCachingThreshold(double motorCachingThreshold) {
        this.motorCachingThreshold = motorCachingThreshold;
        return this;
    }

    public MecanumConstants useBrakeModeInTeleOp(boolean useBrakeModeInTeleOp) {
        this.useBrakeModeInTeleOp = useBrakeModeInTeleOp;
        return this;
    }

    public MecanumConstants useVoltageCompensation(boolean useVoltageCompensation) {
        this.useVoltageCompensation = useVoltageCompensation;
        return this;
    }

    public MecanumConstants nominalVoltage(double nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
        return this;
    }

    public MecanumConstants staticFrictionCoefficient(double staticFrictionCoefficient) {
        this.staticFrictionCoefficient = staticFrictionCoefficient;
        return this;
    }

    public double getXVelocity() {
        return xVelocity;
    }

    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public Vector getFrontLeftVector() {
        return frontLeftVector;
    }

    public void setFrontLeftVector(Vector frontLeftVector) {
        this.frontLeftVector = frontLeftVector;
    }

    public double getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    public boolean getLeftFrontMotorInverted() {
        return leftFrontMotorInverted;
    }

    public void setLeftFrontMotorInverted(boolean leftFrontMotorInverted) {
        this.leftFrontMotorInverted = leftFrontMotorInverted;
    }

    public boolean  getLeftRearMotorInverted() {
        return leftRearMotorInverted;
    }

    public void setLeftRearMotorInverted(boolean leftRearMotorInverted) {
        this.leftRearMotorInverted = leftRearMotorInverted;
    }

    public boolean getRightFrontMotorInverted() {
        return rightFrontMotorInverted;
    }

    public void setRightFrontMotorInverted(boolean rightFrontMotorInverted) {
        this.rightFrontMotorInverted = rightFrontMotorInverted;
    }

    public boolean getRightRearMotorInverted() {
        return rightRearMotorInverted;
    }

    public void setRightRearMotorInverted(boolean rightRearMotorInverted) {
        this.rightRearMotorInverted = rightRearMotorInverted;
    }

    public double getMotorCachingThreshold() {
        return motorCachingThreshold;
    }

    public void setMotorCachingThreshold(double motorCachingThreshold) {
        this.motorCachingThreshold = motorCachingThreshold;
    }

    public boolean isUseBrakeModeInTeleOp() {
        return useBrakeModeInTeleOp;
    }

    public void setUseBrakeModeInTeleOp(boolean useBrakeModeInTeleOp) {
        this.useBrakeModeInTeleOp = useBrakeModeInTeleOp;
    }

    /**
     * This method sets the default values for the MecanumConstants class.
     * It is called in the constructor of the MecanumConstants class.
     */
    public void defaults() {
        xVelocity = 81.34056;
        yVelocity = 65.43028;
        convertToPolar = Pose.cartesianToPolar(xVelocity, -yVelocity);
        frontLeftVector = new Vector(convertToPolar[0], convertToPolar[1]).normalize();
        maxPower = 1;
        leftFrontMotorInverted = true;
        leftRearMotorInverted = true;
        rightFrontMotorInverted = false;
        rightRearMotorInverted = false;
        motorCachingThreshold = 0.01;
        useBrakeModeInTeleOp = false;
        useVoltageCompensation = false;
        nominalVoltage = 12.0;
        staticFrictionCoefficient = 0.1;
    }
}
