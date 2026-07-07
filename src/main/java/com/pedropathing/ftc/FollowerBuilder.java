package com.pedropathing.ftc;

import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.drivetrains.*;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
// import com.pedropathing.ftc.localization.constants.OctoQuadConstants;
// import com.pedropathing.ftc.localization.constants.OTOSConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.ftc.localization.localizers.DriveEncoderLocalizer;
// import com.pedropathing.ftc.localization.localizers.OctoQuadLocalizer;
// import com.pedropathing.ftc.localization.localizers.OTOSLocalizer;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelLocalizer;
import com.pedropathing.ftc.localization.localizers.TwoWheelLocalizer;
import com.pedropathing.localization.Localizer;
import com.pedropathing.paths.PathConstraints;
import first.support.GoBildaPinpointDriver;

/**
 * This is the FollowerBuilder.
 * It is used to create Followers with a specific drivetrain + localizer without having to use a full constructor
 *
 * @author Baron Henderson - 20077 The Indubitables
 */
public class FollowerBuilder {

    private final FollowerConstants constants;
    private PathConstraints constraints;
    private Localizer localizer;
    private Drivetrain drivetrain;

    public FollowerBuilder(FollowerConstants constants) {
        this.constants = constants;
        constraints = PathConstraints.defaultConstraints;
    }

    public FollowerBuilder setLocalizer(Localizer localizer) {
        this.localizer = localizer;
        return this;
    }

    public FollowerBuilder driveEncoderLocalizer(
        SystemCoreMap scm,
        DriveEncoderConstants lConstants
    ) {
        return setLocalizer(new DriveEncoderLocalizer(scm, lConstants));
    }

    /* 
    public FollowerBuilder octoQuadLocalizer(OctoQuadConstants lConstants, OctoQuadLocalizer.InitMode initMode) {
        return setLocalizer(new OctoQuadLocalizer(hardwareMap, lConstants, initMode));
    }

    public FollowerBuilder OTOSLocalizer(OTOSConstants lConstants) {
        return setLocalizer(new OTOSLocalizer(hardwareMap, lConstants));
    }
    */

    public FollowerBuilder pinpointLocalizer(
        GoBildaPinpointDriver pp,
        PinpointConstants lConstants
    ) {
        return setLocalizer(new PinpointLocalizer(pp, lConstants));
    }

    public FollowerBuilder threeWheelIMULocalizer(
        SystemCoreMap scm,
        ThreeWheelIMUConstants lConstants
    ) {
        return setLocalizer(new ThreeWheelIMULocalizer(scm, lConstants));
    }

    public FollowerBuilder threeWheelLocalizer(SystemCoreMap scm, ThreeWheelConstants lConstants) {
        return setLocalizer(new ThreeWheelLocalizer(scm, lConstants));
    }

    public FollowerBuilder twoWheelLocalizer(SystemCoreMap scm, TwoWheelConstants lConstants) {
        return setLocalizer(new TwoWheelLocalizer(scm, lConstants));
    }

    public FollowerBuilder setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        return this;
    }

    public FollowerBuilder mecanumDrivetrain(SystemCoreMap scm, MecanumConstants mecanumConstants) {
        return setDrivetrain(new Mecanum(scm, mecanumConstants));
    }

    /*
    @Deprecated
    public FollowerBuilder mecanumExDrivetrain(MecanumConstants mecanumConstants) {
        return setDrivetrain(new MecanumEx(hardwareMap, mecanumConstants));
    }
    */
    /*
    public FollowerBuilder swerveDrivetrain(SwerveConstants swerveConstants, SwervePod... pods) {
        return setDrivetrain(new Swerve(hardwareMap, swerveConstants, pods));
    }
    */

    public FollowerBuilder pathConstraints(PathConstraints pathConstraints) {
        this.constraints = pathConstraints;
        PathConstraints.setDefaultConstraints(pathConstraints);
        return this;
    }

    public Follower build() {
        return new Follower(constants, localizer, drivetrain, constraints);
    }
}
