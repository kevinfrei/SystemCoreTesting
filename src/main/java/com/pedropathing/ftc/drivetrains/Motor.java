package com.pedropathing.ftc.drivetrains;

/**
 * This serves as a lowest common denominator interface for drivebase motors.
 * Honestly, it's not great, and probably far from ideal, but it should let users (and me)
 * have a 'custom' motor implementation (like 2 A301's per wheel!) without needing to change
 * the library.
 */
public interface Motor {
    void setReversed(boolean isReversed);
    void setPower(double dutyCyle);
    void setZeroBraking(boolean shouldBrake);
}
