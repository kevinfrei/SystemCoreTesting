package first.robot.helpers;

import java.time.Duration;
import java.time.Instant;

public class ElapsedTime {

    public Instant lastTime;

    public ElapsedTime() {
        lastTime = Instant.now();
    }

    public void reset() {
        lastTime = Instant.now();
    }

    public long millis() {
        return Instant.now().toEpochMilli() - lastTime.toEpochMilli();
    }

    public long milliseconds() {
        return millis();
    }
}
