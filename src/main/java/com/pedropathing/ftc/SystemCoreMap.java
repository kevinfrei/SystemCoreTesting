package com.pedropathing.ftc;

import com.pedropathing.ftc.drivetrains.SCMotor;
import com.pedropathing.ftc.localization.*;
import com.revrobotics.spark.A301;
import first.support.GoBildaPinpoint;
import org.jspecify.annotations.Nullable;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.rotation.Encoder;

/**
 * This is to replace the hardwareMap thing from the old FTC SDK.
 * We have to pass this around, wherever we had to pass the hardware map before.
 */
public interface SystemCoreMap {
    /**
     * Override this to make your SystemCoreMap tolerant to failing
     * requests.
     *
     * @return True if you want an exception thrown when requested hardware
     *         is missing
     */
    default boolean failOnNull() {
        return true;
    }

    enum HardwareName {
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
        // The GoBildaPinpoint I2C device
        GOBILDA_PINPOINT,
    }

    /**
     * User has to override this one method to return the object requested.
     * Motors supported include:
     * - {@link A301}
     * - {@link ExpansionHubMotor}
     * - An object that implements the {@link SCMotor} interface
     * Encoders supported include:
     * - {@link Encoder}
     * - {@link ExpansionHubMotor} because, well, that's how you get to those
     * encoders...
     * - {@link A301} if you're using drive motors for odometry.
     * - An object that implements the {@link SCEncoder} interface
     * IMUs supported include:
     * - {@link OnboardIMU}
     * - An object that implements the {@link CustomIMU} interface
     *
     * @param nm The {@link HardwareName} item requested
     * @return The object requested, or null
     */
    @Nullable
    Object getHardware(HardwareName nm);

    /**
     * This is the helper to get a particular Motor object, by calling
     * {@link #getHardware} getHardware} with the specific hardware 'name'
     * requested.
     *
     * @param nm One of the FL/FR/RR/FL motors
     * @return an SCMotor interface for the motor requested, or null
     */
    @Nullable
    default SCMotor getMotor(HardwareName nm) {
        Object motor = getHardware(nm);
        switch (motor) {
            case null -> {
                if (failOnNull()) {
                    throw new IllegalArgumentException("No motor for named item " + nm.toString());
                }
                return null;
            }
            case SCMotor scMotor -> {
                return scMotor;
            }
            case A301 a301 -> {
                return new A301Motor(a301);
            }
            case ExpansionHubMotor expansionHubMotor -> {
                return new HubMotor(expansionHubMotor);
            }
            default -> throw new IllegalArgumentException(
                "Unknown motor type returned for " + nm.toString()
            );
        }
    }

    /**
     * This is the helper to get a particular Motor object, by calling
     * {@link #getHardware} with the specific hardware 'name' requested.
     *
     * @param nm One of the FL/FR/RR/FL motors
     * @return an SCMotor interface for the motor requested, or null
     */
    @Nullable
    default SCEncoder getEncoder(HardwareName nm) {
        Object enc = getHardware(nm);
        switch (enc) {
            case null -> {
                if (failOnNull()) {
                    throw new IllegalArgumentException(
                        "No encoder for named item " + nm.toString()
                    );
                }
                return null;
            }
            case SCEncoder scEncoder -> {
                return scEncoder;
            }
            case Encoder encoder -> {
                return new SystemCoreEncoder(encoder);
            }
            case ExpansionHubMotor expansionHubMotor -> {
                return new HubEncoder(expansionHubMotor);
            }
            case A301 a301 -> {
                return new A301Encoder(a301);
            }
            default -> throw new IllegalStateException(
                "Unknown encoder type returned for " + nm.toString()
            );
        }
    }

    /**
     * This is the helper to get the IMU from the {@link #getHardware} API.
     *
     * @return an SCMotor interface for the motor requested, or null
     */
    @Nullable
    default CustomIMU getIMU() {
        Object imu = getHardware(HardwareName.IMU);
        switch (imu) {
            case null -> {
                if (failOnNull()) {
                    throw new IllegalArgumentException("No IMU found!");
                }
                return null;
            }
            case CustomIMU customIMU -> {
                return customIMU;
            }
            case OnboardIMU onboardIMU -> {
                return new SystemCoreIMU(onboardIMU);
            }
            default -> throw new IllegalArgumentException(
                "Unknown IMU type returned from getHardware(HardwareNames.IMU)"
            );
        }
    }

    @Nullable
    default GoBildaPinpoint getPinpoint() {
        Object o = getHardware(HardwareName.GOBILDA_PINPOINT);
        if (o != null && o instanceof GoBildaPinpoint) {
            return (GoBildaPinpoint) o;
        }
        if (failOnNull()) {
            throw new IllegalArgumentException("No Pinpoint found!");
        } else {
            return null;
        }
    }
}
