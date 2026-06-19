package com.pedropathing.ftc.localization.localizers;

import com.pedropathing.ftc.localization.A301Encoder;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.HubEncoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;

import com.pedropathing.ftc.localization.SystemCoreEncoder;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Matrix;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.util.NanoTimer;
import com.revrobotics.spark.A301;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;

/**
 * This is the DriveEncoderLocalizer class. This class extends the Localizer superclass and is a
 * localizer that uses the drive encoder set up.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 4/2/2024
 */

public class DriveEncoderLocalizer implements Localizer {
    private Pose startPose;
    private Pose displacementPose;
    private Pose currentVelocity;
    private Matrix prevRotationMatrix;
    private final NanoTimer timer;
    private long deltaTimeNano;
    private final Encoder leftFront;
    private final Encoder rightFront;
    private final Encoder leftRear;
    private final Encoder rightRear;
    private double totalHeading;
    public static double FORWARD_TICKS_TO_INCHES;
    public static double STRAFE_TICKS_TO_INCHES;
    public static double TURN_TICKS_TO_RADIANS;
    public static double ROBOT_WIDTH;
    public static double ROBOT_LENGTH;

    /**
     * This creates a new DriveEncoderLocalizer from a HardwareMap, with a starting Pose at (0,0)
     * facing 0 heading.
     *
     * @param lf the left front motor
     * @param lr the left rear motor
     * @param rf the right front motor
     * @param rr the right rear motor
     * @param constants The DriveEncoderConstants for your drive base
     */
    public DriveEncoderLocalizer(A301 lf, A301 lr, A301 rr, A301 rf, DriveEncoderConstants constants) {
        this(lf, lr, rr, rf, constants, new Pose());
    }
    public DriveEncoderLocalizer(A301 lf, A301 lr, A301 rr, A301 rf, DriveEncoderConstants constants, Pose startPose) {
        this(new A301Encoder(lf), new A301Encoder(lr), new A301Encoder(rr), new A301Encoder(rf), constants, startPose);
    }
    public DriveEncoderLocalizer(ExpansionHubMotor lf, ExpansionHubMotor lr, ExpansionHubMotor rr, ExpansionHubMotor rf, DriveEncoderConstants constants) {
        this(lf, lr, rr, rf, constants, new Pose());
    }
    public DriveEncoderLocalizer(ExpansionHubMotor lf, ExpansionHubMotor lr, ExpansionHubMotor rr, ExpansionHubMotor rf, DriveEncoderConstants constants, Pose startPose) {
        this(new HubEncoder(lf), new HubEncoder(lr), new HubEncoder(rr), new HubEncoder(rf), constants, startPose);
    }
    public DriveEncoderLocalizer(Encoder lf, Encoder lr, Encoder rr, Encoder rf, DriveEncoderConstants constants) {
        this(lf, lr, rr, rf, constants, new Pose());
    }
    /**
     * This creates a new DriveEncoderLocalizer from a HardwareMap and a Pose, with the Pose
     * specifying the starting pose of the localizer.
     *
     * @param lf the left front motor
     * @param lr the left rear motor
     * @param rf the right front motor
     * @param rr the right rear motor
     * @param setStartPose the Pose to start from
     */
    public DriveEncoderLocalizer(Encoder lf, Encoder lr, Encoder rr, Encoder rf, DriveEncoderConstants constants, Pose setStartPose) {
        FORWARD_TICKS_TO_INCHES = constants.forwardTicksToInches;
        STRAFE_TICKS_TO_INCHES = constants.strafeTicksToInches;
        TURN_TICKS_TO_RADIANS = constants.turnTicksToInches;

        ROBOT_WIDTH = constants.robot_Width;
        ROBOT_LENGTH = constants.robot_Length;

        leftFront = lf;
        leftRear = lr;
        rightRear = rr;
        rightFront = rf;

        leftFront.setMultiplier(constants.leftFrontEncoderDirection == SystemCoreEncoder.REVERSE ? -1 : 1);
        leftRear.setMultiplier(constants.leftRearEncoderDirection == SystemCoreEncoder.REVERSE ? -1 : 1);
        rightFront.setMultiplier(constants.rightFrontEncoderDirection == SystemCoreEncoder.REVERSE ? -1 : 1);
        rightRear.setMultiplier(constants.rightRearEncoderDirection == SystemCoreEncoder.REVERSE ? -1 : 1);

        setStartPose(setStartPose);
        timer = new NanoTimer();
        deltaTimeNano = 1;
        displacementPose = new Pose();
        currentVelocity = new Pose();
    }

