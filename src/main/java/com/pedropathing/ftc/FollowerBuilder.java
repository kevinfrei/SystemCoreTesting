package com.pedropathing.ftc;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.drivetrains.*;
import com.pedropathing.ftc.localization.CustomIMU;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
// import com.pedropathing.ftc.localization.constants.OctoQuadConstants;
// import com.pedropathing.ftc.localization.constants.OTOSConstants;
// import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.ftc.localization.localizers.DriveEncoderLocalizer;
// import com.pedropathing.ftc.localization.localizers.OctoQuadLocalizer;
// import com.pedropathing.ftc.localization.localizers.OTOSLocalizer;
// import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.ftc.localization.localizers.ThreeWheelLocalizer;
import com.pedropathing.ftc.localization.localizers.TwoWheelLocalizer;
import com.pedropathing.localization.Localizer;
import com.pedropathing.paths.PathConstraints;
import com.revrobotics.spark.A301;
import org.wpilib.hardware.expansionhub.ExpansionHubMotor;
import org.wpilib.hardware.rotation.Encoder;

/** This is the FollowerBuilder.
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

    public FollowerBuilder driveEncoderLocalizer(A301 lf, A301 lr, A301 rr, A301 rf, DriveEncoderConstants lConstants) {
        return setLocalizer(new DriveEncoderLocalizer(lf, lr, rr, rf, lConstants));
    }
    public FollowerBuilder driveEncoderLocalizer(ExpansionHubMotor lf, ExpansionHubMotor lr, ExpansionHubMotor rr, ExpansionHubMotor rf, DriveEncoderConstants lConstants) {
        return setLocalizer(new DriveEncoderLocalizer(lf, lr, rr, rf, lConstants));
    }
    /*
    public FollowerBuilder octoQuadLocalizer(OctoQuadConstants lConstants, OctoQuadLocalizer.InitMode initMode) {
        return setLocalizer(new OctoQuadLocalizer(hardwareMap, lConstants, initMode));
    }

    public FollowerBuilder OTOSLocalizer(OTOSConstants lConstants) {
        return setLocalizer(new OTOSLocalizer(hardwareMap, lConstants));
    }

    public FollowerBuilder pinpointLocalizer(PinpointConstants lConstants) {
        return setLocalizer(new PinpointLocalizer(hardwareMap, lConstants));
    }
    */
    public FollowerBuilder threeWheelIMULocalizer(ExpansionHubMotor lEnc, ExpansionHubMotor rEnc, ExpansionHubMotor strafeEnc, CustomIMU imu, ThreeWheelIMUConstants lConstants) {
        return setLocalizer(new ThreeWheelIMULocalizer(lEnc, rEnc, strafeEnc, imu, lConstants));
    }

    public FollowerBuilder threeWheelIMULocalizer(Encoder lEnc, Encoder rEnc, Encoder strafeEnc, CustomIMU imu, ThreeWheelIMUConstants lConstants) {
        return setLocalizer(new ThreeWheelIMULocalizer(lEnc, rEnc, strafeEnc, imu, lConstants));
    }

    public FollowerBuilder threeWheelLocalizer(ExpansionHubMotor lEnc, ExpansionHubMotor rEnc, ExpansionHubMotor strafeEnc, ThreeWheelConstants lConstants) {
        return setLocalizer(new ThreeWheelLocalizer(lEnc, rEnc, strafeEnc, lConstants));
    }

    public FollowerBuilder threeWheelLocalizer(Encoder lEnc, Encoder rEnc, Encoder strafeEnc, ThreeWheelConstants lConstants) {
        return setLocalizer(new ThreeWheelLocalizer(lEnc, rEnc, strafeEnc, lConstants));
    }

    public FollowerBuilder twoWheelLocalizer(ExpansionHubMotor fwdEnc, ExpansionHubMotor strafeEnc, CustomIMU imu, TwoWheelConstants lConstants) {
        return setLocalizer(new TwoWheelLocalizer(fwdEnc, strafeEnc, imu, lConstants));
    }

    public FollowerBuilder twoWheelLocalizer(Encoder fwdEnc, Encoder strafeEnc, CustomIMU imu, TwoWheelConstants lConstants) {
        return setLocalizer(new TwoWheelLocalizer(fwdEnc, strafeEnc, imu, lConstants));
    }

    public FollowerBuilder setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        return this;
    }

    public FollowerBuilder mecanumDrivetrain(A301 lf, A301 lr, A301 rr, A301 rf, MecanumConstants mecanumConstants) {
        return setDrivetrain(new Mecanum(lf, lr, rr, rf, mecanumConstants));
    }

    public FollowerBuilder mecanumDrivetrain(ExpansionHubMotor lf, ExpansionHubMotor lr, ExpansionHubMotor rr, ExpansionHubMotor rf, MecanumConstants mecanumConstants) {
        return setDrivetrain(new Mecanum(lf, lr, rr, rf, mecanumConstants));
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
