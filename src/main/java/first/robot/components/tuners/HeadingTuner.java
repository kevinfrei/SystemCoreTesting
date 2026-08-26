package first.robot.components.tuners;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * This is the Heading PIDF Tuner OpMode. It will keep the robot in place.
 * The user should try to turn the robot to test the PIDF and adjust the PIDF values accordingly.
 * It will try to keep the robot at a constant heading while the user tries to turn it.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/12/2024
 */
@Utility(group = "Manual Tuning")
public class HeadingTuner extends Tuning {

    public static double DISTANCE = 40;
    private boolean forward = true;

    private Path forwards;
    private Path backwards;

    public HeadingTuner(Robot r) {
        super(r);
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
    }

    /**
     * This initializes the Follower and creates the forward and backward Paths. Additionally, this
     * initializes the Panels telemetry.
     */
    @Override
    public void init_loop() {
        telemetryM.debug("This will activate the heading PIDF(s).");
        telemetryM.debug(
            "The robot will try to stay at a constant heading while you try to turn it."
        );
        telemetryM.debug("You can adjust the PIDF values to tune the robot's heading PIDF(s).");
        telemetryM.update(telemetry);
        follower.update();
        drawCurrent();
    }

    @Override
    public void start() {
        super.start();
        follower.deactivateAllPIDFs();
        follower.activateHeading();
        forwards = new Path(new BezierLine(new Pose(72, 72), new Pose(DISTANCE + 72, 72)));
        forwards.setConstantHeadingInterpolation(0);
        backwards = new Path(new BezierLine(new Pose(DISTANCE + 72, 72), new Pose(72, 72)));
        backwards.setConstantHeadingInterpolation(0);
        follower.followPath(forwards);
    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to
     * the Telemetry, as well as the Panels.
     */
    @Override
    public void loop() {
        follower.update();
        drawCurrentAndHistory();

        if (!follower.isBusy()) {
            if (forward) {
                forward = false;
                follower.followPath(backwards);
            } else {
                forward = true;
                follower.followPath(forwards);
            }
        }

        telemetryM.debug("Turn the robot manually to test the Heading PIDF(s).");
        telemetryM.addData("Zero Line", 0);
        telemetryM.addData("Error", follower.errorCalculator.getHeadingError());
        telemetryM.update(telemetry);
    }
}
