package com.pedropathing.ftc.drivetrains;

import org.wpilib.hardware.expansionhub.ExpansionHubMotor;

public class HubMotor implements Motor {

    ExpansionHubMotor motor;
    double mult;

    public HubMotor(ExpansionHubMotor m) {
        motor = m;
        mult = 1;
    }

    @Override
    public void setReversed(boolean isReversed) {
        mult = isReversed ? -1 : 1;
    }

    @Override
    public void setPower(double dutyCyle) {
        motor.setThrottle(mult * dutyCyle);
    }

    @Override
    public void setZeroBraking(boolean shouldBrake) {
        motor.setFloatOn0(!shouldBrake);
    }

    public ExpansionHubMotor getRaw() {
        return motor;
    }
}
