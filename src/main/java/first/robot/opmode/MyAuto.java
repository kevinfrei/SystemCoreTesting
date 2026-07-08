// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import first.robot.Robot;
import first.robot.helpers.ElapsedTime;
import org.wpilib.driverstation.Alliance;
import org.wpilib.opmode.Autonomous;
import org.wpilib.opmode.PeriodicOpMode;

@Autonomous(name = "Looping 50ms Auto", group = "Mouse")
public class MyAuto extends PeriodicOpMode {

    private ElapsedTime timer;
    private double curNum;
    private double delta;
    private double cutOff = 0.5;
    private int ms = 250;
    private int which;
    private Robot.HybridMouseBot robot;

    // The Robot instance is passed into the opmode via the constructor.
    public MyAuto(Robot robot) {
        this.timer = new ElapsedTime();
        this.curNum = 0.0;
        this.delta = 0.025;
        this.which = 3;
        this.robot = new Robot.HybridMouseBot(robot, Alliance.BLUE);
    }

    // Called once when this opmode transitions to enabled.
    @Override
    public void start() {
        stopMotion();
        timer.reset();
    }

    //
    // This method runs periodically, using the same period as the Robot instance.
    //
    // Additional periodic methods may be configured with addPeriodic(),
    // which can have periods that differ from the main Robot instance.

    @Override
    public void periodic() {
        if (timer.millis() > ms) {
            timer.reset();
            if (Math.abs(curNum) >= cutOff) {
                delta = -delta;
            }
            curNum += delta;
            if (Math.abs(curNum - 0.001) < 0.01) {
                which = (which + 1) % 7;
            }
        }
    }

    @Override
    public void end() {
        stopMotion();
    }

    private void stopMotion() {
        curNum = 0;
        which = 3;
    }
}
