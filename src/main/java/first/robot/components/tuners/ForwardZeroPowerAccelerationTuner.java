package first.robot.components.tuners;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import first.robot.Robot;
import java.util.ArrayList;
import org.wpilib.opmode.Utility;

/**
 * This is the ForwardZeroPowerAccelerationTuner autonomous follower OpMode. This runs the robot
 * forward until a specified velocity is achieved. Then, the robot cuts power to the motors, setting
 * them to zero power. The deceleration, or negative acceleration, is then measured until the robot
 * stops. The accelerations across the entire time the robot is slowing down is then averaged and
 * that number is then printed. This is used to determine how the robot will decelerate in the
 * forward direction when power is cut, making the estimations used in the calculations for the
 * drive Vector more accurate and giving better braking at the end of Paths.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Baron Henderson - 20077 The Indubitables
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/13/2024
 */
@Utility(name = "Fwd 0-Pow Accel Tuner", group = "Automatic Tuning")
public class ForwardZeroPowerAccelerationTuner extends Tuning {

    private final ArrayList<Double> accelerations = new ArrayList<>();
    public static double VELOCITY = 30;

    private double previousVelocity;
    private long previousTimeNano;

    private boolean stopping;
    private boolean end;

    public ForwardZeroPowerAccelerationTuner(Robot r) {
        super(r);
    }

    @Override
    public void init() {
        follower.setStartingPose(new Pose(72, 72));
    }

    /**
     * This initializes the drive motors as well as the Panels telemetryM.
     */
    @Override
    public void init_loop() {
        telemetryM.debug(
            "The robot will run forward until it reaches " + VELOCITY + " inches per second."
        );
        telemetryM.debug("Then, it will cut power from the drivetrain and roll to a stop.");
        telemetryM.debug("Make sure you have enough room.");
        telemetryM.debug(
            "After stopping, the forward zero power acceleration (natural deceleration) will be displayed."
        );
        telemetryM.debug("Press B on Gamepad 1 to stop.");
        telemetryM.update(telemetry);
        follower.update();
        drawCurrent();
    }

    /**
     * This starts the OpMode by setting the drive motors to run forward at full power.
     */
    @Override
    public void start() {
        super.start();
        follower.startTeleopDrive(false);
        follower.update();
        follower.setTeleOpDrive(1, 0, 0, true);
    }

    /**
     * This runs the OpMode. At any point during the running of the OpMode, pressing B on
     * game pad 1 will stop the OpMode. When the robot hits the specified velocity, the robot will
     * record its deceleration / negative acceleration until it stops. Then, it will average all the
     * recorded deceleration / negative acceleration and print that value.
     */
    @Override
    public void loop() {
        if (gamepad1.getEastFaceButtonPressed()) {
            stopRobot();
            requestOpModeStop();
        }

        follower.update();
        drawCurrentAndHistory();

        Vector heading = new Vector(1.0, follower.getPose().getHeading());
        if (!end) {
            if (!stopping) {
                if (follower.getVelocity().dot(heading) > VELOCITY) {
                    previousVelocity = follower.getVelocity().dot(heading);
                    previousTimeNano = System.nanoTime();
                    stopping = true;
                    follower.setTeleOpDrive(0, 0, 0, true);
                }
            } else {
                double currentVelocity = follower.getVelocity().dot(heading);
                accelerations.add(
                    (currentVelocity - previousVelocity) /
                        ((System.nanoTime() - previousTimeNano) / Math.pow(10.0, 9))
                );
                previousVelocity = currentVelocity;
                previousTimeNano = System.nanoTime();
                if (currentVelocity < follower.getConstraints().getVelocityConstraint()) {
                    end = true;
                }
            }
        } else {
            double average = 0;
            for (double acceleration : accelerations) {
                average += acceleration;
            }
            average /= accelerations.size();

            telemetryM.debug("Forward Zero Power Acceleration (Deceleration): " + average);
            telemetryM.debug("\n");
            telemetryM.debug(
                "Press A to set the Forward Zero Power Acceleration temporarily (while robot remains on)."
            );
            telemetryM.update(telemetry);

            if (gamepad1.getSouthFaceButtonPressed()) {
                follower.getConstants().setForwardZeroPowerAcceleration(average);
                String message = "Forward Zero Power Acceleration: " + average;
                changes.add(message);
            }
        }
    }
}
