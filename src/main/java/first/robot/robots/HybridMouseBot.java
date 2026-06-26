package first.robot.robots;

import first.robot.GlobalContext;
import first.robot.components.Gimbal;
import first.robot.components.HybridDriveBase;
import first.robot.components.LimelightCameraTargeting;
import org.wpilib.hardware.imu.OnboardIMU;

public class HybridMouseBot {

    public final GlobalContext globalContext;
    public final HybridDriveBase driveBase;
    public final Gimbal.Component gimbal;
    public final LimelightCameraTargeting ll;
    public final OnboardIMU imu = new OnboardIMU(OnboardIMU.MountOrientation.LANDSCAPE);

    public HybridMouseBot(GlobalContext r) {
        globalContext = r;
        ll = new LimelightCameraTargeting();
        gimbal = new Gimbal.Component(ll);
        driveBase = new HybridDriveBase();
    }
}
