// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import first.robot.Robot;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;

@Teleop(name = "Spin One Motor")
public class SpinOneMotor extends PeriodicOpMode {

    private static double STEP = 0.015625;
    private final Robot robot;
    private double throttle = 0.0;

    /** The Robot instance is passed into the opmode via the constructor. */
    public SpinOneMotor(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void disabledPeriodic() {
        /* Called periodically (on every DS packet) while the robot is disabled. */
    }

    @Override
    public void start() {
        /* Called once when the robot is enabled. */
        robot.frontLeft.setPower(0);
    }

    @Override
    public void periodic() {
        if (robot.gamepad.getDpadUpButtonPressed()) {
            throttle += STEP;
            robot.frontLeft.setPower(throttle);
            System.out.printf("Throttle up to %f%n", throttle);
        } else if (robot.gamepad.getDpadDownButtonPressed()) {
            throttle -= STEP;
            robot.frontLeft.setPower(throttle);
            System.out.printf("Throttle up to %f%n", throttle);
        }
    }

    @Override
    public void end() {
        /* Called when the robot is disabled (after previously being enabled). */
        robot.frontLeft.setPower(0);
    }

    @Override
    public void close() {
        /* Called when the opmode is de-selected / no additional methods will be called. */
        robot.frontLeft.setPower(0);
    }
}
