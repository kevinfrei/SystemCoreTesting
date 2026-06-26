// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import org.wpilib.driverstation.Gamepad;
import org.wpilib.framework.OpModeRobot;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class GlobalContext extends OpModeRobot {

    public Gamepad g1 = new Gamepad(0);
    public Gamepad g2 = new Gamepad(1);

    /*
    public final A301 frontLeft = new A301(DriveBase.Config.flPort);
    public final A301 frontRight = new A301(DriveBase.Config.frPort);
    public final A301 rearRight = new A301(DriveBase.Config.rrPort);
    public final A301 rearLeft = new A301(DriveBase.Config.rlPort);

    public final ExpansionHubMotor frontLeft = new ExpansionHubMotor(0, 0);
    public final ExpansionHubMotor frontRight = new ExpansionHubMotor(0, 1);
    public final ExpansionHubMotor rearRight = new ExpansionHubMotor(0, 2);
    public final ExpansionHubMotor rearLeft = new ExpansionHubMotor(0, 3);
    public final ExpansionHubMotor fbEncoder = new ExpansionHubMotor(1, 0);
    public final ExpansionHubMotor strfEncoder = new ExpansionHubMotor(0, 0);

    public final Encoder fwdEncoder = new Encoder(
        DriveBase.Config.fbEncCh0,
        DriveBase.Config.fbEncCh1
    );
    public final Encoder strafEncoder = new Encoder(
        DriveBase.Config.strafeEncCh0,
        DriveBase.Config.strafeEncCh1
    );

    // 2 for 2 wheel odo, 4 for 'use the drive encoders'. Let's hope we can attach odo;

    private final com.pedropathing.ftc.localization.Encoder[] encoders = {
        new SystemCoreEncoder(fwdEncoder),
        new SystemCoreEncoder(strafEncoder),
    };
    */

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public GlobalContext() {}

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
