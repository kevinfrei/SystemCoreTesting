// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import org.wpilib.driverstation.Gamepad;
import org.wpilib.framework.OpModeRobot;
import org.wpilib.hardware.hal.CANBusMap;

import com.revrobotics.spark.A301;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class Robot extends OpModeRobot {

  public final A301 motor1 = new A301(CANBusMap.CAN_D0);
  public final A301 motor2 = new A301(CANBusMap.CAN_D0);
  public final Gamepad gamepad = new Gamepad(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {

  }

  /** This function is called exactly once when the DS first connects. */
  @Override
  public void driverStationConnected() {

  }

  /**
   * This function is called periodically anytime when no opmode is selected, including when the
   * Driver Station is disconnected.
   */
  @Override
  public void nonePeriodic() {}
}
