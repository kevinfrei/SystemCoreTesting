package first.robot.components.tuners;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * This is the Triangle autonomous OpMode.
 * It runs the robot in a triangle, with the starting point being the bottom-middle point.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @author Samarth Mahapatra - 1002 CircuitRunners Robotics Surge
 * @version 1.0, 12/30/2024
 */
@Utility(group = "Test Tuning")
public class Triangle extends Tuning {

    private final Pose startPose = new Pose(72, 72, Math.toRadians(0));
    private final Pose interPose = new Pose(24 + 72, -24 + 72, Math.toRadians(90));
    private final Pose endPose = new Pose(24 + 72, 24 + 72, Math.toRadians(45));

    private PathChain triangle;

    public Triangle(Robot r) {
        super(r);
    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to
     * the Telemetry, as well as the Panels.
     */
    @Override
    public void loop() {
        follower.update();
        drawCurrentAndHistory();

        if (follower.atParametricEnd()) {
            follower.followPath(triangle, true);
        }
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
    }

    @Override
    public void init_loop() {
        telemetryM.debug(
            "This will run in a roughly triangular shape, starting on the bottom-middle point."
        );
        telemetryM.debug(
            "So, make sure you have enough space to the left, front, and right to run the OpMode."
        );
        telemetryM.update(telemetry);
        follower.update();
        drawCurrent();
    }

    /**
     * Creates the PathChain for the "triangle".
     */
    @Override
    public void start() {
        super.start();
        follower.setStartingPose(startPose);

        triangle = follower
            .pathBuilder()
            .addPath(new BezierLine(startPose, interPose))
            .setLinearHeadingInterpolation(startPose.getHeading(), interPose.getHeading())
            .addPath(new BezierLine(interPose, endPose))
            .setLinearHeadingInterpolation(interPose.getHeading(), endPose.getHeading())
            .addPath(new BezierLine(endPose, startPose))
            .setLinearHeadingInterpolation(endPose.getHeading(), startPose.getHeading())
            .build();

        follower.followPath(triangle);
    }
}
