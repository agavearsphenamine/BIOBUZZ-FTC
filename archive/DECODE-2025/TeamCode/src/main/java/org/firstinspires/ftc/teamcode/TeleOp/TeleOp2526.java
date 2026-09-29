package org.firstinspires.ftc.teamcode.TeleOp;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

@Configurable
@TeleOp
public class TeleOp2526 extends OpMode {
    private Follower follower;
    public static Pose startingPose; //See ExampleAuto to understand how to use this
    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;

    public boolean isRobotCentric = true;

    DcMotor intake1;
    DcMotor intake2;
    DcMotor shoot;
    CRServo intake3;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                .build();

        intake1 = hardwareMap.get(DcMotor.class, "intake1");//front_INT
        intake2 = hardwareMap.get(DcMotor.class, "intake2");//middle_INT
        shoot = hardwareMap.get(DcMotor.class, "shoot");//kill
        intake3 = hardwareMap.get(CRServo.class, "intake3");//back_INT


        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake3.setDirection(DcMotorSimple.Direction.REVERSE); //REVERSE SERVO IF NEEDED
    }

    @Override
    public void start() {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        //Call this once per loop
        follower.update();
        telemetryM.update();

        if (!automatedDrive) {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors

            //This is the normal version to use in the TeleOp
            if (isRobotCentric) follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,  //og y
                    -gamepad1.left_stick_x,  //og x
                    -gamepad1.right_stick_x,
                    true // Robot Centric = true; Field Centric = false
            );

            if (!isRobotCentric) follower.setTeleOpDrive(
                    gamepad1.left_stick_x,
                    -gamepad1.left_stick_y,
                    gamepad1.right_stick_x,
                    false // Robot Centric = true; Field Centric = false
            );
        }

        //Shoot
        if (gamepad1.right_trigger != 0){
            shoot.setPower(1);
        } else {
            shoot.setPower(0);
        }

        //Intake 1
        if (gamepad1.left_trigger != 0){
            intake1.setPower(1);
        } else {
            intake1.setPower(0);
        }

        //Intake 3 (servo)
        if (gamepad1.right_bumper) {
            intake3.setPower(1);
        } else {
            intake3.setPower(0);
        }

        //Intake 2
        if (gamepad1.left_bumper) {
            intake2.setPower(1);
        } else {
            intake2.setPower(0);
        }

        if (gamepad1.aWasPressed()){
            isRobotCentric = !isRobotCentric;
        }

        // Reset field-centric heading
        if (gamepad1.yWasPressed()) {
            Pose pose = follower.getPose();

            follower.setStartingPose(new Pose(
                    pose.getX(),
                    pose.getY(),
                    Math.toRadians(90)
            ));
        }

        telemetry.addData("Drive Mode", isRobotCentric ? "ROBOT" : "FIELD");
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("LS Y", gamepad1.left_stick_y);
        telemetry.addData("LS X", gamepad1.left_stick_x);
        telemetry.addData("RS X", gamepad1.right_stick_x);


        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);
    }
}