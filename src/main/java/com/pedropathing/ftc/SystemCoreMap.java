package com.pedropathing.ftc;

import com.pedropathing.ftc.drivetrains.SCMotor;
import com.pedropathing.ftc.localization.*;
import com.revrobotics.spark.A301;
import org.jspecify.annotations.Nullable;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.rotation.Encoder;

/**
 * This is to replace the hardwareMap thing from the old weird FTC SDK.
 * We have to pass this around, wherever we had to pass the hardware map before.
 */
public abstract class SystemCoreMap {

    /**
     * Override this to make your SystemCoreMap tolerant to failing
     * requests.
     *
     * @return True if you want an exception thrown when requested hardware
     *         is missing
     */
    protected boolean failOnNull() {
        return true;
    }

    public enum HardwareName {
        // Drive Base motor selection.
        // These should return an object of type:
        // * com.pedropathing.ftc.drivetrains.SCMotor
        // * com.revrobotics.spark.A301
        // * org.wpilib.hardware.expansionhub.ExpansionHubMotor
        FRONT_LEFT_MOTOR,
        FRONT_RIGHT_MOTOR,
        REAR_RIGHT_MOTOR,
        REAR_LEFT_MOTOR,
        // Encoder selection.
        // These should return an object of type:
        // * com.pedropathing.ftc.localization.SCEncoder,
        // * org.wpilib.hardware.expansionhub.ExpansionHubMotor,
        // * org.wpilib.hardware.rotation.Encoder
        // For 2 pod setups:
        TWO_WHEEL_STRAFE_ENCODER,
        TWO_WHEEL_FWD_ENCODER,
        // For 3 pod setups
        THREE_WHEEL_LEFT_ENCODER,
        THREE_WHEEL_RIGHT_ENCODER,
        THREE_WHEEL_STRAFE_ENCODER,
        // For wheel-based localization
        FRONT_LEFT_ENCODER,
        FRONT_RIGHT_ENCODER,
        REAR_RIGHT_ENCODER,
        REAR_LEFT_ENCODER,
        // An IMU interface
        IMU,
    }

    /**
     * User has to override this one method to return the object requested.
     *
     * Motors supported include:
     * - {@link com.revrobotics.spark.A301}
     * - {@link org.wpilib.hardware.expansionhub.ExpansionHubMotor}
     * - An object that implements the {@link com.pedropathing.ftc.drivetrains.SCMotor} interface
     *
     * Encoders supported include:
     * - {@link org.wpilib.hardware.rotation.Encoder}
     * - {@link org.wpilib.hardware.expansionhub.ExpansionHubMotor} because, well, that's how you get to those encoders...
     * - {@link com.revrobotics.spark.A301} if you're using drive motors for odometry.
     * - An object that implements the {@link com.pedropathing.ftc.localization.SCEncoder} interface
     *
     * IMUs supported include:
     * - {@link org.wpilib.hardware.imu.OnboardIMU}
     * - An object that implements the {@link com.pedropathing.ftc.localization.CustomIMU} interface
     *
     * @param nm The {@link com.pedropathing.ftc.SystemCoreMap.HardwareName} item requested
     * @return The object requested, or null
     */
    @Nullable
    protected abstract Object getHardware(HardwareName nm);

    /**
     * This is the helper to get a particular Motor object, by calling
     * {@link getHardware} with the specific hardware 'name' requested.
     *
     * @param nm One of the FL/FR/RR/FL motors
     * @return an SCMotor interface for the motor requested, or null
     */
    @Nullable
    public final SCMotor getMotor(HardwareName nm) {
        Object motor = getHardware(nm);
        if (motor == null) {
            if (failOnNull()) {
                throw new IllegalArgumentException("No motor for named item " + nm.toString());
            }
            return null;
        }
        if (motor instanceof SCMotor) {
            return (SCMotor) motor;
        }
        if (motor instanceof A301) {
            return new A301Motor((A301) motor);
        }
        if (motor instanceof ExpansionHubMotor) {
            return new HubMotor((ExpansionHubMotor) motor);
        }
        throw new IllegalArgumentException("Unknown motor type returned for " + nm.toString());
    }

    /**
     * This is the helper to get a particular Motor object, by calling
     * {@link getHardware} with the specific hardware 'name' requested.
     *
     * @param nm One of the FL/FR/RR/FL motors
     * @return an SCMotor interface for the motor requested, or null
     */
    @Nullable
    public final SCEncoder getEncoder(HardwareName nm) {
        Object enc = getHardware(nm);
        if (enc == null) {
            if (failOnNull()) {
                throw new IllegalArgumentException("No encoder for named item " + nm.toString());
            }
            return null;
        }
        if (enc instanceof SCEncoder) {
            return (SCEncoder) enc;
        }
        if (enc instanceof Encoder) {
            return new SystemCoreEncoder((Encoder) enc);
        }
        if (enc instanceof ExpansionHubMotor) {
            return new HubEncoder((ExpansionHubMotor) enc);
        }
        if (enc instanceof A301) {
            return new A301Encoder((A301) enc);
        }
        throw new IllegalArgumentException("Unknown encoder type returned for " + nm.toString());
    }

    /**
     * This is the helper to get the IMU from the {@link getHardware} API.
     *
     * @return an SCMotor interface for the motor requested, or null
     */
    @Nullable
    public final CustomIMU getIMU() {
        Object imu = getHardware(HardwareName.IMU);
        if (imu == null) {
            if (failOnNull()) {
                throw new IllegalArgumentException("No IMU found!");
            }
            return null;
        }
        if (imu instanceof CustomIMU) {
            return (CustomIMU) imu;
        }
        if (imu instanceof OnboardIMU) {
            return new SystemCoreIMU((OnboardIMU) imu);
        }
        throw new IllegalArgumentException(
            "Unknown IMU type returned from getHardware(HardwareNames.IMU)"
        );
    }
}
