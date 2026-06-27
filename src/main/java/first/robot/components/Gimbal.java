package first.robot.components;

import first.robot.GlobalContext;
import first.robot.helpers.ElapsedTime;
import first.robot.helpers.HubServo;
import first.robot.helpers.MathUtils;
import first.robot.helpers.TargetAcquisition;
import org.wpilib.command3.*;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.math.filter.MedianFilter;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Utility;

public class Gimbal {

    // Helper class for controlling the two servos of the camera gimbal
    public static class ServoInfo {

        public double low;
        public double high;
        public double init;
        public double rangeDegrees;
        public boolean flip;

        public ServoInfo(double lo, double hi, double init, double angleRange, boolean flip) {
            low = lo;
            high = hi;
            this.init = init;
            rangeDegrees = angleRange;
            this.flip = flip;
        }

        public double Clip(double val) {
            return Math.clamp(val, low, high);
        }

        // This normalizes an angle to "bot relative" from something mounted on the gimbal
        // I'm 100% sure this is kinda wrong, hopefully only because 'flip' isn't used yet...
        public double Adjust(double externalDegrees, double servoPos) {
            double servoTicksOffCenter = servoPos - init;
            double ticksToDegrees = rangeDegrees / (high - low);
            return externalDegrees - servoTicksOffCenter * ticksToDegrees;
        }

        // Stick dead zone handling, plus scaling through a potentially uneven range of positive
        // values (servo's scale from 0 to 1, controllers scale from -1 to 1)
        double Stick(double val) {
            // Remove the dead zone for the stick, and scale the remainder between 0-1
            double scaledStick = MathUtils.DeadZone(val, Config.TESTING_DEAD_ZONE);
            // Get the higher of the two ranges (mid to high, mid to low)
            double maxRange = Math.max(Math.abs(init - high), Math.abs(init - low));
            return Clip(scaledStick * maxRange + init);
        }
    }

    public static class Config {

        // Hardware Configuration:
        public static int USB_ID = 0;
        public static int YAW_SERVO_PORT = 0; // TODO
        public static ServoInfo Yaw = new ServoInfo(0.0, 0.95, 0.475, 100.0, false);
        public static int PITCH_SERVO_PORT = 1; // TODO
        public static ServoInfo Pitch = new ServoInfo(0.05, 1.0, 0.15, 80.0, true);

        // Stuff for TargetAcquisition
        public static double TARGET_HEIGHT = 23.5; // Inches: This is a blind guess
        public static double CAMERA_HEIGHT = 5; // I could go look at the CAD...

        // OpMode testing configuration:
        public static double TESTING_DELTA = 0.025;
        public static double TESTING_DEAD_ZONE = 0.05;
        public static int TESTING_ANALOG_SMOOTHING_LEVEL = 25;
    }

    // This doesn't support  movement yet, but *does* implement the TargetAcquisition interface
    public static class Component extends Mechanism implements TargetAcquisition {

        private final HubServo yaw, pitch;
        private final TargetAcquisition camera;

        // Things I want the gimbal to do:
        // Track a target when it's visible (as it moves, follow it, unless you can't
        // Scan when it can't find a target (what 'scan' means is still an implementation question)
        // Scan in the direction where it thinks a target might be? This one is iffy.
        // Indicate where a target is currently located, offset from the Vision subsystem/component
        public Component(TargetAcquisition vision) {
            super("Gimbal", Scheduler.getDefault());
            yaw = new HubServo(Config.USB_ID, Config.YAW_SERVO_PORT);
            pitch = new HubServo(Config.USB_ID, Config.PITCH_SERVO_PORT);
            yaw.setReversed(Config.Yaw.flip);
            pitch.setReversed(Config.Pitch.flip);
            camera = vision;
        }

        @Override
        public double getDistance() {
            if (camera.getDistance() < 0) {
                return -1;
            }
            // In Decode (2025) we use the fixed target height and camera height to calculate the
            // distance pretty accurately. To do that with a gimbal, you have to get the *accurate*
            // angle, because the camera itself doesn't know the gimbal angle.
            double angle = getVerticalPosition();
            // tan(angle) = height / distance
            return (Config.TARGET_HEIGHT - Config.CAMERA_HEIGHT) / Math.tan(Math.toRadians(angle));
        }

        @Override
        public double getHorizontalPosition() {
            double fromCamera = camera.getHorizontalPosition();
            double gimbalPosition = yaw.getPosition();
            return Config.Yaw.Adjust(fromCamera, gimbalPosition);
        }

