package first.robot.components.tuners;

import com.pedropathing.geometry.Pose;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * This is the ForwardTuner OpMode. This tracks the forward movement of the robot and displays the
 * necessary ticks to inches multiplier. This displayed multiplier is what's necessary to scale the
 * robot's current distance in ticks to the specified distance in inches. So, to use this, run the
 * tuner, then pull/push the robot to the specified distance using a ruler on the ground. When you're
 * at the end of the distance, record the ticks to inches multiplier. Feel free to run multiple trials
 * and average the results. Then, input the multiplier into the forward ticks to inches in your
 * localizer of choice.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 5/6/2024
 */
@Utility(group = "Loc Tuning")
public class ForwardTuner extends Tuning {

    public static double DISTANCE = 48;

    public ForwardTuner(Robot r) {
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
            "Pull your robot forward " +
                DISTANCE +
                " inches. Your forward ticks to inches will be shown on the telemetry."
        );
        telemetryM.update(telemetry);
        drawCurrent();
    }

    /**
     * This updates the robot's pose estimate, and updates the Panels telemetry with the
     * calculated multiplier and draws the robot.
     */
    @Override
    public void loop() {
        follower.update();

        telemetryM.debug("Distance Moved: " + follower.getPose().getX());
        telemetryM.debug(
            "The multiplier will display what your forward ticks to inches should be to scale your current distance to " +
                DISTANCE +
                " inches."
        );
        telemetryM.debug(
            "Multiplier: " +
                DISTANCE /
                    (follower.getPose().getX() /
                        follower.getPoseTracker().getLocalizer().getForwardMultiplier())
        );
        telemetryM.update(telemetry);

        drawCurrentAndHistory();
    }
}
