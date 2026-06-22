package com.pedropathing.ftc;

import com.pedropathing.ftc.localization.SCEncoder;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;

class HubEncoder implements SCEncoder {

    private final ExpansionHubMotor motor;
    private double previousPosition;
    private double currentPosition;
    private double multiplier;

    public HubEncoder(ExpansionHubMotor m) {
        motor = m;
        multiplier = 1;
        reset();
    }

    @Override
    public void setMultiplier(double setMultiplier) {
        multiplier = setMultiplier;
    }

    @Override
    public double getMultiplier() {
        return multiplier; // TODO:KBF THe ExpansionHub motor won't tell us if it's reversed
    }

    @Override
    public void update() {
        previousPosition = currentPosition;
        currentPosition = motor.getEncoderPosition();
    }

    @Override
    public void reset() {
        double pos = motor.getEncoderPosition();
        previousPosition = pos;
        currentPosition = pos;
    }

    @Override
    public double getDeltaPosition() {
        return getMultiplier() * (currentPosition - previousPosition);
    }
}
