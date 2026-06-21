package first.robot.opmode;

import first.robot.Robot;
import first.robot.components.DriveBase;
import org.wpilib.command2.CommandScheduler;
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
        // Schedulee commands?
        // CommandScheduler.getInstance().schedule(/* TODO */);
        // CommandScheduler.getInstance().registerSubsystem(Robot.drivebase);
        CommandScheduler.getInstance().enable();
    }

    @Override
    public void periodic() {
        // Is this all we have to do in the periodic function?
        CommandScheduler.getInstance().run();
    }

    @Override
    public void end() {
        // No idea what needs done here, but seems sensible:
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().unregisterAllSubsystems();
        CommandScheduler.getInstance().disable();
    }
}
