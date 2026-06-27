package first.robot.components;

import first.robot.helpers.TargetAcquisition;

// TODO: Is a Limelight 3A supported? I should go find out
public class LimelightCameraTargeting implements TargetAcquisition {

    @Override
    public double getDistance() {
        // -1 means no target, so this should keep a dead implementation 'safe'
        return -1;
    }

    @Override
    public double getHorizontalPosition() {
        return 0;
    }

    @Override
    public double getVerticalPosition() {
        return 0;
    }
}
