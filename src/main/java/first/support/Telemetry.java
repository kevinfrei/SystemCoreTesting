package first.support;

import java.util.ArrayList;
import java.util.List;

// import org.wpilib.driverstation.DriverStation;

// Super cheesy Telemetry implementation until the DriverStationDisplay work is done in WPILib
public class Telemetry {

    List<String> lines;

    public Telemetry() {
        lines = new ArrayList<>(10);
    }

    public void debug(String s) {
        lines.add(s);
    }

    public void debug(String s, Object o) {
        lines.add(String.format("%s: %s", s, o.toString()));
    }

    public void addData(Object a, Object b) {
        debug(a.toString(), b);
    }

    public void update(Object o) {
        int len = 0;
        for (String s : lines) {
            len = Math.max(len, s.length());
        }
        String top = "*".repeat(len + 4);
        String spc = "*" + " ".repeat(len + 2) + "*";
        System.out.println(top);
        System.out.println(spc);
        for (String s : lines) {
            System.out.printf("* %s *%n", s);
        }
        System.out.println(spc);
        System.out.println(top);
        lines.clear();
    }

    public void update() {
        update(this);
    }
}
