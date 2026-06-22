package com.pedropathing.ftc;

import com.pedropathing.ftc.drivetrains.SCMotor;
import com.pedropathing.ftc.localization.*;
import com.revrobotics.spark.A301;
import org.jspecify.annotations.Nullable;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.hardware.imu.OnboardIMU;

/**
 * This is to replace the hardwareMap thing from the old weird FTC SDK.
 * We have to pass this around, wherever we had to pass the hardware map before.
 */
public abstract class SystemCoreMap {

    public enum HardwareName {
        // Drive Base motor selection
        FRONT_LEFT_MOTOR,
        FRONT_RIGHT_MOTOR,
        REAR_RIGHT_MOTOR,
        REAR_LEFT_MOTOR,
        // Encoder selection:
        TWO_WHEEL_STRAFE_ENCODER,
        TWO_WHEEL_FWD_ENCODER,
        // For 3 encoder setups
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

    // User has to override this one method to return the object requested
    @Nullable
    protected abstract Object getHardware(HardwareName nm);

    public final SCMotor getMotor(HardwareName nm) {
        Object motor = getHardware(nm);
        if (motor == null) {
            throw new IllegalArgumentException("No motor for named item " + nm.toString());
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

    public final SCEncoder getEncoder(HardwareName nm) {
        Object enc = getHardware(nm);
        if (enc == null) {
            throw new IllegalArgumentException("No encoder for named item " + nm.toString());
        }
        if (enc instanceof SCEncoder) {
            return (SCEncoder) enc;
        }
        if (enc instanceof org.wpilib.hardware.rotation.Encoder) {
            return new SystemCoreEncoder((org.wpilib.hardware.rotation.Encoder) enc);
        }
        if (enc instanceof ExpansionHubMotor) {
            return new HubEncoder((ExpansionHubMotor) enc);
        }
        throw new IllegalArgumentException("Unknown encoder type returned for " + nm.toString());
    }

    public final CustomIMU getIMU() {
        Object imu = getHardware(HardwareName.IMU);
        if (imu == null) {
            throw new IllegalArgumentException("No IMU found!");
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
