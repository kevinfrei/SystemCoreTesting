package first.robot.components;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.SystemCoreMap;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.drivetrains.SCMotor;
import com.pedropathing.ftc.localization.SCEncoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.ftc.localization.localizers.DriveEncoderLocalizer;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.ftc.localization.localizers.TwoWheelLocalizer;
import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.Localizer;
import com.pedropathing.paths.PathConstraints;
import first.robot.Robot;
import first.robot.components.DriveBase.Speed;
import first.support.GoBildaPinpoint;
import first.support.GoBildaPinpoint.DeviceStatus;
import first.support.GoBildaPinpoint.GoBildaOdometryPods;
import javax.management.InvalidApplicationException;
import org.wpilib.command2.SubsystemBase;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.hardware.hal.CANBusMap;
import org.wpilib.hardware.hal.util.AllocationException;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.opmode.OpMode;
import org.wpilib.opmode.Teleop;
import org.wpilib.opmode.Utility;
import org.wpilib.units.Units;

public class DriveBase {

    public static class Config {

        /* HARDWARE CONFIGURATION */
        // Motors:
        public static boolean FL_INVERTED = true;
        public static boolean FR_INVERTED = false;
        public static boolean RL_INVERTED = true;
        public static boolean RR_INVERTED = false;

        // PinPoint Odo pods:
        public static double fwdMultiplier_pp = 1.0;
        public static double latMultiplier_pp = 1.0;
        public static double rotMulitiplier_pp = 1.0;
        public static double fwdPodDirection_pp = SCEncoder.REVERSE;
        public static double latPodDirection_pp = SCEncoder.REVERSE;
        public static double fwdPodTicksToInches_pp = 1.0; // 2000 / ((Math.PI * 32) / 25.4);
        public static double latPodTicksToInches_pp = 1.0; // 2000 / ((Math.PI * 32) / 25.4);
        public static double fwdPodYOffset_pp = 0; // Use the offset tuner [-2.5]
        public static double latPodXOffset_pp = 0; // Use the offset tuner [0.25]

        // 2 dead wheel odo:
        public static OnboardIMU.MountOrientation imuOrientation_2w =
            OnboardIMU.MountOrientation.LANDSCAPE;
        public static double fwdMultiplier_2w = 1.0;
        public static double latMultiplier_2w = 1.0;
        public static double rotMulitiplier_2w = 1.0;
        public static double fwdPodDirection_2w = SCEncoder.REVERSE;
        public static double latPodDirection_2w = SCEncoder.REVERSE;
        public static double fwdPodTicksToInches_2w = 2000 / ((Math.PI * 32) / 25.4);
        public static double latPodTicksToInches_2w = 2000 / ((Math.PI * 32) / 25.4);
        public static double fwdPodYOffset_2w = 0; // Use the offset tuner [-2.5]
        public static double latPodXOffset_2w = 0; // Use the offset tuner [0.25]

        // Motor odometry:
        public static double fwdTicksToInches_m = 135;
        public static double latTicksToInches_m = 150;
        public static double turnTicksToInches_m = 100;

        /* SOFTWARE CONFIGURATION */
        // Max power scaling for translational driving:
        public static double SNAIL_SPEED = 0.40;
        public static double NORMAL_SPEED = 0.8;
        public static double TURBO_SPEED = 1.0;
        public static double AUTO_SPEED = 0.95;

        // The 'fastest' the robot can turn (0: not turning, 1.0: Fastest possible)
        public static double SNAIL_TURN = 0.25;
        public static double NORMAL_TURN = 0.5;
        public static double TURBO_TURN = 1.0;

        public static double STICK_DEAD_ZONE = 0.05;
        public static double DRIVE_STICK_CURVE = 3.0;

        /**** Stuff for the PedroPathing follower ****/

        // Measured by hoomans:
        public static double botWeightKg = 4.90;
        public static double botWidth = 10.1;
        public static double botLength = 12.5;

