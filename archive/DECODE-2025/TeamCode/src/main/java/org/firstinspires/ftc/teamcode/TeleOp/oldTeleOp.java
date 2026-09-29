//package org.firstinspires.ftc.teamcode.TeleOp;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.geometry.Vector2d;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Gamepad;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Position;
//import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
//import org.firstinspires.ftc.vision.VisionPortal;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//
//import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
//
//@Disabled
//@TeleOp(name = "TeleOp2526")
//public class oldTeleOp extends LinearOpMode {
//
//    //Variables
//    double speedCoefficient = 0.75;
//    double turningCoefficient = 0.5;
//
//    private SampleMecanumDrive drive;
//
//    private boolean isForward = false;
//
//    DcMotor intakeRoller;
//
//    private VisionPortal visionPortal;
//    private AprilTagProcessor aprilTag;
//
//    private Position cameraPosition = new Position(DistanceUnit.INCH,
//            0, 0, 0, 0);
//    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
//            0, -90, 0, 0);
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//        intakeRoller = hardwareMap.get(DcMotor.class, "Goonmaster67");
//        intakeRoller.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        drive = new SampleMecanumDrive(hardwareMap);
//        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        // Create AprilTag processor
//        aprilTag = new AprilTagProcessor.Builder()
//                .setCameraPose(cameraPosition, cameraOrientation)
//                .setLensIntrinsics(1416.5635739, 1416.5635739, 643.596635748, 365.63416356)
//                .build();
//
//        // Connect to Logitech webcam
//        visionPortal = new VisionPortal.Builder()
//                .setCamera(hardwareMap.get(WebcamName.class, "Gooncam 1"))
//                .addProcessor(aprilTag)
//                .build();
//
//        telemetry.addLine("Initialized — waiting for start");
//        telemetry.update();
//
//        waitForStart();
//
//        if (isStopRequested()) return;
//
//        while (opModeIsActive() && !isStopRequested()) {
//                //orienting the robot for field centric
//                if(!isForward){
//                    orienting(gamepad1);
//                    telemetry.update();
//                    if (gamepad1.y) {
//                        drive.setPoseEstimate(new Pose2d(
//                                drive.getPoseEstimate().getX(),
//                                drive.getPoseEstimate().getY(),
//                                0 // reset heading
//                        ));
//                        isForward = true;
//                    }
//                    if (gamepad1.b){
//                        intakeRoller.setPower(0.5);
//                    } else{
//                        intakeRoller.setPower(0);
//                    }
//                    AprilTagDetection tag = aprilTag.getDetections().get(0);
//
//                    double x = tag.ftcPose.x;      // forward/back distance (inches)
//                    double y = tag.ftcPose.y;      // left/right offset (inches)
//                    double heading = tag.ftcPose.yaw; // rotation (degrees)
//
//                    telemetry.addData("Tag ID", tag.id);
//                    telemetry.addData("X (Forward)", "%.2f in", x);
//                    telemetry.addData("Y (Strafe)", "%.2f in", y);
//                    telemetry.addData("Heading", "%.1f°", heading);
//
//                    telemetry.update();
//                }
//
//                else{
//                    fieldCentricDrive(gamepad1);
//                }
//        }
//    }
//    public void fieldCentricDrive(Gamepad gamepad){
//
//        //speed boost when holding the "a" button
//        if(gamepad.a){
//            speedCoefficient = 0.9;
//            turningCoefficient = 0.9;
//        }
//        else{
//            speedCoefficient = 0.7;
//            turningCoefficient = 0.5;
//        }
//
//        // Read pose
//        Pose2d poseEstimate = drive.getPoseEstimate();
//
//
//        // Create a vector from the gamepad x/y inputs
//        // Then, rotate that vector by the inverse of that heading
//        Vector2d input = new Vector2d(
//                -gamepad.left_stick_y * speedCoefficient,
//                -gamepad.left_stick_x * speedCoefficient
//        ).rotated(-poseEstimate.getHeading());
//
//        // Pass in the rotated input + right stick value for rotation
//        // Rotation is not part of the rotated input thus must be passed in separately
//        drive.setWeightedDrivePower(
//                new Pose2d(
//                        input.getX(),
//                        input.getY(),
//                        -gamepad.right_stick_x * turningCoefficient
//                )
//        );
//
//        // Update everything. Odometry. Etc.
//        drive.update();
//
//        telemetry.addData("Mode", "Field-Centric");
//        telemetry.addData("X", poseEstimate.getX());
//        telemetry.addData("Y", poseEstimate.getY());
//        telemetry.addData("Heading (radians)", poseEstimate.getHeading());
//        telemetry.addData("Heading (degrees)", Math.toDegrees(poseEstimate.getHeading()));
//        telemetry.update();
//    }
//
//
//    public void orienting(Gamepad gamepad) {
//        drive.setWeightedDrivePower(
//                new Pose2d(
//                        -gamepad.left_stick_y * 0.5,
//                        -gamepad.left_stick_x * 0.5,
//                        -gamepad.right_stick_x * 0.4
//                )
//        );
//        drive.update();
//    }
//}
