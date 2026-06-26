// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import first.robot.GlobalContext;
import first.robot.robots.HybridMouseBot;
import java.time.Duration;
import java.time.Instant;
import org.wpilib.opmode.Autonomous;
import org.wpilib.opmode.PeriodicOpMode;

@Autonomous(name = "Looping 50ms Auto", group = "Group 1")
public class MyAuto extends PeriodicOpMode {

    private Instant time;
    private double curNum;
    private double delta;
    private double cutOff = 0.5;
    private int ms = 50;
    private int which;
    private HybridMouseBot robot;

    // TODO: Move the gimbal on MouseBot around

    // The Robot instance is passed into the opmode via the constructor.
    public MyAuto(GlobalContext globalContext) {
        this.time = Instant.now();
        this.curNum = 0.0;
        this.delta = 0.025;
        this.which = 3;
        robot = new HybridMouseBot(globalContext);
    }

    // Called once when this opmode transitions to enabled.
    @Override
    public void start() {
        stopMotion();
        time = Instant.now();
    }

    //
    // This method runs periodically, using the same period as the Robot instance.
    //
    // Additional periodic methods may be configured with addPeriodic(),
    // which can have periods that differ from the main Robot instance.

    @Override
    public void periodic() {
        Instant end = Instant.now();
        long elapsedMillis = Duration.between(time, end).toMillis();
        if (elapsedMillis > ms) {
            if (Math.abs(curNum) >= cutOff) {
                delta = -delta;
            }
            curNum += delta;
            time = end;
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
