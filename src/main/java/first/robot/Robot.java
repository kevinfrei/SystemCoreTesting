// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.SystemCoreMap;
import com.revrobotics.spark.A301;
import first.robot.components.DriveBase;
import org.jspecify.annotations.Nullable;
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
public class Robot extends OpModeRobot {

    public static class MyHardwareMap extends SystemCoreMap {

        private final Robot r;

        public MyHardwareMap(Robot r) {
            this.r = r;
        }

        @Override
        protected @Nullable Object getHardware(HardwareName nm) {
            return switch (nm) {
                case FRONT_LEFT_MOTOR -> r.frontLeft;
                case FRONT_RIGHT_MOTOR -> r.frontRight;
                case REAR_LEFT_MOTOR -> r.rearLeft;
                case REAR_RIGHT_MOTOR -> r.rearRight;
                case FRONT_LEFT_ENCODER -> r.frontLeft;
                case FRONT_RIGHT_ENCODER -> r.frontRight;
                case REAR_LEFT_ENCODER -> r.rearLeft;
                case REAR_RIGHT_ENCODER -> r.rearRight;
                case IMU -> r.imu;
                default -> null;
            };
        }
    }

    public final A301 frontLeft = new A301(DriveBase.Config.flPort);
    public final A301 frontRight = new A301(DriveBase.Config.frPort);
    public final A301 rearRight = new A301(DriveBase.Config.rrPort);
    public final A301 rearLeft = new A301(DriveBase.Config.rlPort);
    /*
    public final Encoder fwdEncoder = new Encoder(
        DriveBase.Config.fbEncCh0,
        DriveBase.Config.fbEncCh1
    );
    public final Encoder strafEncoder = new Encoder(
        DriveBase.Config.strafeEncCh0,
        DriveBase.Config.strafeEncCh1
    );
    */
    public final OnboardIMU imu = new OnboardIMU(OnboardIMU.MountOrientation.LANDSCAPE);

    // 2 for 2 wheel odo, 4 for 'use the drive encoders'. Let's hope we can attach odo;
    /*
    private final com.pedropathing.ftc.localization.Encoder[] encoders = {
        new SystemCoreEncoder(fwdEncoder),
        new SystemCoreEncoder(strafEncoder),
    };
    */

    public final Gamepad gamepad = new Gamepad(0);
    public Follower follower = null;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public Robot() {}

    /**
     * This function is called exactly once when the DS first connects.
     */
    @Override
    public void driverStationConnected() {
        SystemCoreMap scm = new MyHardwareMap(this);
        follower = DriveBase.getFollower(scm);
    }

    /**
     * This function is called periodically anytime when no opmode is selected, including when the
     * Driver Station is disconnected.
     */
    @Override
    public void nonePeriodic() {}
}
