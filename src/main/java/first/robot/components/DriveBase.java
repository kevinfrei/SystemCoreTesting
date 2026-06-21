package first.robot.components;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.drivetrains.Motor;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import org.wpilib.command2.SubsystemBase;
import org.wpilib.hardware.hal.CANBusMap;
import org.wpilib.hardware.hal.util.AllocationException;
import org.wpilib.hardware.imu.OnboardIMU;
import totes.FourWheelDriveBase;

public class DriveBase {

    public static class Config {

        /* HARDWARE CONFIGURATION */
        // Ports for the A301 motors
        public static int flPort = CANBusMap.CAN_D0;
        public static int frPort = CANBusMap.CAN_D1;
        public static int rrPort = CANBusMap.CAN_D2;
        public static int rlPort = CANBusMap.CAN_D3;

        // Ports for the odopod encoders
        public static int fbEncCh0 = 0;
        public static int fbEncCh1 = 1;
        public static int strafeEncCh0 = 0;
        public static int strafeEncCh1 = 1;

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
        public static com.pedropathing.control.PIDFCoefficients translationPID =
            new com.pedropathing.control.PIDFCoefficients(0.08, 0.000005, 0.008, 0.02);
        public static com.pedropathing.control.PIDFCoefficients headingPID =
            new com.pedropathing.control.PIDFCoefficients(0.9, 0.005, 0.05, 0.02);
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
            return null;
        }

        public static PathConstraints getPathConstraints() {
            return null;
        }

        public static MecanumConstants getDriveConstants() {
            return null;
        }

        public static class Localizer {

            public static class MotorLocConfig {

                public static double fwdTicksToInches = 135;
                public static double latTicksToInches = 150;
                public static double turnTicksToInches = 100;
            }

            public static class TwoWheelConfig {

                public static String ForwardPodName = "odofb";
                public static String StrafePodName = "odostrafe";
                public static String IMUName = "imu";
                public static OnboardIMU.MountOrientation orientation =
                    OnboardIMU.MountOrientation.LANDSCAPE;
                public static double ForwardPodDirection = Encoder.FORWARD;
                public static double StrafePodDirection = Encoder.REVERSE;
                public static double ForwardPodTicksToInches = 2000 / ((Math.PI * 32) / 25.4);
                public static double StrafePodTicksToInches = 2000 / ((Math.PI * 32) / 25.4);
                public static double ForwardPodY = -2.5;
                public static double StrafePodX = 0.25;
            }

            public enum LocalizerSelection {
                USE_MOTORS,
                USE_TWO_WHEEL,
                USE_OTOS,
                USE_PINPOINT,
            }

            public static LocalizerSelection WhichLocalizer = LocalizerSelection.USE_TWO_WHEEL;

            public static DriveEncoderConstants getDriveEncoderConstants() {
                return new DriveEncoderConstants()
                    .leftFrontEncoderDirection(Encoder.FORWARD)
                    .leftRearEncoderDirection(Encoder.FORWARD)
                    .rightFrontEncoderDirection(Encoder.FORWARD)
                    .rightRearEncoderDirection(Encoder.FORWARD)
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
                    .IMU_Orientation(TwoWheelConfig.orientation);
            }
        }
    }

    public static class Commands {}

    protected static FourWheelDriveBase fwdb = null;
    protected static Follower follower = null;

    protected static Follower createFollower(FourWheelDriveBase fwdb) {
        if (follower == null) {
            if (fwdb != null && fwdb != DriveBase.fwdb) {
                throw new AllocationException(
                    "Attempt to create a second follower for the singleton drivebase with a different FWDB"
                );
            }
            Motor[] motors = fwdb.getMotors();
            Encoder[] encoders = fwdb.getEncoders();
            FollowerBuilder fb = new FollowerBuilder(Config.getFollowerConstants())
                .pathConstraints(Config.getPathConstraints())
                .mecanumDrivetrain(
                    motors[0],
                    motors[1],
                    motors[2],
                    motors[3],
                    Config.getDriveConstants()
                );
            switch (Config.Localizer.WhichLocalizer) {
                case USE_MOTORS:
                    if (encoders == null || encoders.length != 4) {
                        throw new IllegalArgumentException(
                            "Invalid encoders for using the MOTORS Localizer"
                        );
                    }
                    fb = fb.driveEncoderLocalizer(
                        encoders[0],
                        encoders[1],
                        encoders[2],
                        encoders[3],
                        Config.Localizer.getDriveEncoderConstants()
                    );
                    break;
                case USE_TWO_WHEEL:
                    fb = fb.twoWheelLocalizer(
                        encoders[0],
                        encoders[1],
                        fwdb.getIMU(),
                        Config.Localizer.getTwoWheelConstants()
                    );
                    break;
            }
            Follower f = fb.build();
            f.setMaxPowerScaling(Config.AUTO_SPEED);
            follower = f;
        }
        return follower;
    }

    public static Follower getFollower(FourWheelDriveBase fwdb) {
        return createFollower(fwdb);
    }

    public static class Component extends SubsystemBase {

        public Component(FourWheelDriveBase fwdb) {
            super("Drive Base");
        }
    }
}
