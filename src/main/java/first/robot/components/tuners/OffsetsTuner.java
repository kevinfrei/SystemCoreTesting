package first.robot.components.tuners;

import com.pedropathing.geometry.Pose;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * Tuning OpMode to get the min and max encoder values for swerve pods
 *
 * @author Kabir Goyal
 * //
 * class AnalogMinMaxTuner extends Tuning {
 * <p>
 * //populate the below with your names for the servos and encoders
 * public String[] encoderNames = {
 * "leftFrontEncoder",
 * "rightFrontEncoder",
 * "leftBackEncoder",
 * "rightBackEncoder",
 * };
 * public AnalogInput[] encoders = new AnalogInput[encoderNames.length];
 * public double[] minVoltages = new double[encoderNames.length];
 * public double[] maxVoltages = new double[encoderNames.length];
 * <p>
 * public List<LynxModule> lynxModules; //js to improve loop times a bit yk
 * <p>
 * public void start() {}
 * @author Kabir Goyal
 * <p>
 * //
 * class SwerveOffsetsTest extends OpMode {
 * <p>
 * boolean debugStringEnabled = false;
 * @author Kabir Goyal
 * <p>
 * //
 * class SwerveTurnTest extends OpMode {
 * <p>
 * boolean debugStringEnabled = false;
 * @author Havish Sripada - 12808 RevAmped Robotics
 * @author Baron Henderson
 * @Override public void init_loop() {
 * telemetryM.debug(
 * "Press START. Then, Spin each pod slowly for 4 to 5 full rotations.\n" +
 * "The OpMode will keep track of the min and max voltages seen so far and print them to telemetry."
 * );
 * telemetryM.update(telemetry);
 * }
 * @Override public void init() {
 * lynxModules = hardwareMap.getAll(LynxModule.class);
 * for (LynxModule hub : lynxModules) {
 * hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
 * }
 * <p>
 * for (int i = 0; i < encoders.length; i++) {
 * encoders[i] = hardwareMap.get(AnalogInput.class, encoderNames[i]);
 * minVoltages[i] = 5; //bigger value than should ever be read
 * }
 * }
 * <p>
 * /**
 * This runs the OpMode, updating the Follower as well as printing out the debug statements to
 * the Telemetry, as well as the FTC Dashboard.
 * //
 * @Override public void loop() {
 * for (LynxModule hub : lynxModules) {
 * hub.clearBulkCache();
 * }
 * <p>
 * telemetryM.debug(
 * "Spin each pod slowly for 4 to 5 full rotations.\n" +
 * "The OpMode will keep track of the min and max voltages seen so far and print them to telemetry.\n\n"
 * );
 * <p>
 * for (int i = 0; i < encoders.length; i++) {
 * double currentVoltage = encoders[i].getVoltage();
 * minVoltages[i] = Math.min(minVoltages[i], currentVoltage);
 * maxVoltages[i] = Math.max(maxVoltages[i], currentVoltage);
 * telemetryM.addData(encoderNames[i] + "min value:", minVoltages[i]);
 * telemetryM.addData(encoderNames[i] + "max value:", maxVoltages[i]);
 * telemetryM.addLine("");
 * }
 * <p>
 * telemetryM.update();
 * }
 * }
 * <p>
 * /**
 * This is the SwerveOffsetsTest
 * You should use this to check how good your swerve angle offsets are and if your motor directions are correct
 * @Override public void init() {}
 * <p>
 * /** This initializes the PoseUpdater, the drive motors, and the Panels telemetry. * //
 * @Override public void init_loop() {
 * if (gamepad1.getSouthFaceButtonPressed() || gamepad2.getSouthFaceButtonPressed()) {
 * debugStringEnabled = !debugStringEnabled;
 * }
 * <p>
 * telemetryM.debug(
 * "This OpMode will run all four swerve pods in the direction they think is forward" +
 * "\nensure your bot is not on the ground while running"
 * );
 * telemetryM.debug(
 * "Drivetrain debug string " +
 * (debugStringEnabled ? "enabled" : "disabled") +
 * " (press gamepad a to toggle)"
 * );
 * telemetryM.update(telemetry);
 * follower.update();
 * drawCurrent();
 * }
 * @Override public void start() {
 * follower.startTeleopDrive();
 * follower.update();
 * }
 * <p>
 * /**
 * This updates the robot's pose estimate, the simple drive, and updates the
 * Panels telemetry with the robot's position as well as draws the robot's position.
 * //
 * @Override public void loop() {
 * if (gamepad1.getSouthFaceButtonPressed() || gamepad2.getSouthFaceButtonPressed()) {
 * debugStringEnabled = !debugStringEnabled;
 * }
 * <p>
 * follower.setTeleOpDrive(0.25, 0, 0, true);
 * follower.update();
 * <p>
 * if (debugStringEnabled) {
 * telemetryM.debug("Drivetrain Debug String:\n" + follower.getDrivetrain().debugString());
 * }
 * telemetryM.update(telemetry);
 * <p>
 * drawCurrentAndHistory();
 * }
 * }
 * <p>
 * /**
 * This is the SwerveTurnTest
 * You should use this to check your encoder directions and x/y pod offsets
 * @Override public void init() {}
 * <p>
 * /** This initializes the PoseUpdater, the drive motors, and the Panels telemetry. * //
 * @Override public void init_loop() {
 * if (gamepad1.getSouthFaceButtonPressed() || gamepad2.getSouthFaceButtonPressed()) {
 * debugStringEnabled = !debugStringEnabled;
 * }
 * <p>
 * telemetryM.debug(
 * "This OpMode will run all four swerve pods in their turning direction (perpendicular to the center of the robot) " +
 * "\nrun this once off the ground to check servo directions and motor directions before testing on the ground"
 * );
 * telemetryM.debug(
 * "Drivetrain debug string " +
 * (debugStringEnabled ? "enabled" : "disabled") +
 * " (press gamepad a to toggle)"
 * );
 * telemetryM.update(telemetry);
 * follower.update();
 * drawCurrent();
 * }
 * @Override public void start() {
 * follower.startTeleopDrive();
 * follower.update();
 * }
 * <p>
 * /**
 * This updates the robot's pose estimate, the simple drive, and updates the
 * Panels telemetry with the robot's position as well as draws the robot's position.
 * //
 * @Override public void loop() {
 * if (gamepad1.getSouthFaceButtonPressed() || gamepad2.getSouthFaceButtonPressed()) {
 * debugStringEnabled = !debugStringEnabled;
 * }
 * <p>
 * follower.setTeleOpDrive(0, 0, 0.25, true);
 * follower.update();
 * <p>
 * if (debugStringEnabled) {
 * telemetryM.debug("Drivetrain Debug String:\n" + follower.getDrivetrain().debugString());
 * }
 * telemetryM.update(telemetry);
 * <p>
 * drawCurrentAndHistory();
 * }
 * }
 * <p>
 * /**
 * This is the OffsetsTuner OpMode. This tracks the movement of the robot as it turns 180 degrees,
 * and calculates what the robot's strafeX and forwardY offsets should be. Ensure that your strafeX and forwardY offsets
 * are set to 0 before running this OpMode. After running, input the displayed offsets into your localizer constants.
 */
@Utility(group = "Loc Tuning")
public class OffsetsTuner extends Tuning {

    public OffsetsTuner(Robot r) {
        super(r);
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
        follower.update();
        drawCurrent();
    }

    /**
     * This initializes the PoseUpdater as well as the Panels telemetry.
     */
    @Override
    public void init_loop() {
        telemetryM.debug(
            "Prerequisite: Make sure both your offsets are set to 0 in your localizer constants."
        );
        telemetryM.debug(
            "Turn your robot " +
                Math.PI +
                " radians. Your offsets in inches will be shown on the telemetry."
        );
        telemetryM.update(telemetry);

        drawCurrent();
    }

    /**
     * This updates the robot's pose estimate, and updates the Panels telemetry with the
     * calculated offsets and draws the robot.
     */
    @Override
    public void loop() {
        follower.update();

        telemetryM.debug("Total Angle: " + follower.getTotalHeading());

        telemetryM.debug(
            "The following values are the offsets in inches that should be applied to your localizer."
        );
        telemetryM.debug("strafeX: " + (72.0 - follower.getPose().getX()) / 2.0);
        telemetryM.debug("forwardY: " + (72.0 - follower.getPose().getY()) / 2.0);
        telemetryM.update(telemetry);

        drawCurrentAndHistory();
    }
}
