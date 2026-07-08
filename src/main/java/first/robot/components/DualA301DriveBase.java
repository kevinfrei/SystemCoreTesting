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
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.revrobotics.spark.A301;
import first.robot.Robot;
import first.robot.helpers.DualA301Motor;
import first.robot.helpers.MathUtils;
import org.jspecify.annotations.Nullable;
import org.wpilib.command3.Mechanism;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.hardware.hal.CANBusMap;
import org.wpilib.hardware.hal.util.AllocationException;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.imu.OnboardIMU.MountOrientation;
import org.wpilib.opmode.OpMode;
import org.wpilib.opmode.Utility;

public class DualA301DriveBase {

    public static class Config {

        // ---------------------- //
        // HARDWARE CONFIGURATION //
        // ---------------------- //

        // Motor ports
        public static int flA = CANBusMap.CAN_D0;
        public static int flB = CANBusMap.CAN_D1;
        public static int frA = CANBusMap.CAN_D2;
        public static int frB = CANBusMap.CAN_D3;
        public static int rlA = CANBusMap.CAN_D4;
        public static int rlB = CANBusMap.CAN_D5;
        public static int rrA = CANBusMap.CAN_D6;
        public static int rrB = CANBusMap.CAN_D7;
        public static boolean flInvert = true;
        public static boolean frInvert = false;
        public static boolean rlInvert = true;
        public static boolean rrInvert = false;

        // IMU Orientation
        public static MountOrientation imuMounting = MountOrientation.LANDSCAPE;

        // Ports for the odopod encoders
        public static int fbEncCh0 = 0;
        public static int fbEncCh1 = 1;
        public static int strafeEncCh0 = 0;
        public static int strafeEncCh1 = 1;

        // ---------------------- //
        // SOFTWARE CONFIGURATION //
        // ---------------------- //

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

        // -------------------------- //
        // PedroPathing configuration //
        // -------------------------- //

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
        // Tristan says Kalman Filtering is for curve prediction, so...it helps predict ac/deceleration?
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
                .leftFrontMotorInverted(flInvert)
                .leftRearMotorInverted(rlInvert)
                .rightFrontMotorInverted(frInvert)
                .rightRearMotorInverted(rrInvert)
                .xVelocity(xVelocity)
                .yVelocity(yVelocity);
        }

        public static class Localizer {

            public static class MotorLocConfig {

                public static double fwdTicksToInches = 135;
                public static double latTicksToInches = 150;
                public static double turnTicksToInches = 100;
            }

            public static class TwoWheelConfig {

                public static double ForwardPodDirection = SCEncoder.FORWARD;
                public static double StrafePodDirection = SCEncoder.REVERSE;
                public static double ForwardPodTicksToInches = 2000 / ((Math.PI * 32) / 25.4);
                public static double StrafePodTicksToInches = 2000 / ((Math.PI * 32) / 25.4);
                public static double ForwardPodY = -2.5;
                public static double StrafePodX = 0.25;
            }

            public enum LocalizerSelection {
                USE_MOTORS,
                USE_TWO_WHEEL,
                // USE_OTOS,
                // USE_PINPOINT,
            }

            public static LocalizerSelection WhichLocalizer = LocalizerSelection.USE_TWO_WHEEL;

            public static DriveEncoderConstants getDriveEncoderConstants() {
                return new DriveEncoderConstants()
                    .leftFrontEncoderDirection(SCEncoder.FORWARD)
                    .leftRearEncoderDirection(SCEncoder.FORWARD)
                    .rightFrontEncoderDirection(SCEncoder.FORWARD)
                    .rightRearEncoderDirection(SCEncoder.FORWARD)
                    .forwardTicksToInches(MotorLocConfig.fwdTicksToInches)
                    .strafeTicksToInches(MotorLocConfig.latTicksToInches)
                    .turnTicksToInches(MotorLocConfig.turnTicksToInches)
                    .robotLength(botLength)
                    .robotWidth(botWidth);
            }

