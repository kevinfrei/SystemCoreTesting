package totes;

import com.pedropathing.ftc.localization.CustomIMU;
import com.pedropathing.ftc.localization.Encoder;
import org.jspecify.annotations.NonNull;

/* Interface for using a Robot (or other class) to contain stuff for Pedro */
public interface TwoWheelOdo {
    @NonNull
    Encoder[] getEncoders();

    @NonNull
    CustomIMU getIMU();
}
