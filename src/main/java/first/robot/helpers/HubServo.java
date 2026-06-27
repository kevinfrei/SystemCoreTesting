package first.robot.helpers;

import org.wpilib.hardware.expansionhub.ExpansionHubServo;

/**
 * A helper container to allow querying of the current position of the servo
 */
public class HubServo extends ExpansionHubServo {

    double curPos;

    /**
     * Constructs a servo at the requested channel on a specific USB port.
     *
     * @param usbId   The USB port ID the hub is connected to
     * @param channel The servo channel
     */
    public HubServo(int usbId, int channel) {
        super(usbId, channel);
        curPos = 0;
    }

    @Override
    public void setPosition(double pos) {
        curPos = pos;
        super.setPosition(pos);
    }

    public double getPosition() {
        return curPos;
    }
}
