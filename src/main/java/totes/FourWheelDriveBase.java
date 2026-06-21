package totes;

import com.pedropathing.ftc.drivetrains.Motor;
import com.pedropathing.ftc.localization.CustomIMU;
import com.pedropathing.ftc.localization.Encoder;
import org.jspecify.annotations.NonNull;

public interface FourWheelDriveBase {
    @NonNull
    Motor[] getMotors();

    @NonNull
    Encoder[] getEncoders();

    @NonNull
    CustomIMU getIMU();
}
