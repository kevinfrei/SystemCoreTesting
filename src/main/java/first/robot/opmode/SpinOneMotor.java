// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.opmode;

import com.revrobotics.spark.A301;
import first.robot.Robot;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;

@Teleop(name = "Spin 1 of 8 Motors")
public class SpinOneMotor extends PeriodicOpMode {

    private final Robot robot;
    private A301[] motors;
    private int lastset = 0;

    /** The Robot instance is passed into the opmode via the constructor. */
    public SpinOneMotor(Robot robot) {
        this.robot = robot;
        motors = new A301[] {
            robot.frontLeft.A(),
            robot.frontLeft.B(),
            robot.frontRight.A(),
            robot.frontRight.B(),
            robot.rearRight.A(),
            robot.rearRight.B(),
            robot.rearLeft.A(),
            robot.rearLeft.B(),
        };
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
        double y = robot.gamepad.getLeftY();
        double x = robot.gamepad.getLeftX();
        double angle = (8 * Math.atan2(y, x)) / (Math.PI * 2);
        int select = (int) Math.round(angle);
        double mag = .6 - Math.abs(angle - select);
        select = (select + 8) & 7;
        if (Math.abs(Math.hypot(x, y)) > 0.5) {
            if (select != lastset) {
                set(0);
            }
            lastset = select;
            set(mag);
        } else {
            set(0);
        }
    }

    private void set(double m) {
        motors[lastset].setThrottle(m);
    }

    @Override
    public void end() {
        /* Called when the robot is disabled (after previously being enabled). */
        set(0);
    }

    @Override
    public void close() {
        /*
         * Called when the opmode is de-selected / no additional methods will be called.
         */
        set(0);
    }
}
