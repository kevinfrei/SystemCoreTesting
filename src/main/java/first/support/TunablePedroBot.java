package first.support;

import com.pedropathing.follower.Follower;
import org.wpilib.driverstation.Gamepad;

public interface TunablePedroBot {
    public Follower getFollower();
    public Gamepad getGamepad1();
    public Gamepad getGamepad2();
}
