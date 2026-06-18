// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;

import first.robot.Robot;

@Teleop(name="MyTeleop")
public class MyTeleop extends PeriodicOpMode {
  private final Robot robot;
  private double throttle = 0.0;

  /** The Robot instance is passed into the opmode via the constructor. */  
  public MyTeleop(Robot robot) {
    this.robot = robot;
  }

  @Override
  public void disabledPeriodic() {
    /* Called periodically (on every DS packet) while the robot is disabled. */
  }

  @Override
  public void start() {
    /* Called once when the robot is enabled. */
  }

  @Override
  public void periodic() {
    if (robot.gamepad.getDpadUpButtonPressed()) {
      throttle += 0.1;
      robot.motor1.setThrottle(throttle);
    } else if (robot.gamepad.getDpadDownButtonPressed()) {
      throttle -= 0.1;
      robot.motor1.setThrottle(throttle);
    }
  }

  @Override
  public void end() {
    /* Called when the robot is disabled (after previously being enabled). */
    robot.motor1.setThrottle(0);
  }

  @Override
  public void close() {
    /* Called when the opmode is de-selected / no additional methods will be called. */
    robot.motor1.setThrottle(0);
  }
}
