/*
package com.pedropathing.ftc.drivetrains;

Note from Kevin: I migrated this mechanically, but after finding that it's deprecated, decided to just remove it.

import com.pedropathing.drivetrain.CustomDrivetrain;
import com.pedropathing.math.Vector;
import com.revrobotics.spark.A301;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.system.RobotController;

import java.util.Arrays;
import java.util.List;

/**
 * This is the MecanumEx class, a version of Mecanum using CustomDrivetrain
 * @author Kabir Goyal
 * @version 1.0, 4/30/2025
 * //
@Deprecated
public class MecanumEx extends CustomDrivetrain {
    public MecanumConstants constants;
    private final Motor leftFront;
    private final Motor leftRear;
    private final Motor rightFront;
    private final Motor rightRear;
    private final List<Motor> motors;
    private final double[] lastMotorPowers;
    private double motorCachingThreshold;
    private boolean useBrakeModeInTeleOp;
    private double staticFrictionCoefficient;

    public MecanumEx(A301 lf, A301 lr, A301 rr, A301 rf, MecanumConstants mc) {
        this(new A301Motor(lf), new A301Motor(lr), new A301Motor(rr), new A301Motor(rf), mc);
    }

    public MecanumEx(
            ExpansionHubMotor lf,
            ExpansionHubMotor lr,
            ExpansionHubMotor rr,
            ExpansionHubMotor rf,
            MecanumConstants mc
    ){
        this(new HubMotor(lf), new HubMotor(lr), new HubMotor(rr), new HubMotor(rf), mc);
    }
    /**
     * This creates a new Mecanum, which takes in various movement vectors and outputs
     * the wheel drive powers necessary to move in the intended direction, given the true movement
     * vector for the front left mecanum wheel.
     *
     * @param lf The Left Front motor
     * @param lr the Left Rear motor
     * @param rr the Right Rear motor
     * @param rf the Right Front motor
     * @param mecanumConstants this is the MecanumConstants object that contains the names of the motors and directions etc.
     * //
    public MecanumEx(Motor lf, Motor lr, Motor rr, Motor rf, MecanumConstants mecanumConstants) {
        constants = mecanumConstants;

        this.maxPowerScaling = mecanumConstants.maxPower;
        this.motorCachingThreshold = mecanumConstants.motorCachingThreshold;
        this.useBrakeModeInTeleOp = mecanumConstants.useBrakeModeInTeleOp;

        leftFront = lf;
        leftRear = lr;
        rightRear = rr;
        rightFront = rf;

        motors = Arrays.asList(leftFront, leftRear, rightFront, rightRear);
        lastMotorPowers = new double[] {0,0,0,0};

        // TODO:KBF Migrate this 'up the stack' somewhere?
        /*
        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }
        * //
        setMotorsToFloat();
        breakFollowing();

        Vector copiedFrontLeftVector = mecanumConstants.frontLeftVector.normalize();
        vectors = new Vector[]{
                new Vector(copiedFrontLeftVector.getMagnitude(), copiedFrontLeftVector.getTheta()),
                new Vector(copiedFrontLeftVector.getMagnitude(), 2 * Math.PI - copiedFrontLeftVector.getTheta()),
                new Vector(copiedFrontLeftVector.getMagnitude(), 2 * Math.PI - copiedFrontLeftVector.getTheta()),
                new Vector(copiedFrontLeftVector.getMagnitude(), copiedFrontLeftVector.getTheta())};
    }

    /**
     * This method takes in forward, strafe, and rotation values and applies them to
     * the drivetrain.
     *
     * @param forward the forward power value, which would typically be
     *                -gamepad1.left_stick_y in a normal arcade drive setup
     * @param strafe the strafe power value, which would typically be
     *               -gamepad1.left_stick_x in a normal arcade drive setup
     *               because pedro treats left as positive
     * @param rotation the rotation power value, which would typically be
     *                 -gamepad1.right_stick_x in a normal arcade drive setup
     *                 because CCW is positive
     * //
    public void arcadeDrive(double forward, double strafe, double rotation) {
        double[] wheelPowers = new double[4];

        wheelPowers[0] = forward + strafe - rotation; //leftFront
        wheelPowers[1] = forward - strafe - rotation; //leftRear
        wheelPowers[2] = forward - strafe + rotation; //rightFront
        wheelPowers[3] = forward + strafe + rotation; //rightRear

        double denom = 1;
        for (double power : wheelPowers) {
           denom = Math.max(denom, Math.abs(power));
        }

        for (int i = 0; i < 4; i++) {
            wheelPowers[i] = wheelPowers[i] / denom;
        }

        for (int i = 0; i < motors.size(); i++) {
            if (Math.abs(lastMotorPowers[i] - wheelPowers[i]) > motorCachingThreshold ||
                    (wheelPowers[i] == 0 && lastMotorPowers[i] != 0)) {
                lastMotorPowers[i] = wheelPowers[i];
                motors.get(i).setPower(wheelPowers[i]);
            }
        }
    }

    @Override
    public void updateConstants() {
        leftFront.setReversed(constants.leftRearMotorInverted);
        leftRear.setReversed(constants.leftRearMotorInverted);
        rightFront.setReversed(constants.rightFrontMotorInverted);
        rightRear.setReversed(constants.rightRearMotorInverted);
        this.motorCachingThreshold = constants.motorCachingThreshold;
        this.useBrakeModeInTeleOp = constants.useBrakeModeInTeleOp;
        this.voltageCompensation = constants.useVoltageCompensation;
        this.nominalVoltage = constants.nominalVoltage;
        this.staticFrictionCoefficient = constants.staticFrictionCoefficient;
    }


    /**
     * This sets the motors to the zero power behavior of brake.
     * //
    private void setMotorsToBrake() {
        for (Motor motor : motors) {
            motor.setZeroBraking(true);
        }
    }

    /**
     * This sets the motors to the zero power behavior of float.
     * //
    private void setMotorsToFloat() {
        for (Motor motor : motors) {
            motor.setZeroBraking(false);
        }
    }

    @Override
    public void breakFollowing() {
        for (int i = 0; i < motors.size(); i++) {
            lastMotorPowers[i] = 0;
            motors.get(i).setPower(0);
        }
        setMotorsToFloat();
    }

    @Override
    public void startTeleopDrive() {
        if (useBrakeModeInTeleOp) {
            setMotorsToBrake();
        }
    }

    @Override
    public void startTeleopDrive(boolean brakeMode) {
        if (brakeMode) {
            setMotorsToBrake();
        } else {
            setMotorsToFloat();
        }
    }

    public void getAndRunDrivePowers(Vector correctivePower, Vector headingPower,
                                     Vector pathingPower, double robotHeading, Vector robotVelocity) {
        super.runDrive(correctivePower, headingPower, pathingPower, robotHeading,
                       robotVelocity);
    }

    @Override
    public double xVelocity() {
        return constants.xVelocity;
    }

    @Override
    public double yVelocity() {
        return constants.yVelocity;
    }

    @Override
    public void setXVelocity(double xMovement) { constants.setXVelocity(xMovement); }
    @Override
    public void setYVelocity(double yMovement) { constants.setYVelocity(yMovement); }

    public double getStaticFrictionCoefficient() {
        return staticFrictionCoefficient;
    }

    @Override
    public double getVoltage() {
        return RobotController.getBatteryVoltage();
    }

    private double getVoltageNormalized() {
        double voltage = getVoltage();
        return (nominalVoltage - (nominalVoltage * staticFrictionCoefficient)) / (voltage - ((nominalVoltage * nominalVoltage / voltage) * staticFrictionCoefficient));
    }

    @Override
    public String debugString() {
        return "Mecanum{" +
                " leftFront=" + leftFront +
                ", leftRear=" + leftRear +
                ", rightFront=" + rightFront +
                ", rightRear=" + rightRear +
                ", motors=" + motors +
                ", motorCachingThreshold=" + motorCachingThreshold +
                ", useBrakeModeInTeleOp=" + useBrakeModeInTeleOp +
                '}';
    }

    public List<Motor> getMotors() {
        return motors;
    }
}
*/