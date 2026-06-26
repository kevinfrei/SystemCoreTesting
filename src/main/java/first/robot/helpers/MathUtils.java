package first.robot.helpers;

public class MathUtils {

    public static double DeadZone(double val, double deadZone) {
        if (Math.abs(val) > deadZone) {
            return Math.copySign((Math.abs(val) - deadZone) / (1 - deadZone), val);
        }
        return 0;
    }
}
