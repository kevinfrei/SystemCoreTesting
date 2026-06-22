package com.pedropathing.ftc;

import com.pedropathing.ftc.drivetrains.SCMotor;
import com.revrobotics.spark.A301;
import org.jspecify.annotations.NonNull;

class A301Motor implements SCMotor {

    private final A301 motor;

    public A301Motor(@NonNull A301 hardware) {
        motor = hardware;
    }

    @Override
    public void setReversed(boolean isReversed) {
        motor.setInverted(isReversed);
    }

    @Override
    public void setPower(double dutyCyle) {
        motor.setThrottle(dutyCyle);
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
