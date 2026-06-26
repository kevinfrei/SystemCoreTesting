package first.robot.robots;

import first.robot.GlobalContext;
import first.robot.components.DualA301DriveBase;
import org.wpilib.hardware.imu.OnboardIMU;

public class DualDriveBaseBot {

    private final GlobalContext globalContext;
    private final DualA301DriveBase.Component driveBase;
    public final OnboardIMU imu = new OnboardIMU(OnboardIMU.MountOrientation.LANDSCAPE);

    public DualDriveBaseBot(GlobalContext r) {
        globalContext = r;
        driveBase = new DualA301DriveBase.Component(r);
    }
}
