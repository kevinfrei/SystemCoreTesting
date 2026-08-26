package first.robot.components.tuners;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * This is the Centripetal Tuner OpMode. It runs the robot in a specified distance
 * forward and to the left. On reaching the end of the forward Path, the robot runs the backward
 * Path the same distance back to the start. Rinse and repeat! This is good for testing a variety
 * of Vectors, like the drive Vector, the translational Vector, the heading Vector, and the
 * centripetal Vector.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/13/2024
 */
@Utility(group = "Manual Tuning")
public class CentripetalTuner extends Tuning {

    public static double DISTANCE = 20;
    private boolean forward = true;

    private Path forwards;
    private Path backwards;

    public CentripetalTuner(Robot r) {
        super(r);
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
    }

    /**
     * This initializes the Follower and creates the forward and backward Paths.
     * Additionally, this initializes the Panels telemetry.
     */
    @Override
    public void init_loop() {
        telemetryM.debug(
            "This will run the robot in a curve going " +
                DISTANCE +
                " inches to the left and the same number of inches forward."
        );
        telemetryM.debug("The robot will go continuously along the path.");
        telemetryM.debug("Make sure you have enough room.");
        telemetryM.update(telemetry);
        follower.update();
        drawCurrent();
    }

    @Override
    public void start() {
        super.start();
        follower.activateAllPIDFs();
        forwards = new Path(
            new BezierCurve(
                new Pose(72, 72),
                new Pose(Math.abs(DISTANCE) + 72, 72),
                new Pose(Math.abs(DISTANCE) + 72, DISTANCE + 72)
            )
        );
        backwards = new Path(
            new BezierCurve(
                new Pose(Math.abs(DISTANCE) + 72, DISTANCE + 72),
                new Pose(Math.abs(DISTANCE) + 72, 72),
                new Pose(72, 72)
            )
        );

        backwards.setTangentHeadingInterpolation();
        backwards.reverseHeadingInterpolation();

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

        telemetryM.debug("Driving away from the origin along the curve?: " + forward);
        telemetryM.update(telemetry);
    }
}
