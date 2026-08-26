package first.robot.components.tuners;

import com.pedropathing.geometry.Pose;
import first.robot.Robot;
import org.wpilib.opmode.Utility;

/**
 * This is the TurnTuner OpMode. This tracks the turning movement of the robot and displays the
 * necessary ticks to inches multiplier. This displayed multiplier is what's necessary to scale the
 * robot's current angle in ticks to the specified angle in radians. So, to use this, run the
 * tuner, then pull/push the robot to the specified angle using a protractor or lines on the ground.
 * When you're at the end of the angle, record the ticks to inches multiplier. Feel free to run
 * multiple trials and average the results. Then, input the multiplier into the turning ticks to
 * radians in your localizer of choice.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 5/6/2024
 */
@Utility(group = "Loc Tuning")
public class TurnTuner extends Tuning {

    public static double ANGLE = 2 * Math.PI;

    public TurnTuner(Robot r) {
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
            "Turn your robot " +
                ANGLE +
                " radians. Your turn ticks to inches will be shown on the telemetry."
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

        telemetryM.debug("Total Angle: " + follower.getTotalHeading());
        telemetryM.debug(
            "The multiplier will display what your turn ticks to inches should be to scale your current angle to " +
                ANGLE +
                " radians."
        );
        telemetryM.debug(
            "Multiplier: " +
                ANGLE /
                    (follower.getTotalHeading() /
                        follower.getPoseTracker().getLocalizer().getTurningMultiplier())
        );
        telemetryM.update(telemetry);

        drawCurrentAndHistory();
    }
}