    /**
     * This returns the current pose estimate.
     *
     * @return returns the current pose estimate as a Pose
     */
    @Override
    public Pose getPose() {
        return startPose.plus(displacementPose);
    }

    /**
     * This returns the current velocity estimate.
     *
     * @return returns the current velocity estimate as a Pose
     */
    @Override
    public Pose getVelocity() {
        return currentVelocity;
    }

    /**
     * This returns the current velocity estimate.
     *
     * @return returns the current velocity estimate as a Vector
     */
    @Override
    public Vector getVelocityVector() {
        return currentVelocity.getAsVector();
    }

    /**
     * This sets the start pose. Changing the start pose should move the robot as if all its
     * previous movements were displacing it from its new start pose.
     *
     * @param setStart the new start pose
     */
    @Override
    public void setStartPose(Pose setStart) {
        startPose = setStart;
    }

    /**
     * This sets the Matrix that contains the previous pose's heading rotation.
     *
     * @param heading the rotation of the Matrix
     */
    public void setPrevRotationMatrix(double heading) {
        prevRotationMatrix = new Matrix(3,3);
        prevRotationMatrix.set(0, 0, Math.cos(heading));
        prevRotationMatrix.set(0, 1, -Math.sin(heading));
        prevRotationMatrix.set(1, 0, Math.sin(heading));
        prevRotationMatrix.set(1, 1, Math.cos(heading));
        prevRotationMatrix.set(2, 2, 1.0);
    }

    /**
     * This sets the current pose estimate. Changing this should just change the robot's current
     * pose estimate, not anything to do with the start pose.
     *
     * @param setPose the new current pose estimate
     */
    @Override
    public void setPose(Pose setPose) {
        displacementPose = setPose.minus(startPose);
        resetEncoders();
    }

    /**
     * This updates the elapsed time timer that keeps track of time between updates, as well as the
     * change position of the Encoders. Then, the robot's global change in position is calculated
     * using the pose exponential method.
     */
    @Override
    public void update() {
        deltaTimeNano = timer.getElapsedTime();
        timer.resetTimer();

        updateEncoders();
        Matrix robotDeltas = getRobotDeltas();
        Matrix globalDeltas;
        setPrevRotationMatrix(getPose().getHeading());

        Matrix transformation = new Matrix(3,3);
        if (Math.abs(robotDeltas.get(2, 0)) < 0.001) {
            transformation.set(0, 0, 1.0 - (Math.pow(robotDeltas.get(2, 0), 2) / 6.0));
            transformation.set(0, 1, -robotDeltas.get(2, 0) / 2.0);
            transformation.set(1, 0, robotDeltas.get(2, 0) / 2.0);
            transformation.set(1, 1, 1.0 - (Math.pow(robotDeltas.get(2, 0), 2) / 6.0));
            transformation.set(2, 2, 1.0);
        } else {
            transformation.set(0, 0, Math.sin(robotDeltas.get(2, 0)) / robotDeltas.get(2, 0));
            transformation.set(0, 1, (Math.cos(robotDeltas.get(2, 0)) - 1.0) / robotDeltas.get(2, 0));
            transformation.set(1, 0, (1.0 - Math.cos(robotDeltas.get(2, 0))) / robotDeltas.get(2, 0));
            transformation.set(1, 1, Math.sin(robotDeltas.get(2, 0)) / robotDeltas.get(2, 0));
            transformation.set(2, 2, 1.0);
        }

        globalDeltas = Matrix.multiply(Matrix.multiply(prevRotationMatrix, transformation), robotDeltas);

        displacementPose = displacementPose.plus(new Pose(globalDeltas.get(0, 0), globalDeltas.get(1, 0), globalDeltas.get(2, 0)));
        currentVelocity = new Pose(globalDeltas.get(0, 0) / (deltaTimeNano / Math.pow(10.0, 9)), globalDeltas.get(1, 0) / (deltaTimeNano / Math.pow(10.0, 9)), globalDeltas.get(2, 0) / (deltaTimeNano / Math.pow(10.0, 9)));

        totalHeading += globalDeltas.get(2, 0);
    }

