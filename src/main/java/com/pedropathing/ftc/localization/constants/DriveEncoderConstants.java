package com.pedropathing.ftc.localization.constants;

import com.pedropathing.ftc.localization.SCEncoder;

public class DriveEncoderConstants {

    public double forwardTicksToInches = 1;
    public double strafeTicksToInches = 1;
    public double turnTicksToInches = 1;

    public double robot_Width = 1;
    public double robot_Length = 1;

    public double leftFrontEncoderDirection = SCEncoder.REVERSE;
    public double rightFrontEncoderDirection = SCEncoder.FORWARD;
    public double leftRearEncoderDirection = SCEncoder.REVERSE;
    public double rightRearEncoderDirection = SCEncoder.FORWARD;

    public DriveEncoderConstants forwardTicksToInches(double forwardTicksToInches) {
        this.forwardTicksToInches = forwardTicksToInches;
        return this;
    }

    public DriveEncoderConstants strafeTicksToInches(double strafeTicksToInches) {
        this.strafeTicksToInches = strafeTicksToInches;
        return this;
    }

    public DriveEncoderConstants turnTicksToInches(double turnTicksToInches) {
        this.turnTicksToInches = turnTicksToInches;
        return this;
    }

    public DriveEncoderConstants robotWidth(double robot_Width) {
        this.robot_Width = robot_Width;
        return this;
    }

    public DriveEncoderConstants robotLength(double robot_Length) {
        this.robot_Length = robot_Length;
        return this;
    }

    public DriveEncoderConstants leftFrontEncoderDirection(double leftFrontEncoderDirection) {
        this.leftFrontEncoderDirection = leftFrontEncoderDirection;
        return this;
    }

    public DriveEncoderConstants rightFrontEncoderDirection(double rightFrontEncoderDirection) {
        this.rightFrontEncoderDirection = rightFrontEncoderDirection;
        return this;
    }

    public DriveEncoderConstants leftRearEncoderDirection(double leftRearEncoderDirection) {
        this.leftRearEncoderDirection = leftRearEncoderDirection;
        return this;
    }

    public DriveEncoderConstants rightRearEncoderDirection(double rightRearEncoderDirection) {
        this.rightRearEncoderDirection = rightRearEncoderDirection;
        return this;
    }

    public void defaults() {
        forwardTicksToInches = 1;
        strafeTicksToInches = 1;
        turnTicksToInches = 1;

        robot_Width = 1;
        robot_Length = 1;

        leftFrontEncoderDirection = SCEncoder.REVERSE;
        rightFrontEncoderDirection = SCEncoder.FORWARD;
        leftRearEncoderDirection = SCEncoder.REVERSE;
        rightRearEncoderDirection = SCEncoder.FORWARD;
    }
}
