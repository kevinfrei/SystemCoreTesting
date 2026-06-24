package first.robot.helpers;

import com.pedropathing.ftc.drivetrains.SCMotor;
import com.revrobotics.spark.A301;

public class DualA301Motor implements SCMotor {

    private A301 a, b;

    public DualA301Motor(A301 a, A301 b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void setReversed(boolean isReversed) {
        a.setInverted(isReversed);
        b.setInverted(isReversed);
    }

    @Override
    public void setPower(double dutyCyle) {
        a.setThrottle(dutyCyle);
        b.setThrottle(dutyCyle);
    }

    @Override
    public void setZeroBraking(boolean shouldBrake) {
        /* BLDC's don't really coast, but I suspect this would still be helpful */
    }
}
