package first.robot.components.tuners;

// import static first.robot.components.tuners.Tuning.changes;
// import static first.robot.components.tuners.Tuning.drawCurrent;
// import static first.robot.components.tuners.Tuning.drawCurrentAndHistory;
// import static first.robot.components.tuners.Tuning.follower;
// import static first.robot.components.tuners.Tuning.stopRobot;

// import com.bylazar.configurables.PanelsConfigurables;
// import com.bylazar.configurables.annotations.Configurable;
// import com.bylazar.configurables.annotations.IgnoreConfigurable;
// import com.bylazar.field.FieldManager;
// import com.bylazar.field.PanelsField;
// import com.bylazar.field.Style;
// import com.bylazar.telemetry.PanelsTelemetry;
// import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.*;
// import com.pedropathing.telemetry.SelectableOpMode;
import com.pedropathing.util.*;
import first.support.Telemetry;
import first.support.TunablePedroBot;
import java.util.ArrayList;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.opmode.OpMode;

/**
 * This is the Tuning class. It contains a selection menu for various tuning OpModes.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 6/26/2025
 */
public abstract class Tuning implements OpMode {

    protected enum State {
        Constructed,
        Initialized,
        Enabled,
        Stopped,
    }

    protected State state;
    protected Gamepad gamepad1, gamepad2;
    protected static Follower follower;

    // This stuff is so very, very dumb. I should go learn how to display a field in
    // AdvantageScope. All the FRC people rave about it. Personally, I really miss Panels.
    protected Telemetry telemetryM;
    protected Telemetry telemetry;

    protected Tuning(TunablePedroBot b) {
        state = State.Constructed;
        gamepad1 = b.getGamepad1();
        gamepad2 = b.getGamepad2();
        follower = b.getFollower();
        follower.setStartingPose(new Pose());
        telemetryM = new Telemetry();
        telemetry = telemetryM; // lol
    }

    public void start() {
        state = State.Enabled;
        poseHistory = follower.getPoseHistory();
    }

    public void periodic() {
        switch (state) {
            case State.Constructed:
                init();
                state = State.Initialized;
                break;
            case State.Initialized:
                init_loop();
                break;
            case State.Enabled:
                loop();
                break;
            default:
                break;
        }
    }

    public void close() {
        state = State.Stopped;
    }

    public abstract void init();

    public abstract void init_loop();

    public abstract void loop();

    // @IgnoreConfigurable
    static PoseHistory poseHistory;

    // @IgnoreConfigurable
    // static TelemetryManager telemetryM;

    // @IgnoreConfigurable
    static ArrayList<String> changes = new ArrayList<>();

    /*
    super("Select a Tuning OpMode", s -> {
        s.folder("Localization", l -> {
            l.add("Localization Test", LocalizationTest::new);
            l.add("Offsets Tuner", OffsetsTuner::new);
            l.add("Forward Tuner", ForwardTuner::new);
            l.add("Lateral Tuner", LateralTuner::new);
            l.add("Turn Tuner", TurnTuner::new);
        });
        s.folder("Automatic", a -> {
            a.add("Forward Velocity Tuner", ForwardVelocityTuner::new);
            a.add("Lateral Velocity Tuner", LateralVelocityTuner::new);
            a.add(
                "Forward Zero Power Acceleration Tuner",
                ForwardZeroPowerAccelerationTuner::new
            );
            a.add(
                "Lateral Zero Power Acceleration Tuner",
                LateralZeroPowerAccelerationTuner::new
            );
            a.add("Predictive Braking Tuner", PredictiveBrakingTuner::new);
        });
        s.folder("Manual", p -> {
            p.add("Translational Tuner", TranslationalTuner::new);
            p.add("Heading Tuner", HeadingTuner::new);
            p.add("Drive Tuner", DriveTuner::new);
            p.add("Centripetal Tuner", CentripetalTuner::new);
        });
        s.folder("Tests", p -> {
            p.add("Line", Line::new);
            p.add("Triangle", Triangle::new);
            p.add("Circle", Circle::new);
        });
        s.folder("Swerve", p -> {
            p.add("Analog Min / Max Tuner", AnalogMinMaxTuner::new);
            p.add("Swerve Offsets Test", SwerveOffsetsTest::new);
            p.add("Swerve Turn Test", SwerveTurnTest::new);
        });
    });
    */

    public static void drawCurrent() {
        try {
            Drawing.drawRobot(follower.getPose());
            Drawing.sendPacket();
        } catch (Exception e) {
            throw new RuntimeException("Drawing failed " + e);
        }
    }

    public static void drawCurrentAndHistory() {
        Drawing.drawPoseHistory(poseHistory);
        drawCurrent();
    }

    /**
     * This creates a full stop of the robot by setting the drive motors to run at 0 power.
     */
    public static void stopRobot() {
        follower.startTeleopDrive(true);
        follower.setTeleOpDrive(0, 0, 0, true);
    }

    protected void requestOpModeStop() {
        // TODO: Something in here?
    }
}
