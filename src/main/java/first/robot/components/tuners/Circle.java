package first.robot.components.tuners;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * This is the Circle autonomous OpMode. It runs the robot in a PathChain that's actually not quite
 * a circle, but some Bezier curves that have control points set essentially in a square. However,
 * it turns enough to tune your centripetal force correction and some of your heading. Some lag in
 * heading is to be expected.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/12/2024
 */
@Utility(group = "Test Tuning")
public class Circle extends Tuning {

    public static double RADIUS = 10;
    private PathChain circle;

    public Circle(Robot r) {
        super(r);
    }

    @Override
    public void start() {
        super.start();
        circle = follower
            .pathBuilder()
            .addPath(
                new BezierCurve(
                    new Pose(72, 72),
                    new Pose(RADIUS + 72, 72),
                    new Pose(RADIUS + 72, RADIUS + 72)
                )
            )
            .setHeadingInterpolation(HeadingInterpolator.facingPoint(72, RADIUS + 72))
            .addPath(
                new BezierCurve(
                    new Pose(RADIUS + 72, RADIUS + 72),
                    new Pose(RADIUS + 72, 2 * RADIUS + 72),
                    new Pose(72, 2 * RADIUS + 72)
                )
            )
            .setHeadingInterpolation(HeadingInterpolator.facingPoint(72, RADIUS + 72))
            .addPath(
                new BezierCurve(
                    new Pose(72, 2 * RADIUS + 72),
                    new Pose(-RADIUS + 72, 2 * RADIUS + 72),
                    new Pose(-RADIUS + 72, RADIUS + 72)
                )
            )
            .setHeadingInterpolation(HeadingInterpolator.facingPoint(72, RADIUS + 72))
            .addPath(
                new BezierCurve(
                    new Pose(-RADIUS + 72, RADIUS + 72),
                    new Pose(-RADIUS + 72, 72),
                    new Pose(72, 72)
                )
            )
            .setHeadingInterpolation(HeadingInterpolator.facingPoint(72, RADIUS + 72))
            .build();
        follower.followPath(circle);
    }

    @Override
    public void init_loop() {
        telemetryM.debug(
            "This will run in a roughly circular shape of radius " +
                RADIUS +
                ", starting on the right-most edge. "
        );
        telemetryM.debug(
            "So, make sure you have enough space to the left, front, and back to run the OpMode."
        );
        telemetryM.debug(
            "It will also continuously face the center of the circle to test your heading and centripetal correction."
        );
        telemetryM.update(telemetry);
        follower.update();
        drawCurrent();
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to
     * the Telemetry, as well as the FTC Dashboard.
     */
    @Override
    public void loop() {
        follower.update();
        drawCurrentAndHistory();

        if (follower.atParametricEnd()) {
            follower.followPath(circle);
        }
    }
}
