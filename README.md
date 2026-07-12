# SystemCore Testing

This is my WPILib 2027
[SystemCore/A301](https://github.com/wpilibsuite/SystemcoreTesting/discussions/300)
test repo.

I'm the lead mentor (and primarily a software guy) for FTC Teams 16750 and
20403, the Technototes & Protototes, respectively.

I'm in the process of porting [PedroPathing](https://pedropathing.com) to
SystemCore. You can read all the gory details
[here](src\main\java\com\pedropathing\PedroPorting.md)

The other work I'm doing in here is to generally support multiple robots in a
single codebase (because that's quite common in FTC, and doing something more
cumbersome is, well, more cumbersome). This codebase has stuff for a full
A301-based drivetrain, a "Hybrid mode" drivetrain, and I'm trying to get my
little 'demo bot' going with hybrid mode.

There are also parts of the "component" concept that our software team had begun
looking at this past season (_single file_, drop in "components" that include
testing opmodes, configuration, commands, etc... for _common_ subsystem types.
In WPILib Commands V3 nomenclature, a subsystem is called a `Mechanism`). You
can see them in the `src/main/java/first/robot/components` folder. As of this
writing, I have the DualA301DriveBase component, a barebones gimbal (movable
camera mount), a HybridDriveBase, and an _empty_ LimelightCameraTargeting
component (Limelight cameras aren't yet supported). None of it is very "done"
and honestly, only some of it has even really been tested yet. I'll try to keep
this readme up-to-date.
