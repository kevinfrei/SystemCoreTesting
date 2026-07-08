package com.pedropathing.ftc;

import com.pedropathing.ftc.drivetrains.SCMotor;
import com.revrobotics.spark.A301;
import org.jspecify.annotations.NonNull;

class A301Motor implements SCMotor {

    @NonNull
    private final A301 motor;

    private enum DriveMode {
        THROTTLE_BASED,
        RPM_BASED,
    }

    private DriveMode mode = DriveMode.RPM_BASED;
    private double rpm;
    public static int DEFAULT_RPM = 235;

    public A301Motor(@NonNull A301 hardware) {
        motor = hardware;
        var sig = hardware.getGearboxRPM();
        rpm = sig.isValid() ? sig.get().value : DEFAULT_RPM;
    }

    public void ThrottleMode() {
        mode = DriveMode.THROTTLE_BASED;
    }

    public void RPMMode() {
        mode = DriveMode.RPM_BASED;
    }

    @Override
    public void setReversed(boolean isReversed) {
        motor.setInverted(isReversed);
    }

    @Override
    public void setPower(double dutyCyle) {
        if (mode == DriveMode.THROTTLE_BASED) {
            motor.setThrottle(dutyCyle);
        } else {
            motor.setVelocity(dutyCyle * rpm);
        }
    }

    @Override
    public void setZeroBraking(boolean shouldBrake) {
        // TODO: This isn't supported via the A301 API today
    }

    @NonNull
    public A301 getRaw() {
        return motor;
    }
}
