package com.pedropathing.ftc;

// KBF- TODO: Implement this from the SystmeCore API

import com.pedropathing.ftc.localization.CustomIMU;
import org.wpilib.hardware.imu.OnboardIMU;

class SystemCoreIMU implements CustomIMU {

    private final OnboardIMU imu;

    public SystemCoreIMU(OnboardIMU IMU) {
        imu = IMU;
    }

    /**
     * Old version did this:
     *   Initializes the IMU using the hardwareMap and hubOrientation.
     * @ param hardwareMap the hardware map
     * @ param hardwareMapName the name of the hardware map
     * @ param hubOrientation the hub orientation
     * TODO:KBF Fix this. Not sure what the intialize method should take
     */
    @Override
    public void initialize() {
        imu.resetYaw();
    }

    /**
     * Gets the IMU's reading for the heading of the robot in radians
     * @return the heading of the robot in radians
     */
    @Override
    public double getHeading() {
        return imu.getYawRadians();
    }

    /**
     * Resets the IMU's yaw to 0.
     */
    @Override
    public void resetYaw() {
        imu.resetYaw();
    }
}
