// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import com.pedropathing.follower.Follower;
import first.robot.components.DualA301DriveBase;
import first.robot.components.Gimbal;
import first.robot.components.HybridDriveBase;
import first.robot.components.LimelightCameraTargeting;
import first.support.TunablePedroBot;
import org.wpilib.driverstation.Alliance;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.framework.OpModeRobot;
import org.wpilib.hardware.imu.OnboardIMU;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class Robot extends OpModeRobot implements TunablePedroBot {

    public Gamepad g1 = new Gamepad(0);
    public Gamepad g2 = new Gamepad(1);

    protected static DualDriveBaseBot dual = null;
    protected static HybridMouseBot mouse = null;

    public Follower getFollower() {
        if (dual != null) {
            return DualA301DriveBase.getFollower();
        }
        return null;
    }

    public Gamepad getGamepad1() {
        return g1;
    }

    public Gamepad getGamepad2() {
        return g2;
    }

    public static class DualDriveBaseBot {

        private final Robot robot;
        private final DualA301DriveBase.Component driveBase;
        public final OnboardIMU imu = new OnboardIMU(OnboardIMU.MountOrientation.LANDSCAPE);

        public DualDriveBaseBot(Robot r) {
            robot = r;
            driveBase = new DualA301DriveBase.Component(r);
            dual = this;
        }
    }

    public static class HybridMouseBot {

        public final Robot robot;
        public final HybridDriveBase.Component driveBase;
        public final Gimbal.Component gimbal;
        public final LimelightCameraTargeting ll;
        public final OnboardIMU imu = new OnboardIMU(OnboardIMU.MountOrientation.LANDSCAPE);

        public HybridMouseBot(Robot r, Alliance all) {
            robot = r;
            ll = new LimelightCameraTargeting();
            gimbal = new Gimbal.Component(ll);
            driveBase = new HybridDriveBase.Component(gimbal, all);
            mouse = this;
        }
    }

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public Robot() {}

    /**
     * This function is called exactly once when the DS first connects.
     */
    @Override
    public void driverStationConnected() {}

    /**
     * This function is called periodically anytime when no opmode is selected, including when the
     * Driver Station is disconnected.
     */
    @Override
    public void nonePeriodic() {}
}