        // Adjusted to be sensible (no good guidance on these :/ )
        public static double brakingStrength = 0.5;
        public static double brakingStart = 0.5;
        // Values from tuners:
        public static double xVelocity = 59.2;
        public static double yVelocity = 51.7;
        public static double forwardDeceleration = -40.0;
        public static double lateralDeceleration = -48.0;
        public static double centripetalScale = 0.0005;
        // PIDs to be tuned:
        public static PIDFCoefficients translationPID = new PIDFCoefficients(
            0.08,
            0.000005,
            0.008,
            0.02
        );
        public static PIDFCoefficients headingPID = new PIDFCoefficients(0.9, 0.005, 0.05, 0.02);
        // "Kalman filtering": T in this constructor is the % of the previous
        // derivative that should be used to calculate the derivative.
        // (D is "Derivative" in PIDF...)
        // Tristan says Kalman Filtering is for curve prediction, so...it helps predict
        // ac/deceleration?
        public static FilteredPIDFCoefficients drivePID = new FilteredPIDFCoefficients(
            0.005,
            0.00001,
            0.0004,
            0.6, // Kalman filter: 60% of D will come from the *previous* derivative
            0.02
        );

        // The percent of a path that must be complete for Pedro to decide it's done
        public static double tValueContraint = 0.99;

        // Time, in *milliseconds*, to let the follower algorithm correct
        // before the path is considered "complete".
        public static double timeoutConstraint = 250;

        // The maximum velocity (in inches/second) the bot can be moving while still
        // saying the path is complete.
        public static double acceptableVelocity = 1.0;
        // The maximum distance (in inches) the bot can be from the path end
        // while still saying the path is complete.
        public static double acceptableDistance = 2.0;
        // The maximum heading error (in degrees) the bot can be from the path end
        // while still saying the path is complete.
        public static double acceptableHeading = 2.5;

        public static FollowerConstants getFollowerConstants() {
            return new FollowerConstants()
                .mass(botWeightKg)
                .forwardZeroPowerAcceleration(forwardDeceleration)
                .lateralZeroPowerAcceleration(lateralDeceleration)
                .holdPointTranslationalScaling(1)
                .headingPIDFCoefficients(headingPID)
                .drivePIDFCoefficients(drivePID)
                .translationalPIDFCoefficients(translationPID)
                .centripetalScaling(centripetalScale);
        }

        public static PathConstraints getPathConstraints() {
            PathConstraints pc = new PathConstraints(
                tValueContraint,
                timeoutConstraint,
                brakingStrength,
                brakingStart
            );
            pc.setVelocityConstraint(acceptableVelocity);
            pc.setTranslationalConstraint(acceptableDistance);
            pc.setHeadingConstraint(Math.toRadians(acceptableHeading));
            return pc;
        }

        public static MecanumConstants getDriveConstants() {
            return new MecanumConstants()
                .maxPower(1)
                .leftFrontMotorInverted(FL_INVERTED)
                .leftRearMotorInverted(RL_INVERTED)
                .rightFrontMotorInverted(FR_INVERTED)
                .rightRearMotorInverted(RR_INVERTED)
                .xVelocity(xVelocity)
                .yVelocity(yVelocity);
        }

        public static Localizer getLocalizer(SystemCoreMap scm) {
            Localizer loc = switch (Localization.WhichLocalizer) {
                case Localization.LocalizerSelection.USE_PINPOINT -> new PinpointLocalizer(
                    scm,
                    Localization.getPinpointConstants()
                );
                case Localization.LocalizerSelection.USE_MOTORS -> new DriveEncoderLocalizer(
                    scm,
                    Localization.getDriveEncoderConstants()
                );
                case Localization.LocalizerSelection.USE_TWO_WHEEL -> new TwoWheelLocalizer(
                    scm,
                    Localization.getTwoWheelConstants()
                );
                default -> null;
            };
            return loc;
        }

        public static class Localization {

            public enum LocalizerSelection {
                USE_MOTORS,
                USE_TWO_WHEEL,
                // USE_OTOS,
                USE_PINPOINT,
            }

            public static LocalizerSelection WhichLocalizer = LocalizerSelection.USE_PINPOINT;

            public static DriveEncoderConstants getDriveEncoderConstants() {
                return new DriveEncoderConstants()
                    .leftFrontEncoderDirection(SCEncoder.FORWARD)
                    .leftRearEncoderDirection(SCEncoder.FORWARD)
                    .rightFrontEncoderDirection(SCEncoder.FORWARD)
                    .rightRearEncoderDirection(SCEncoder.FORWARD)
                    .forwardTicksToInches(fwdTicksToInches_m)
                    .strafeTicksToInches(latTicksToInches_m)
                    .turnTicksToInches(turnTicksToInches_m)
                    .robotLength(botLength)
                    .robotWidth(botWidth);
            }

