package first.robot.components.tuners;

import com.pedropathing.geometry.Pose;
import first.robot.Robot;
import org.wpilib.opmode.OpMode;
import org.wpilib.opmode.Utility;

/**
 * This is the LocalizationTest OpMode. This is basically just a simple drive attached to a
 * PoseUpdater. The OpMode will print out the robot's pose to telemetry as well as draw the robot.
 * You should use this to check the robot's localization.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Baron Henderson - 20077 The Indubitables
 * @author Kabir Goyal
 * @version 1.0, 5/6/2024
 */
@Utility(group = "Loc Tuning")
public class LocalizationTest extends Tuning {

    boolean debugStringEnabled = false;

    public LocalizationTest(Robot r) {
        super(r);
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
    }

    /**
     * This initializes the PoseUpdater, the drive motors, and the Panels telemetry.
     */
    @Override
    public void init_loop() {
        if (gamepad1.getSouthFaceButtonPressed() || gamepad2.getSouthFaceButtonPressed()) {
            debugStringEnabled = !debugStringEnabled;
        }

        telemetryM.debug(
            "This will print your robot's position to telemetry while " +
                "allowing robot control through a basic drive on gamepad 1."
        );
        telemetryM.debug(
            "Drivetrain debug string " +
                (debugStringEnabled ? "enabled" : "disabled") +
                " (press gamepad a to toggle)"
        );
        telemetryM.update(telemetry);
        follower.update();
        drawCurrent();
    }

    @Override
    public void start() {
        super.start();
        follower.startTeleopDrive();
        follower.update();
    }

    /**
     * This updates the robot's pose estimate, the simple drive, and updates the
     * Panels telemetry with the robot's position as well as draws the robot's position.
     */
    @Override
    public void loop() {
        if (gamepad1.getSouthFaceButtonPressed() || gamepad2.getSouthFaceButtonPressed()) {
            debugStringEnabled = !debugStringEnabled;
        }

        follower.setTeleOpDrive(
            -gamepad1.getLeftY(),
            -gamepad1.getLeftX(),
            -gamepad1.getRightX(),
            true
        );
        follower.update();

        telemetryM.debug("x:" + follower.getPose().getX());
        telemetryM.debug("y:" + follower.getPose().getY());
        telemetryM.debug("heading:" + follower.getPose().getHeading());
        telemetryM.debug("total heading:" + follower.getTotalHeading());
        if (debugStringEnabled) {
            telemetryM.debug("Drivetrain Debug String:\n" + follower.getDrivetrain().debugString());
        }
        telemetryM.update(telemetry);

        drawCurrentAndHistory();
    }
}
