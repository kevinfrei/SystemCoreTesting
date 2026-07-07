package com.pedropathing.ftc.localization;

public interface SCEncoder {
    void setMultiplier(double mult);
    double getMultiplier();
    void update();
    void reset();
    double getDeltaPosition();
    double FORWARD = 1;
    double REVERSE = -1;
}