            public static TwoWheelConstants getTwoWheelConstants() {
                return new TwoWheelConstants()
                    .forwardEncoderDirection(TwoWheelConfig.ForwardPodDirection)
                    .forwardTicksToInches(TwoWheelConfig.ForwardPodTicksToInches)
                    .forwardPodY(TwoWheelConfig.ForwardPodY)
                    .strafeEncoderDirection(TwoWheelConfig.StrafePodDirection)
                    .strafeTicksToInches(TwoWheelConfig.StrafePodTicksToInches)
                    .strafePodX(TwoWheelConfig.StrafePodX)
                    .IMU_Orientation(Config.imuMounting);
            }
        }
    }

    // TODO: Put drive base commands in here
    public static class Commands {}

    // ------------------------ //
    // Begin PedroPathing stuff //
    // ------------------------ //

    protected static DriveBaseHardwareMap hwMap = null;
    protected static Follower follower = null;

    // No encoders connected: Just use the 4wdb odo, too
    protected static Follower createFollower(DriveBaseHardwareMap theMap) {
        if (follower == null) {
            if (DualA301DriveBase.hwMap != null && theMap != DualA301DriveBase.hwMap) {
                throw new AllocationException(
                    "Attempt to create a second follower for the singleton drivebase with a different FWDB"
                );
            }
            hwMap = theMap;
            Follower f = new FollowerBuilder(Config.getFollowerConstants())
                .pathConstraints(Config.getPathConstraints())
                .mecanumDrivetrain(hwMap, Config.getDriveConstants())
                .driveEncoderLocalizer(hwMap, Config.Localizer.getDriveEncoderConstants())
                .build();
            f.setMaxPowerScaling(Config.AUTO_SPEED);
            follower = f;
        }
        return follower;
    }

    public static Follower getFollower() {
        return createFollower(hwMap);
    }

    // ---------------------- //
    // End PedroPathing stuff //
    // ---------------------- //

    // TODO: Flesh this out from Decode's LearnBot: drive styles & whatnot...
    public static class Component extends Mechanism {

        public final DualA301Motor frontLeft = new DualA301Motor(
            new A301(Config.flA),
            new A301(Config.flB)
        );
        public final DualA301Motor frontRight = new DualA301Motor(
            new A301(Config.frA),
            new A301(Config.frB)
        );
        public final DualA301Motor rearLeft = new DualA301Motor(
            new A301(Config.rlA),
            new A301(Config.rlB)
        );
        public final DualA301Motor rearRight = new DualA301Motor(
            new A301(Config.rrA),
            new A301(Config.rrB)
        );
        public final OnboardIMU imu = new OnboardIMU(Config.imuMounting);

        public Component(Robot r) {
            System.out.println("There1");
            super("Drive Base");
            System.out.println("There2");
        }
    }

    // Validation opmodes below:

    @Utility(
        name = "Test Motors",
        group = "Drivebase",
        description = "Spins drive motors using the position of the left stick"
    )
    public static class MotorValidation implements OpMode {

        Robot robot;

        final DualA301DriveBase.Component drivebase;
        SCMotor fl, fr, rr, rl;
        Gamepad g;
        static double POWER = 0.3;
        static double CUTOFF = 1.3;
        static double DELTA = 0.4;

        public MotorValidation(Robot robot) {
            this.robot = robot;
            g = robot.g1;
            drivebase = new Component(robot);
            fl = drivebase.frontLeft;
            fr = drivebase.frontRight;
            rr = drivebase.rearRight;
            rl = drivebase.rearLeft;
            fl.setReversed(Config.flInvert);
            fr.setReversed(Config.frInvert);
            rl.setReversed(Config.rlInvert);
            rr.setReversed(Config.rrInvert);
        }

        public void start() {
            setPower(0, 0, 0, 0);
        }

        boolean lastZero = false;

        public void periodic() {
            double x = MathUtils.DeadZone(g.getLeftX(), Config.STICK_DEAD_ZONE);
            double y = MathUtils.DeadZone(g.getLeftY(), Config.STICK_DEAD_ZONE);
            // Looking for corners, so both values need to have magnitude >> 0
            if (Math.abs(x) + Math.abs(y) < CUTOFF || Math.abs(Math.abs(x) - Math.abs(y)) > DELTA) {
                if (!lastZero) {
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

    // This is for PedroPathing to get the hardware components
    public static class DriveBaseHardwareMap implements SystemCoreMap {

        public final Component driveBase;

        public DriveBaseHardwareMap(Component c) {
            driveBase = c;
        }

        @Override
        public @Nullable Object getHardware(HardwareName nm) {
            return switch (nm) {
                // Motors
                case FRONT_LEFT_MOTOR -> driveBase.frontLeft;
                case FRONT_RIGHT_MOTOR -> driveBase.frontRight;
                case REAR_LEFT_MOTOR -> driveBase.rearLeft;
                case REAR_RIGHT_MOTOR -> driveBase.rearRight;
                // Encoders
                case FRONT_LEFT_ENCODER -> driveBase.frontLeft;
                case FRONT_RIGHT_ENCODER -> driveBase.frontRight;
                case REAR_LEFT_ENCODER -> driveBase.rearLeft;
                case REAR_RIGHT_ENCODER -> driveBase.rearRight;
                // And the IMU...
                case IMU -> driveBase.imu;
                default -> null;
            };
        }
    }

    @Utility(name = "Try Pedro Tele", group = "Drivebase")
    public static class PedroValidation implements OpMode {

        DualA301DriveBase.Component driveBase;
        DriveBaseHardwareMap map;
        Follower f;
        Robot robot;
        Gamepad gp;
        boolean started = false;

        public PedroValidation(Robot robot) {
            try {
                this.robot = robot;
                driveBase = new Component(robot);
                map = new DriveBaseHardwareMap(driveBase);
                f = createFollower(map);
                gp = robot.g1;
            } catch (Exception e) {
                System.out.printf("Exception!!!%nMessage:%n");
                System.out.println(e.getMessage());
                System.out.println("End of this message...");
            }
        }

        public void start() {
            started = true;
            f.setStartingPose(new Pose(72, 72, 0));
            f.startTeleOpDrive(false);
        }

        private String last = "";

        private void delta(String s) {
            if (s.equals(last)) {
                return;
            }
            System.out.println(s);
            last = s;
        }

        public void periodic() {
            if (!started) {
                delta("Why is periodic running when the opmode hasn't started?");
                return;
            }
            double fwd = MathUtils.DeadZone(-gp.getLeftY(), Config.STICK_DEAD_ZONE);
            double strafe = MathUtils.DeadZone(gp.getLeftX(), Config.STICK_DEAD_ZONE);
            double rotate = MathUtils.DeadZone(gp.getRightX(), Config.STICK_DEAD_ZONE);
            Pose p = f.getPose();
            delta(
                String.format(
                    "F: %f, S: %f, R: %f X: %f Y: %f H: %f",
                    fwd,
                    strafe,
                    rotate,
                    p.getX(),
                    p.getY(),
                    p.getHeading()
                )
            );
            f.setTeleOpDrive(fwd, strafe, rotate, false);
            f.update();
        }

        public void end() {
            started = false;
        }
    }

    @Utility(name = "Dumb Drive", group = "Drivebase")
    public static class DriveBaseDumb implements OpMode {

        Robot g;
        DualA301DriveBase.Component driveBase;
        Gamepad gp;
        SCMotor fl, fr, rr, rl;

        public DriveBaseDumb(Robot robot) {
            g = robot;
            gp = robot.g1;
            driveBase = new Component(robot);
            fl = driveBase.frontLeft;
            fr = driveBase.frontRight;
            rr = driveBase.rearRight;
            rl = driveBase.rearLeft;
        }

        public void start() {
            fl.setReversed(Config.flInvert);
            fr.setReversed(Config.frInvert);
            rl.setReversed(Config.rlInvert);
            rr.setReversed(Config.rrInvert);
        }

        boolean lastZero = false;

        public void periodic() {
            double fwd = MathUtils.DeadZone(-gp.getLeftY(), Config.STICK_DEAD_ZONE);
            double strafe = MathUtils.DeadZone(gp.getLeftX(), Config.STICK_DEAD_ZONE);
            double rotate = MathUtils.DeadZone(gp.getRightX(), Config.STICK_DEAD_ZONE);
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

    @Utility(name = "Field-centric drive", group = "Drivebase")
    public static class DriveBaseTrig implements OpMode {

        Robot g;
        DualA301DriveBase.Component driveBase;
        Gamepad gp;
        SCMotor fl, fr, rr, rl;
        OnboardIMU imu;

        public DriveBaseTrig(Robot robot) {
            System.out.println("here1");
            g = robot;
            System.out.println("here2");
            gp = robot.g1;
            System.out.println("here3");
            driveBase = new Component(robot);
            System.out.println("here4");
            fl = driveBase.frontLeft;
            System.out.println("here5");
            fr = driveBase.frontRight;
            System.out.println("here6");
            rr = driveBase.rearRight;
            System.out.println("here7");
            rl = driveBase.rearLeft;
            System.out.println("here8");
            imu = driveBase.imu;
            System.out.println("here9");
            imu.resetYaw();
            System.out.println("here10");
        }

        public void start() {
            fl.setReversed(Config.flInvert);
            fr.setReversed(Config.frInvert);
            rl.setReversed(Config.rlInvert);
            rr.setReversed(Config.rrInvert);
        }

        public void periodic() {
            // Shamelessly stolen from GM0:
            double y = MathUtils.DeadZone(-gp.getLeftY(), Config.STICK_DEAD_ZONE);
            double x = MathUtils.DeadZone(gp.getLeftX(), Config.STICK_DEAD_ZONE);
            double rx = MathUtils.DeadZone(gp.getRightX(), Config.STICK_DEAD_ZONE);

            if (gp.getRightBumperButtonPressed() || gp.getLeftBumperButtonPressed()) {
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
}
