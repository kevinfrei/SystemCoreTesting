// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import com.revrobotics.spark.A301;
import first.robot.Robot;
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
    private A301[] motors;

    /** The Robot instance is passed into the opmode via the constructor. */
    public MyAuto(Robot robot) {
        this.time = Instant.now();
        this.curNum = 0.0;
        this.delta = 0.025;
        this.motors = new A301[] {
            robot.frontLeft,
            robot.frontRight,
            robot.rearRight,
            robot.rearLeft,
        };
        this.which = 3;
    }

    /** Called once when this opmode transitions to enabled. */
    @Override
    public void start() {
        stopMotors();
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
        if (elapsedMillis > ms) {
            if (Math.abs(curNum) >= cutOff) {
                delta = -delta;
            }
            curNum += delta;
            time = end;
            motors[Math.abs(3 - which)].setThrottle(curNum);
            if (Math.abs(curNum - 0.001) < 0.01) {
                which = (which + 1) % 7;
            }
            System.out.printf("Throttle %f #%d%n", curNum, Math.abs(3 - which));
        }
    }

    @Override
    public void end() {
        stopMotors();
    }

    private void stopMotors() {
        System.out.println("Stopping Motors");
        curNum = 0;
        which = 3;
        for (int i = 0; i < 4; i++) {
            motors[i].setThrottle(0);
        }
    }
}