            public static TwoWheelConstants getTwoWheelConstants() {
                return new TwoWheelConstants()
                    .forwardEncoderDirection(fwdPodDirection_2w)
                    .forwardTicksToInches(fwdPodTicksToInches_2w)
                    .forwardPodY(fwdPodYOffset_2w)
                    .strafeEncoderDirection(latPodDirection_2w)
                    .strafeTicksToInches(latPodTicksToInches_2w)
                    .strafePodX(latPodXOffset_2w)
                    .IMU_Orientation(imuOrientation_2w);
            }

            public static PinpointConstants getPinpointConstants() {
                return new PinpointConstants()
                    .forwardPodY(fwdPodYOffset_pp)
                    .strafePodX(latPodXOffset_pp)
                    .strafeEncoderDirection(
                        latPodDirection_pp == SCEncoder.REVERSE
                            ? GoBildaPinpoint.EncoderDirection.REVERSED
                            : GoBildaPinpoint.EncoderDirection.FORWARD
                    )
                    .forwardEncoderDirection(
                        fwdPodDirection_pp == SCEncoder.REVERSE
                            ? GoBildaPinpoint.EncoderDirection.REVERSED
                            : GoBildaPinpoint.EncoderDirection.FORWARD
                    )
                    .distanceUnit(Units.Inch);
            }
        }
    }

    public enum Speed {
        Snail,
        Normal,
        Turbo,
    }

    // TODO: Put drive base commands in here
    public static class Commands {}

    /*********************
     * Begin PedroPathing stuff
     *********************/

    protected static SystemCoreMap scm = null;
    protected static Follower follower = null;

    protected static Follower createFollower(SystemCoreMap theScm) {
        if (follower == null) {
            if (DriveBase.scm != null && theScm != DriveBase.scm) {
                throw new AllocationException(
                    "Attempt to create a second follower for the singleton drivebase with a different FWDB"
                );
            }
            scm = theScm;
            Follower f = new FollowerBuilder(Config.getFollowerConstants())
                .pathConstraints(Config.getPathConstraints())
                .mecanumDrivetrain(scm, Config.getDriveConstants())
                .setLocalizer(Config.getLocalizer(scm))
                .build();
            f.setMaxPowerScaling(Config.AUTO_SPEED);
            follower = f;
            f.setStartingPose(new Pose(0, 0, 0));
        }
        return follower;
    }

    public static Follower getFollower(SystemCoreMap scm) {
        return createFollower(scm);
    }

    /*********************
     * End PedroPathing stuff
     *********************/

    // TODO: Flesh this out from Decode's LearnBot: drive styles & whatnot...
    public static class Component extends SubsystemBase {

        public Component(Robot r) {
            super("Drive Base");
        }
    }

    // Validation opmodes below:

    @Utility(name = "Test Motors", group = "DBComponent")
    public static class MotorValidation implements OpMode {

        SCMotor fl, fr, rr, rl;
        Gamepad g;
        static double POWER = 0.3;
        static double CUTOFF = 1.3;

        public MotorValidation(Robot robot) {
            fl = robot.frontLeft;
            fr = robot.frontRight;
            rr = robot.rearRight;
            rl = robot.rearLeft;
            g = robot.gamepad;
        }

        public void start() {
            setPower(0, 0, 0, 0);
        }

        boolean lastZero = false;

        public void periodic() {
            double x = deadZone(g.getLeftX());
            double y = deadZone(g.getLeftY());
            // Looking for corners, so both values need to have magnitude >> 0
            if (Math.abs(x) + Math.abs(y) < CUTOFF) {
                if (lastZero == false) {
                    System.out.printf("ZZ X: %f, Y: %f%n", x, y);
                }
                lastZero = true;
                setPower(0, 0, 0, 0);
            } else if (x < 0) {
                // Left
                lastZero = false;
                if (y < 0) {
                    // front
                    System.out.printf("FL X: %f, Y: %f%n", x, y);
                    setPower(POWER, 0, 0, 0);
                } else {
                    // rear
                    System.out.printf("RL X: %f, Y: %f%n", x, y);
                    setPower(0, 0, 0, POWER);
                }
            } else {
                // Right
                lastZero = false;
                if (y < 0) {
                    // front
                    System.out.printf("FR X: %f, Y: %f%n", x, y);
                    setPower(0, POWER, 0, 0);
                } else {
                    // rear
                    System.out.printf("RR X: %f, Y: %f%n", x, y);
                    setPower(0, 0, POWER, 0);
                }
            }
        }

