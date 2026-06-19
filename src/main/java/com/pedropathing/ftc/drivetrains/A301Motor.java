package com.pedropathing.ftc.drivetrains;

import com.revrobotics.spark.A301;

public class A301Motor implements Motor {
    private final A301 motor;

    public A301Motor(A301 hardware) {
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
}
