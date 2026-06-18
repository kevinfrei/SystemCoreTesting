// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import java.time.Duration;
import java.time.Instant;

import org.wpilib.opmode.Autonomous;
import org.wpilib.opmode.PeriodicOpMode;
import first.robot.Robot;

@Autonomous(name = "My Auto", group = "Group 1")
public class MyAuto extends PeriodicOpMode {
  private final Robot robot;
  private Instant time;
  private double curNum;
  private double delta;

  /** The Robot instance is passed into the opmode via the constructor. */
  public MyAuto(Robot robot) {
    this.robot = robot;
    this.time = Instant.now();        
    this.curNum = 0.0;
    this.delta = 0.1;
  }

  /** Called once when this opmode transitions to enabled. */
  @Override
  public void start() {
    time = Instant.now();        
  }

  /*
   * This method runs periodically, using the same period as the Robot instance.
   *
   * Additional periodic methods may be configured with addPeriodic(),
   * which can have periods that differ from the main Robot instance.
   */
  @Override
  public void periodic() {
    Instant end = Instant.now();
    long elapsedMillis = Duration.between(time, end).toMillis();
    if (elapsedMillis > 250) {
      if (Math.abs(curNum) >= 1.0) {
        delta = -delta;
      }
      curNum += delta;
      time = end;
      robot.motor1.setThrottle(curNum);
    }
  }
}
