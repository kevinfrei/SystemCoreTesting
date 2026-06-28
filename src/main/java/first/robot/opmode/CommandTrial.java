package first.robot.opmode;

import org.wpilib.command3.Scheduler;
import org.wpilib.opmode.OpMode;
import org.wpilib.opmode.Teleop;

/*
 I'm trying to figure out how one might use the new Commands V3
 because honestly, coroutines make the most sense for this sort of thing...
 */
@Teleop
public class CommandTrial implements OpMode {

    @Override
    public void start() {
        // Schedule commands?
        // Scheduler.getDefault().
    }

    @Override
    public void periodic() {
        // Is this all we have to do in the periodic function?
        Scheduler.getDefault().run();
    }

    @Override
    public void end() {
        // No idea what needs done here, but seems sensible:
        Scheduler.getDefault().cancelAll();
    }
}
