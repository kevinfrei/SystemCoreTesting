package com.pedropathing.ftc.localization.localizers;

import com.pedropathing.ftc.localization.CustomIMU;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.HubEncoder;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;

import com.pedropathing.ftc.localization.SystemCoreEncoder;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Matrix;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.pedropathing.util.NanoTimer;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;

/**
 * This is the ThreeWheelIMULocalizer class. This class extends the Localizer superclass and is a
 * localizer that uses the three wheel odometry set up with the IMU to have more accurate heading
 * readings.
 *
 * @author Logan Nash
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 7/9/2024
 */

public class ThreeWheelIMULocalizer implements Localizer {
    private Pose startPose;
    private Pose displacementPose;
    private Pose currentVelocity;
    private Matrix prevRotationMatrix;
    private final NanoTimer timer;
    private long deltaTimeNano;
    private final Encoder leftEncoder;
    private final Encoder rightEncoder;
    private final Encoder strafeEncoder;
    private final Pose leftEncoderPose;
    private final Pose rightEncoderPose;
    private final Pose strafeEncoderPose;

    public final CustomIMU imu;
    private double previousIMUOrientation;
    private double deltaRadians;
    private double totalHeading;
    public static double FORWARD_TICKS_TO_INCHES;
    public static double STRAFE_TICKS_TO_INCHES;
    public static double TURN_TICKS_TO_RADIANS;

    public static boolean useIMU = true;

    /**
     * This creates a new ThreeWheelIMULocalizer from a HardwareMap, with a starting Pose at (0,0)
     * facing 0 heading.
     *
     * @param lEnc the Left dead-wheel encoder
     * @param rEnc the right dead-wheel encoder
     * @param strafeEnc the strafing dead-wheel encoder
     * @param hw_imu the IMU to be used for heading (along with the wheels...)
     */
    public ThreeWheelIMULocalizer(
            org.wpilib.hardware.rotation.Encoder lEnc,
            org.wpilib.hardware.rotation.Encoder rEnc,
            org.wpilib.hardware.rotation.Encoder strafeEnc,
            CustomIMU hw_imu,
            ThreeWheelIMUConstants constants) {
        this(lEnc, rEnc, strafeEnc, hw_imu, constants, new Pose());
    }

    public ThreeWheelIMULocalizer(
            org.wpilib.hardware.rotation.Encoder lEnc,
            org.wpilib.hardware.rotation.Encoder rEnc,
            org.wpilib.hardware.rotation.Encoder strafeEnc,
            CustomIMU hw_imu,
            ThreeWheelIMUConstants constants,
            Pose setStartPose) {
        this(new SystemCoreEncoder(lEnc), new SystemCoreEncoder(rEnc), new SystemCoreEncoder(strafeEnc), hw_imu, constants, setStartPose);
    }

    public ThreeWheelIMULocalizer(
            ExpansionHubMotor lEnc,
            ExpansionHubMotor rEnc,
            ExpansionHubMotor strafeEnc,
            CustomIMU hw_imu,
            ThreeWheelIMUConstants constants) {
        this(lEnc, rEnc, strafeEnc, hw_imu, constants, new Pose());
    }

    public ThreeWheelIMULocalizer(
            ExpansionHubMotor lEnc,
            ExpansionHubMotor rEnc,
            ExpansionHubMotor strafeEnc,
            CustomIMU hw_imu,
            ThreeWheelIMUConstants constants,
            Pose setStartPose) {
        this(new HubEncoder(lEnc), new HubEncoder(rEnc), new HubEncoder(strafeEnc), hw_imu, constants, setStartPose);
    }
    /**
     * This creates a new ThreeWheelIMULocalizer from a HardwareMap and a Pose, with the Pose
     * specifying the starting pose of the localizer.
     *
     * @param lEnc the Left dead-wheel encoder
     * @param rEnc the right dead-wheel encoder
     * @param strafeEnc the strafing dead-wheel encoder
     * @param hw_imu the IMU to be used for heading (along with the wheels...)
     * @param setStartPose the Pose to start from
     */
    public ThreeWheelIMULocalizer(Encoder lEnc, Encoder rEnc, Encoder strafeEnc, CustomIMU hw_imu, ThreeWheelIMUConstants constants, Pose setStartPose) {
        FORWARD_TICKS_TO_INCHES = constants.forwardTicksToInches;
        STRAFE_TICKS_TO_INCHES = constants.strafeTicksToInches;
        TURN_TICKS_TO_RADIANS = constants.turnTicksToInches;
        imu = hw_imu;

        leftEncoderPose = new Pose(0, constants.leftPodY, 0);
        rightEncoderPose = new Pose(0, constants.rightPodY, 0);
        strafeEncoderPose = new Pose(constants.strafePodX, 0, Math.toRadians(90));

        imu.initialize();

        leftEncoder = lEnc;
        rightEncoder = rEnc;
        strafeEncoder = strafeEnc;

        leftEncoder.setMultiplier(constants.leftEncoderDirection);
        rightEncoder.setMultiplier(constants.rightEncoderDirection);
        strafeEncoder.setMultiplier(constants.strafeEncoderDirection);

        setStartPose(setStartPose);
        timer = new NanoTimer();
        deltaTimeNano = 1;
        displacementPose = new Pose();
        currentVelocity = new Pose();
        totalHeading = 0;

        resetEncoders();
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
        leftEncoder.update();
        rightEncoder.update();
        strafeEncoder.update();

        double currentIMUOrientation = MathFunctions.normalizeAngle(imu.getHeading());
        deltaRadians = MathFunctions.getTurnDirection(previousIMUOrientation, currentIMUOrientation) * MathFunctions.getSmallestAngleDifference(currentIMUOrientation, previousIMUOrientation);
        previousIMUOrientation = currentIMUOrientation;
    }

    /**
     * This resets the Encoders.
     */
    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
        strafeEncoder.reset();
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
        returnMatrix.set(0,0, FORWARD_TICKS_TO_INCHES * (rightEncoder.getDeltaPosition() * leftEncoderPose.getY() - leftEncoder.getDeltaPosition() * rightEncoderPose.getY()) / (leftEncoderPose.getY() - rightEncoderPose.getY()));
        //y/strafe movement
        returnMatrix.set(1,0, STRAFE_TICKS_TO_INCHES * (strafeEncoder.getDeltaPosition() - strafeEncoderPose.getX() * ((rightEncoder.getDeltaPosition() - leftEncoder.getDeltaPosition()) / (leftEncoderPose.getY() - rightEncoderPose.getY()))));
        // theta/turning
        if (MathFunctions.getSmallestAngleDifference(0, deltaRadians) > 0.00005 && useIMU) {
            returnMatrix.set(2, 0, deltaRadians);
        } else {
            returnMatrix.set(2,0, TURN_TICKS_TO_RADIANS * (rightEncoder.getDeltaPosition() - leftEncoder.getDeltaPosition()) / (leftEncoderPose.getY() - rightEncoderPose.getY()));
        }
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
     * This resets the IMU.
     */
    @Override
    public void resetIMU() {
        imu.resetYaw();
    }

    /**
     * This is returns the IMU.
     *
     * @return returns the IMU
     */
    @Override
    public double getIMUHeading() {
        return imu.getHeading();
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