        @Override
        public double getVerticalPosition() {
            double fromCamera = camera.getVerticalPosition();
            double gimbalPosition = pitch.getPosition();
            return Config.Pitch.Adjust(fromCamera, gimbalPosition);
        }

        private enum State {
            Unknown,
            Following,
            Scanning,
            Scanning_Start,
        }

        State curState = State.Unknown;
        ElapsedTime stateTimer = new ElapsedTime();

        // TODO: Implement either target tracking when a target is visible
        //   OR
        //  target *scanning* when a target isn't visible
        public void periodic() {
            double vert = camera.getVerticalPosition();
            double horiz = camera.getHorizontalPosition();
            if (Double.isNaN(vert) || Double.isNaN(horiz)) {
                if (curState == State.Unknown || curState == State.Following) {
                    curState = State.Scanning_Start;
                    stateTimer.reset();
                }
            } else {
                if (curState != State.Following) {
                    curState = State.Following;
                    stateTimer.reset();
                }
            }
            switch (curState) {
                case Following:
                    // Move the gimbal to try to center the target
                    break;
                case Scanning_Start:
                    // Initialize our scan positions (from where we are currently)
                    setPos(0, 0);
                    break;
                case Scanning:
                    // I want the stall detector, so I can see when the camera has hit it's edge
                    break;
                default:
                    break;
            }
        }

        private void setPos(double X, double Y) {}
    }

    // TODO: Make an opmode to *calculate* the angle spread of the gimbal
    //  This would entail watching a target and moving the gimbal to measure how far
    //  each movement changes the position of the target.

    @Utility(name = "Testing", group = "Gimbal")
    public static class TestingOpMode extends PeriodicOpMode {

        private boolean anyButtonsReleased() {
            return (
                gamepad1.getEastFaceButtonReleased() ||
                gamepad1.getWestFaceButtonReleased() ||
                gamepad1.getNorthFaceButtonReleased() ||
                gamepad1.getSouthFaceButtonReleased()
            );
        }

        // Hardware
        HubServo yaw, pitch;
        Gamepad gamepad1;

        // State
        double yawPos = Config.Yaw.init;
        double pitchPos = Config.Pitch.init;
        boolean digitalMode = true;

        MedianFilter yawAvg = new MedianFilter(Config.TESTING_ANALOG_SMOOTHING_LEVEL);
        MedianFilter pitchAvg = new MedianFilter(Config.TESTING_ANALOG_SMOOTHING_LEVEL);

        public TestingOpMode(GlobalContext gc) {
            gamepad1 = gc.g1;
            yaw = new HubServo(Config.USB_ID, Config.YAW_SERVO_PORT);
            pitch = new HubServo(Config.USB_ID, Config.PITCH_SERVO_PORT);
            yaw.setReversed(Config.Yaw.flip);
            yaw.setPosition(yawPos);
            pitch.setReversed(Config.Pitch.flip);
            pitch.setPosition(pitchPos);
            digitalMode = true;
        }

        @Override
        public void periodic() {
            if (digitalMode) {
                if (gamepad1.getDpadUpButtonPressed()) {
                    pitchPos += Config.TESTING_DELTA;
                } else if (gamepad1.getDpadDownButtonPressed()) {
                    pitchPos -= Config.TESTING_DELTA;
                } else if (gamepad1.getDpadRightButtonPressed()) {
                    yawPos += Config.TESTING_DELTA;
                } else if (gamepad1.getDpadLeftButtonPressed()) {
                    yawPos -= Config.TESTING_DELTA;
                }
            } else {
                // Smooth the values from the sticks a bit
                yawPos = yawAvg.calculate(Config.Yaw.Stick(gamepad1.getRightX()));
                pitchPos = pitchAvg.calculate(Config.Pitch.Stick(gamepad1.getLeftY()));
            }
            if (anyButtonsReleased()) {
                yawPos = Config.Yaw.init;
                pitchPos = Config.Pitch.init;
                digitalMode = !digitalMode;
            }

            yawPos = Config.Yaw.Clip(yawPos);
            pitchPos = Config.Pitch.Clip(pitchPos);

            yaw.setPosition(yawPos);
            pitch.setPosition(pitchPos);
            if (digitalMode) {
                System.out.println("dpad up/dn: Pitch, dpad lt/rt: Yaw");
                System.out.println("Press a button to switch to analog mode");
            } else {
                System.out.println("Left stick pitch (u/d), Right stick yaw (l/r)");
                System.out.println("Press a button to switch to digital mode");
            }
            System.out.printf("Yaw %f%n", yawPos);
            System.out.printf("Pitch %f%n", pitchPos);
        }
    }
}
