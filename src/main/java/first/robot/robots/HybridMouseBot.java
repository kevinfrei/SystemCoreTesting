package first.robot.robots;

import com.pedropathing.follower.Follower;
import first.robot.GlobalContext;
import first.robot.components.Gimbal;
import first.robot.components.HybridDriveBase;
import first.robot.components.LimelightCameraTargeting;
import org.wpilib.driverstation.Alliance;
import org.wpilib.hardware.imu.OnboardIMU;

public class HybridMouseBot {

    public final GlobalContext globalContext;
    public final HybridDriveBase.Component driveBase;
    public final Gimbal.Component gimbal;
    public final LimelightCameraTargeting ll;
    public final OnboardIMU imu = new OnboardIMU(OnboardIMU.MountOrientation.LANDSCAPE);

    public HybridMouseBot(GlobalContext r, Alliance all) {
        globalContext = r;
        ll = new LimelightCameraTargeting();
        gimbal = new Gimbal.Component(ll);
        driveBase = new HybridDriveBase.Component(gimbal, all);
    }
}