        private void setPower(double pfl, double pfr, double prr, double prl) {
            fl.setPower(pfl);
            fr.setPower(pfr);
            rr.setPower(prr);
            rl.setPower(prl);
        }

        public void end() {
            setPower(0, 0, 0, 0);
        }
    }

    @Utility(group = "DBComponent")
    public static class PinpointTest implements OpMode {

        GoBildaPinpoint odo;
        boolean started = false;

        public PinpointTest(Robot r) {
            odo = r.pinpoint;
            odo.resetPosAndIMU();
        }

        public void start() {
            started = true;
            odo.resetPosAndIMU();
            odo.setPosition(new Pose2d(0, 0, new Rotation2d(0)));
        }

        public void periodic() {
            if (!started) {
                return;
            }
            odo.update();
            DeviceStatus status = odo.getDeviceStatus();
            switch (status) {
                case READY: {
                    int odoLoopTime = odo.getLoopTime();
                    Pose2d pose = odo.getPosition();
                    System.out.printf(
                        "x::%.1f y::%.1f h:%.1f t:%d%n",
                        pose.getX(),
                        pose.getY(),
                        pose.getRotation().getDegrees(),
                        odoLoopTime
                    );
                    break;
                }
                case NOT_READY:
                    System.out.println("ODO: Not Ready");
                    break;
                case CALIBRATING:
                    System.out.println("ODO: Calibrating");
                    break;
                case FAULT_X_POD_NOT_DETECTED:
                    System.out.println("ODO: X pod not detected");
                    break;
                case FAULT_Y_POD_NOT_DETECTED:
                    System.out.println("ODO: Y pod not detected");
                    break;
                case FAULT_NO_PODS_DETECTED:
                    System.out.println("ODO: No Pods detecte");
                    break;
                case FAULT_IMU_RUNAWAY:
                    System.out.println("ODO: IMU Runaway fault");
                    break;
                case FAULT_BAD_READ:
                    System.out.println("ODO: Bad Read (Is the device connected?)");
                    break;
            }
        }

        public void end() {
            started = false;
        }
    }

    @Teleop(name = "Pedro Tele", group = "DBComponent")
    public static class PedroValidation implements OpMode {

        Follower f;
        GoBildaPinpoint odo;
        Gamepad g;
        boolean started;

        public PedroValidation(Robot robot) {
            f = robot.follower;
            g = robot.gamepad;
            odo = robot.pinpoint;
            started = false;
        }

        public void start() {
            f.startTeleOpDrive(false);
            started = true;
        }

        public void end() {
            started = false;
        }

        Speed speed = Speed.Normal;

        protected double getSpeedMult() {
            return switch (speed) {
                case Speed.Normal -> Config.NORMAL_SPEED;
                case Speed.Snail -> Config.SNAIL_SPEED;
                case Speed.Turbo -> Config.TURBO_SPEED;
            };
        }

        protected double getTurnMult() {
            return switch (speed) {
                case Speed.Normal -> Config.NORMAL_TURN;
                case Speed.Snail -> Config.SNAIL_TURN;
                case Speed.Turbo -> Config.TURBO_TURN;
            };
        }

        // Curve the sticks, so that you have more precision at the low end
        protected double curve(double val) {
            return Math.copySign(Math.pow(Math.abs(val), Config.DRIVE_STICK_CURVE), val);
        }

        protected double fwdScale(double val) {
            return curve(val) * getSpeedMult();
        }

        protected double strafeScale(double val) {
            return curve(val) * getSpeedMult();
        }

        protected double rotateScale(double val) {
            return curve(val) * getTurnMult();
        }

        int mask = 127;
        int lastCount = 0;

