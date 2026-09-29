package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(11)
            .forwardZeroPowerAcceleration(-43.98155153662517) //-43.848058992663645, -47.05569846846624, -41.06663248224214, -46.46293511824804, -42.14785975999651, -42.63862697134482, -44.44080287629499, -44.6915501060448, -44.6338373651598, -42.82951322579069
            .lateralZeroPowerAcceleration(-73.90779546781876) //-70.42396504293438, -61.017875334128625, -81.14430468008264, -71.31969320034126, -74.66452430164902, -84.8764102477766
            .translationalPIDFCoefficients(new PIDFCoefficients(0.087, 0, 0.01, 0.068))

            ;

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .threeWheelIMULocalizer(localizerConstants)
                .build();
    }

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("rightFront")
            .rightRearMotorName("rightRear")
            .leftRearMotorName("leftRear")
            .leftFrontMotorName("leftFront")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(70.19419912658608) //70.01101879821724, 70.59447481209712, 70.01908795116734, 70.10247205868797, 70.24394201276081
            .yVelocity(47.14477082460436); //46.21220386798127, 47.121143824362335, 47.09236769691156, 46.952195952614915, 48.34594278115172

    public static ThreeWheelIMUConstants localizerConstants = new ThreeWheelIMUConstants()
            .forwardTicksToInches(.001989436789)
            .strafeTicksToInches(.001989436789)
            .turnTicksToInches(.001989436789)
            .leftPodY(7)
            .rightPodY(-7)
            .strafePodX(-7)
            .leftEncoder_HardwareMapName("rightFront")
            .rightEncoder_HardwareMapName("leftRear")
            .strafeEncoder_HardwareMapName("rightRear")
            .leftEncoderDirection(Encoder.FORWARD)
            .rightEncoderDirection(Encoder.REVERSE)
            .strafeEncoderDirection(Encoder.REVERSE)
            .IMU_HardwareMapName("imu")
            .IMU_Orientation(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.UP))
            .forwardTicksToInches(0.0029565397227416853) // 0.0029429295094910786, 0.002963023787206046, 0.002949804696456857, 0.0029688001733392004, 0.0029581404472152437
            .strafeTicksToInches(0.002983742770319779) // 0.002966550419388064, 0.0030116177881928507, 0.0030116177881928507, 0.002968331692138714, 0.002960596163686416
            .turnTicksToInches(0.0019714297883190787) // 0.0019808256860207613, 0.00196210993446714, 0.0019624203758470014, 0.0019802023861064007, 0.00197159055915409
            ;
}

