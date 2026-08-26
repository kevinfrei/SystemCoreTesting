// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.SystemCoreMap;
import com.revrobotics.spark.A301;
import first.robot.components.DriveBase;
import first.robot.helpers.DualA301Motor;
import first.support.GoBildaPinpoint;
import first.support.TunablePedroBot;
import org.jspecify.annotations.Nullable;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.framework.OpModeRobot;
import org.wpilib.hardware.bus.I2C;
import org.wpilib.hardware.hal.CANBusMap;
import org.wpilib.hardware.imu.OnboardIMU;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class Robot extends OpModeRobot implements SystemCoreMap, TunablePedroBot {

    @Override
    public @Nullable Object getHardware(HardwareName nm) {
        return switch (nm) {
            // Motors
            case FRONT_LEFT_MOTOR -> frontLeft;
            case FRONT_RIGHT_MOTOR -> frontRight;
            case REAR_LEFT_MOTOR -> rearLeft;
            case REAR_RIGHT_MOTOR -> rearRight;
            // Encoders
            case FRONT_LEFT_ENCODER -> frontLeft;
            case FRONT_RIGHT_ENCODER -> frontRight;
            case REAR_LEFT_ENCODER -> rearLeft;
            case REAR_RIGHT_ENCODER -> rearRight;
            // And the IMU...
            case IMU -> imu;
            case GOBILDA_PINPOINT -> pinpoint;
            default -> null;
        };
    }

    /* 
    public final A301 frontLeft = new A301(DriveBase.Config.flPort);
    public final A301 frontRight = new A301(DriveBase.Config.frPort);
    public final A301 rearRight = new A301(DriveBase.Config.rrPort);
    public final A301 rearLeft = new A301(DriveBase.Config.rlPort);
    */
    /* 
    public final ExpansionHubMotor frontLeft = new ExpansionHubMotor(0, 0);
    public final ExpansionHubMotor frontRight = new ExpansionHubMotor(0, 1);
    public final ExpansionHubMotor rearRight = new ExpansionHubMotor(0, 2);
    public final ExpansionHubMotor rearLeft = new ExpansionHubMotor(0, 3);
    public final ExpansionHubMotor fbEncoder = new ExpansionHubMotor(1, 0);
    public final ExpansionHubMotor strfEncoder = new ExpansionHubMotor(0, 0);
    */
    public final DualA301Motor frontLeft = new DualA301Motor(
        new A301(CANBusMap.CAN_D0),
        new A301(CANBusMap.CAN_D1)
    );
    public final DualA301Motor frontRight = new DualA301Motor(
        new A301(CANBusMap.CAN_D2),
        new A301(CANBusMap.CAN_D3)
    );
    public final DualA301Motor rearLeft = new DualA301Motor(
        new A301(CANBusMap.CAN_D4),
        new A301(CANBusMap.CAN_D5)
    );
    public final DualA301Motor rearRight = new DualA301Motor(
        new A301(CANBusMap.CAN_D6),
        new A301(CANBusMap.CAN_D7)
    );
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
    public final Gamepad gamepad2 = new Gamepad(1);

    public final GoBildaPinpoint pinpoint = new GoBildaPinpoint(I2C.Port.PORT_1);
    public Follower follower = null;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public Robot() {
        follower = DriveBase.getFollower(this);
    }

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

    @Override
    public Follower getFollower() {
        return follower;
    }

    @Override
    public Gamepad getGamepad1() {
        return gamepad;
    }

    @Override
    public Gamepad getGamepad2() {
        return gamepad2;
    }
}
