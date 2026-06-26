package first.robot.helpers;

import org.wpilib.command2.Command;
import org.wpilib.command2.Subsystem;

public class MethodCmd extends Command {

    Runnable run;
    boolean finish;

    private MethodCmd(Runnable cmd, Subsystem... requires) {
        this(cmd, true, requires);
    }

    private MethodCmd(Runnable cmd, boolean forever, Subsystem... requires) {
        run = cmd;
        finish = !forever;
        addRequirements(requires);
    }

    public static MethodCmd once(Runnable cmd, Subsystem... requires) {
        return new MethodCmd(cmd, requires);
    }

    public static MethodCmd forever(Runnable cmd, Subsystem... requires) {
        return new MethodCmd(cmd, true, requires);
    }

    @Override
    public void execute() {
        run.run();
    }

    @Override
    public boolean isFinished() {
        return finish;
    }
}
