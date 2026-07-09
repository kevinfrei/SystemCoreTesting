package first.robot.opmode;

import first.robot.Robot;
import org.wpilib.command3.Scheduler;
import org.wpilib.command3.button.CommandGamepad;
import org.wpilib.opmode.OpMode;
import org.wpilib.opmode.Teleop;

/*
 I'm trying to figure out how one might use the new Commands V3
 because honestly, coroutines make the most sense for this sort of thing...
 */
@Teleop(name = "Command Trial")
public class CommandTrial implements OpMode {

    Robot.DualDriveBaseBot bot;
    CommandGamepad gp;
    boolean isEnabled;

    public CommandTrial(Robot r) {
        bot = new Robot.DualDriveBaseBot(r);
        gp = new CommandGamepad(0);
        isEnabled = false;
    }

    @Override
    public void start() {
        // This isn't init. That's the constructor. This run as soon as someone hits 'enable'.
        isEnabled = true;
    }

    @Override
    public void periodic() {
        if (isEnabled) {
            // Is this all we have to do in the periodic function?
            Scheduler.getDefault().run();
        }
    }

    @Override
    public void end() {
        // No idea what needs done here, but seems sensible:
        Scheduler.getDefault().cancelAll();
        isEnabled = false;
    }
}
