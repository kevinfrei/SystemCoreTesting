# Porting [PedroPathing](https://pedropathing.com) to [SystemCore](https://github.com/wpilibsuite/SystemcoreTesting)

Check [down below](#who-am-i) if you care about the author's background.

## Challenges

There are _4_ principle challenges to porting PedroPath to SystemCore:

1. Android to Java changes
2. Hardware Interface
3. Supporting both Hybrid **and** MotionCore devices
4. Missing hardware

Let's discuss each in turn:

## Android to Java changes

To start with, I just copied the source code over. I'm not ready to actually
make a library just yet. So it lives in my testing repo.

There are 2 principle chunks of code, and honestly, this code is _very_ well
organized. I cannot say enough positive things about the students who have built
and maintained this codebase. The FTC-specific code lives in the
[`com.pedropathing.ftc`](ftc) sub-package. The rest of the code lives in
different packages all under the top level `com.pedropathing` package.

There was only _1_ minor syntactic change necessary to make for code outside of
the `com.pedropathing.ftc` package. They were using the `@NotNull` annotation
from JetBrains. I switched it over to
[JSpecify](https://jspecify.dev/docs/user-guide/) and converted it to
`@NonNull`. That was it. A+ for the PedroPath team. Amazing work.

I was honestly expecting more to do here to split things up, clean up something,
or, you know, anything slightly messy. Turns out, this work was _trivial_.

## Hardware interface

The FTC hardware interface is, honestly, weird and annoying. It's string based.
You have to configure things on that awful Driver's Station touch screen, and
oh, I hate that so very much. With SystemCore, everything is strictly based on
connector/port numbers, and you use a regular constructor with said
connector/port number to create the hardware. So easy!

I started out by just passing the required hardware objects around wherever PP
was asking for the `hardwareMap`. This worked, but as the next problem arose,
this became pretty unworkable. So, instead, I created an abstract class
[`com.pedropathing.ftc.SystemCoreMap`](ftc/SystemCoreMap.java) class that users
need to extend and [implement a single method](../../first/robot/Robot.java#L26)
that provides hardware objects as required. (For folks who care, this is a
particularly simplistic mechanism to accomplish
[Dependency Injection](https://en.wikipedia.org/wiki/Dependency_injection))

## Supporting both Hybrid _and_ MotionCore devices

There's a new problem with SystemCore that we don't really have in the current
world: There are _dramatically_ different version of things like _motors_ and
_encoders_. This wasn't done because SystemCore people were lazy or something.
The A301 motor is a
[BLDC](https://en.wikipedia.org/wiki/Brushless_DC_electric_motor), the same guts
as in a _stepper motor_. The motors you connect to an expansion hub, however,
are simple
[brushed DC motors](https://en.wikipedia.org/wiki/Brushed_DC_electric_motor) we
all know and love (or hate as they slowly wear out), which may or may not have
encoders attached. Encoders, on the other, may be attached to a motor on an
expansion hub, but they're strictly separate devices on SystemCore, but the A301
motors also have encoders on them, for use with wheel-based odometry. And
there's also a new IMU which currently doesn't support all 6 configurations you
might have (apparently FRC people don't have reasons to mount their control
hardware upside down?)

So, first things first: I made interfaces for each of the 3 things that PP uses,
that are supposed to be strcitly what PP requires, and nothing more:

- [SCEncoder](ftc/localization/SCEncoder.java) The Encoder interface
- [SCMotor](ftc/drivetrains/SCMotor.java) The Motor interface
- [CustomIMU](ftc/localization/CustomIMU.java) An IMU interface

Then, after trying do stick with the 'pass hardware to the thing that needs it'
approach, I realized that having a factory enables a few very helpful things.
Thus, SystemCoreMap is the factory that creates `SCEncoder`'s, `SCMotor`'s, and
`CustomIMU`'s. Rather than have multiple different methods for returning motors,
encoders, and IMU's. Instead, I just made a single function that returns an
object. There's not really a great reason for that, other than I'm kinda lazy.

The thing that this really enables, however, is for PP to support anyone easily
using something that doesn't exist today, or extending this in useful ways. I'm
immediately going to be adding a "pair of A301's" SCMotor implementation,
because that's what one of my students is building.

## Missing Hardware

There are no drivers for:

- goBilda PinPoint
- SparkFun OTOS
- OctoQuad

I just disabled all that stuff in the code. As/if drivers come available, I'll
go after them as they show up.

And I didn't care much about the Swerve stuff, so I just disabled it for now. We
have a Swerve bot base that we haven't gotten working yet. I'll probably go port
that stuff once we have that thing closer to functional...

In Summary:

### TODO:

- [ ] Pinpoint Localizer (drivers...)
- [ ] OTOS Localizer (drivers...)
- [ ] OctoQuad Localizer (drivers...)
- [ ] OctoQuad encoder location (drivers + adaptor class)
- [ ] All things Swerve related

### Who am I

Hi, I'm [Kevin Frei](https://github.com/kevinfrei), the lead mentor for FTC
teams 16750, The [Technototes](https://github.com/technototes) and 20403, the
Protototes out of Sammamish High School in Bellevue, WA. We received our
SystemCore hardware the _day after our final day of team meetings_. Spectacular
timing, universe! In addition, we've lost _all_ our programmers (they graduated
:sad: ), so we no longer have anyone who's really capable of doing much complex
software work. Well, except for me. (I'm a retired systems software engineer).
So, rather than sit and do nothing over the summer, our hardware students are
building up a drive base. I'm not really very good at that. I'm working on the
software side of things in public, for all FTC teams to learn & benefit from. If
something I've done stinks, please do give me feedback. I'm happy to listen and
learn (open issues, or maybe start a discussion, in this repo)

My only concern is the whole "but this seems mentor-coded" argument. Look,
WPILib is being worked on by professional software developers. And I recognize
that there are other path following libraries I could look at, but we switched
to PedroPathing this past year, and I've been _very_ impressed. None of what
I've done is really at all complicated. A random joker who's willing to pay for
Claude Code would probably have produced something similar, so I don't really
feel at all bad about "mentor coding" because WPILib is a step worse, and I'm
not writing any code that's currently being used solely by my teams for any sort
of competition. If you disagree, feel free to not use this code. I'd like to
contribute it back to the creators, but I hear that at least one of them is in
their last year of FTC, so I don't know what's happening with the overall
library

### fin
