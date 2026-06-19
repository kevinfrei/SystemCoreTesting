package com.pedropathing.ftc.localization;

// KBF- TODO: Implement this from the SystmeCore API

public class SystemCoreIMU implements  CustomIMU {

    /**
     * Old version did this:
     *   Initializes the IMU using the hardwareMap and hubOrientation.
     * @param hardwareMap the hardware map
     * @param hardwareMapName the name of the hardware map
     * @param hubOrientation the hub orientation
     */
    @Override
    public void initialize() {

    }

    /**
     * Gets the IMU's reading for the heading of the robot in radians
     * @return the heading of the robot in radians
     */
    @Override
    public double getHeading() {
        return 0;
    }

    /**
     * Resets the IMU's yaw to 0.
     */
    @Override
    public void resetYaw() {

    }
}
