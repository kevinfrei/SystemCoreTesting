package totes;

import com.pedropathing.ftc.drivetrains.Motor;
import org.jspecify.annotations.NonNull;

/* Interface for using a Robot (or other class) to contain stuff for Pedro */
public interface FourWheelDriveBase {
    @NonNull
    Motor[] getMotors();
}
