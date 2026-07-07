package first.robot.helpers;

import com.pedropathing.ftc.drivetrains.SCMotor;
import com.pedropathing.ftc.localization.SCEncoder;
import com.revrobotics.spark.A301;
import com.revrobotics.util.Signal;

public class DualA301Motor implements SCMotor, SCEncoder {

    private A301 a, b;
    private double previousPosition;
    private double currentPosition;
    private double multiplier;

    public DualA301Motor(A301 a, A301 b) {
        this.a = a;
        this.b = b;
        previousPosition = 0;
        currentPosition = 0;
        multiplier = 1.0;
    }

    public A301 A() {
        return a;
    }

    public A301 B() {
        return b;
    }

    @Override
    public void setReversed(boolean isReversed) {
        a.setInverted(isReversed);
        b.setInverted(isReversed);
    }

    @Override
    public void setPower(double dutyCyle) {
        a.setThrottle(dutyCyle);
        b.setThrottle(dutyCyle);
    }

    @Override
    public void setZeroBraking(boolean shouldBrake) {
        /* BLDC's don't really coast, but I suspect this would still be helpful */
    }

    @Override
    public void setMultiplier(double setMultiplier) {
        multiplier = setMultiplier;
    }

    @Override
    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public void update() {
        previousPosition = currentPosition;
        Signal<Double> pos = a.getAbsoluteEncoderPosition();
        if (pos.isValid()) {
            currentPosition = pos.get();
        }
    }

    @Override
    public void reset() {
        Signal<Double> pos = a.getAbsoluteEncoderPosition();
        if (pos.isValid()) {
            previousPosition = pos.get();
            currentPosition = pos.get();
        }
    }

    @Override
    public double getDeltaPosition() {
        return getMultiplier() * (currentPosition - previousPosition);
    }
}