    /**
     * This updates the Encoders.
     */
    public void updateEncoders() {
        leftFront.update();
        rightFront.update();
        leftRear.update();
        rightRear.update();
    }

    /**
     * This resets the Encoders.
     */
    public void resetEncoders() {
        leftFront.reset();
        rightFront.reset();
        leftRear.reset();
        rightRear.reset();
    }

    /**
     * This calculates the change in position from the perspective of the robot using information
     * from the Encoders.
     *
     * @return returns a Matrix containing the robot relative movement.
     */
    public Matrix getRobotDeltas() {
        Matrix returnMatrix = new Matrix(3,1);
        // x/forward movement
        returnMatrix.set(0,0, FORWARD_TICKS_TO_INCHES * (leftFront.getDeltaPosition() + rightFront.getDeltaPosition() + leftRear.getDeltaPosition() + rightRear.getDeltaPosition()));
        //y/strafe movement
        returnMatrix.set(1,0, STRAFE_TICKS_TO_INCHES * (-leftFront.getDeltaPosition() + rightFront.getDeltaPosition() + leftRear.getDeltaPosition() - rightRear.getDeltaPosition()));
        // theta/turning
        returnMatrix.set(2,0, TURN_TICKS_TO_RADIANS * ((-leftFront.getDeltaPosition() + rightFront.getDeltaPosition() - leftRear.getDeltaPosition() + rightRear.getDeltaPosition()) / (ROBOT_WIDTH + ROBOT_LENGTH)));
        return returnMatrix;
    }

    /**
     * This returns how far the robot has turned in radians, in a number not clamped between 0 and
     * 2 * pi radians. This is used for some tuning things and nothing actually within the following.
     *
     * @return returns how far the robot has turned in total, in radians.
     */
    @Override
    public double getTotalHeading() {
        return totalHeading;
    }

    /**
     * This returns the multiplier applied to forward movement measurement to convert from encoder
     * ticks to inches. This is found empirically through a tuner.
     *
     * @return returns the forward ticks to inches multiplier
     */
    @Override
    public double getForwardMultiplier() {
        return FORWARD_TICKS_TO_INCHES;
    }

    /**
     * This returns the multiplier applied to lateral/strafe movement measurement to convert from
     * encoder ticks to inches. This is found empirically through a tuner.
     *
     * @return returns the lateral/strafe ticks to inches multiplier
     */
    @Override
    public double getLateralMultiplier() {
        return STRAFE_TICKS_TO_INCHES;
    }

    /**
     * This returns the multiplier applied to turning movement measurement to convert from encoder
     * ticks to radians. This is found empirically through a tuner.
     *
     * @return returns the turning ticks to radians multiplier
     */
    @Override
    public double getTurningMultiplier() {
        return TURN_TICKS_TO_RADIANS;
    }

    /**
     * This does nothing since this localizer does not use the IMU.
     */
    @Override
    public void resetIMU() {
    }

    @Override
    public double getIMUHeading() {
        return Double.NaN;
    }

    /**
     * This returns whether if any component of robot's position is NaN.
     *
     * @return returns whether the robot's position is NaN
     */
    @Override
    public boolean isNAN() {
        return Double.isNaN(getPose().getX()) || Double.isNaN(getPose().getY()) || Double.isNaN(getPose().getHeading());
    }


}
