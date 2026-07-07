package com.pedropathing.ftc;

import com.pedropathing.ftc.drivetrains.SCMotor;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;

class HubMotor implements SCMotor {

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