        public void periodic() {
            lastCount = (lastCount + 1) & mask;
            if (!started) {
                return;
            }
            odo.update();
            f.update();
            double fwd = fwdScale(deadZone(-g.getLeftY()));
            double strafe = strafeScale(deadZone(-g.getLeftX()));
            double rotate = rotateScale(deadZone(-g.getRightX()));
            f.setTeleOpDrive(fwd, strafe, rotate, false);
            if (lastCount == 0) {
                Pose p = f.getPose();
                System.out.printf(
                    "x:%.1f y:%.1f h:%.1f%n",
                    p.getX(),
                    p.getY(),
                    Math.toDegrees(p.getHeading())
                );
                System.out.printf("f:%.3f s:%.3f r:%.3f%n", fwd, strafe, rotate);
            }
        }
    }

    @Utility(name = "Dumb Drive", group = "DBComponent")
    public static class DriveBaseDumb implements OpMode {

        Gamepad g;
        SCMotor fl, fr, rr, rl;

        public DriveBaseDumb(Robot robot) {
            fl = robot.frontLeft;
            fr = robot.frontRight;
            rr = robot.rearRight;
            rl = robot.rearLeft;
            g = robot.gamepad;
        }

        public void start() {
            fl.setReversed(true);
            fr.setReversed(false);
            rl.setReversed(true);
            rr.setReversed(false);
        }

        boolean lastZero = false;

        public void periodic() {
            double fwd = deadZone(-g.getLeftY());
            double strafe = deadZone(g.getLeftX());
            double rotate = deadZone(g.getRightX());
            if (
                Math.abs(fwd) <= 0.0001 && Math.abs(strafe) <= 0.0001 && Math.abs(rotate) <= 0.0001
            ) {
                if (!lastZero) {
                    System.out.printf("F: %f, S: %f, R: %f%n", fwd, strafe, rotate);
                }
                lastZero = true;
            } else {
                System.out.printf("F: %f, S: %f, R: %f%n", fwd, strafe, rotate);
                lastZero = false;
            }
            if (Math.abs(fwd) >= Math.abs(strafe) && Math.abs(fwd) >= Math.abs(rotate)) {
                setPower(fwd, fwd, fwd, fwd);
            } else if (Math.abs(strafe) >= Math.abs(fwd) && Math.abs(strafe) >= Math.abs(rotate)) {
                setPower(strafe, -strafe, strafe, -strafe);
            } else {
                setPower(rotate, -rotate, -rotate, rotate);
            }
        }

        private void setPower(double pfl, double pfr, double prr, double prl) {
            fl.setPower(pfl);
            fr.setPower(pfr);
            rr.setPower(prr);
            rl.setPower(prl);
        }

        public void end() {
            setPower(0, 0, 0, 0);
        }
    }

    @Teleop(name = "Trig Drive", group = "DBComponent")
    public static class DriveBaseTrig implements OpMode {

        Gamepad g;
        SCMotor fl, fr, rr, rl;
        OnboardIMU imu;

        public DriveBaseTrig(Robot robot) {
            fl = robot.frontLeft;
            fr = robot.frontRight;
            rr = robot.rearRight;
            rl = robot.rearLeft;
            g = robot.gamepad;
            imu = robot.imu;
            imu.resetYaw();
        }

        public void start() {
            fl.setReversed(true);
            fr.setReversed(false);
            rl.setReversed(true);
            rr.setReversed(false);
        }

        public void periodic() {
            // Shamelessly stolen from GM0:
            double y = deadZone(-g.getLeftY());
            double x = deadZone(g.getLeftX());
            double rx = deadZone(g.getRightX());

            if (g.getRightBumperButtonPressed() || g.getLeftBumperButtonPressed()) {
                imu.resetYaw();
            }

            double botHeading = imu.getYawRadians();

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1; // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            setPower(frontLeftPower, frontRightPower, backRightPower, backLeftPower);
        }

        private void setPower(double pfl, double pfr, double prr, double prl) {
            fl.setPower(pfl);
            fr.setPower(pfr);
            rr.setPower(prr);
            rl.setPower(prl);
        }

        public void end() {
            setPower(0, 0, 0, 0);
        }
    }

    public static double deadZone(double val) {
        if (Math.abs(val) > Config.STICK_DEAD_ZONE) {
            return Math.copySign(
                (Math.abs(val) - Config.STICK_DEAD_ZONE) / (1 - Config.STICK_DEAD_ZONE),
                val
            );
        }
        return 0;
    }
}
