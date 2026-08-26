package first.robot.components.tuners;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * This is the Translational PIDF Tuner OpMode. It will keep the robot in place.
 * The user should push the robot laterally to test the PIDF and adjust the PIDF values accordingly.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/12/2024
 */
@Utility(group = "Manual Tuning")
public class TranslationalTuner extends Tuning {

    public static double DISTANCE = 40;
    private boolean forward = true;

    private Path forwards;
    private Path backwards;

    public TranslationalTuner(Robot r) {
        super(r);
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
    }

    /**
     * This initializes the Follower and creates the forward and backward Paths.
     */
    @Override
    public void init_loop() {
        telemetryM.debug("This will activate the translational PIDF(s)");
        telemetryM.debug("The robot will try to stay in place while you push it laterally.");
        telemetryM.debug(
            "You can adjust the PIDF values to tune the robot's translational PIDF(s)."
        );
        telemetryM.update(telemetry);
        follower.update();
        drawCurrent();
    }

    @Override
    public void start() {
        super.start();
        follower.deactivateAllPIDFs();
        follower.activateTranslational();
        forwards = new Path(new BezierLine(new Pose(72, 72), new Pose(DISTANCE + 72, 72)));
        forwards.setConstantHeadingInterpolation(0);
        backwards = new Path(new BezierLine(new Pose(DISTANCE + 72, 72), new Pose(72, 72)));
        backwards.setConstantHeadingInterpolation(0);
        follower.followPath(forwards);
    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to the Telemetry
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

        telemetryM.debug("Push the robot laterally to test the Translational PIDF(s).");
        telemetryM.addData("Zero Line", 0);
        telemetryM.addData(
            "Error X",
            follower.errorCalculator.getTranslationalError().getXComponent()
        );
        telemetryM.addData(
            "Error Y",
            follower.errorCalculator.getTranslationalError().getYComponent()
        );
        telemetryM.update(telemetry);
    }
}
