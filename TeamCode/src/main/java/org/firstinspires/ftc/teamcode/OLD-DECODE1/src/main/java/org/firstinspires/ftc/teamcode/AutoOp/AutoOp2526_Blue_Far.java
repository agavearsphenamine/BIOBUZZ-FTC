
package org.firstinspires.ftc.teamcode.AutoOp;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutoOp Blue Far", group = "Autonomous")
@Configurable // Panels
public class AutoOp2526_Blue_Far extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class

    DcMotor intake1;
    DcMotor intake2;
    DcMotor shoot;
    CRServo intake3;
    private Timer pathTimer, actionTimer, opmodeTimer;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(96.000, 8.191, Math.toRadians(90)));

        paths = new Paths(follower); // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        intake1 = hardwareMap.get(DcMotor.class, "intake1");
        intake2 = hardwareMap.get(DcMotor.class, "intake2");
        shoot = hardwareMap.get(DcMotor.class, "shoot");
        intake3 = hardwareMap.get(CRServo.class, "intake3");


        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake3.setDirection(DcMotorSimple.Direction.REVERSE); //REVERSE SERVO IF NEEDED

        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        pathState = 0;
        pathTimer = new Timer();
        actionTimer = new Timer();

    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }


    public static class Paths {
        public PathChain ToShootPose;
        public PathChain PickUpArtifacts;
        public PathChain ToArtifactPose;
        public PathChain BackToShootPose;

        public Paths(Follower follower) {
            ToShootPose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.000, 8.191),

                                    new Pose(96.655, 96.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(45))

                    .build();

            ToArtifactPose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.655, 96.000),

                                    new Pose(96.689, 83.721)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(0))

                    .build();

            PickUpArtifacts = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.689, 83.721),

                                    new Pose(129.256, 83.721)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();

            BackToShootPose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(129.256, 83.721),

                                    new Pose(96.655, 96.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))

                    .build();
        }
    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Start path ONCE
                if (pathTimer.getElapsedTimeSeconds() == 0) {
                    follower.followPath(paths.ToShootPose);
                    pathTimer.resetTimer();
                }
                // Rev shooter the entire time
                shoot.setPower(1.0);

                // If robot is STILL driving → do NOT feed
                if (follower.isBusy()) {
                    intake1.setPower(0);
                    intake2.setPower(0);
                    intake3.setPower(0);
                }
                // Once path finishes → shoot for 0.2 seconds
                else {
                    // Start feed timer once
                    if (!actionTimer.isRunning()) {
                        actionTimer.resetTimer();
                    }

                    intake1.setPower(1.0);
                    intake2.setPower(1.0);
                    intake3.setPower(1.0);

                    if (actionTimer.getElapsedTimeSeconds() >= 1.2) {

                        // Stop everything
                        intake1.setPower(0);
                        intake2.setPower(0);
                        intake3.setPower(0);
                        shoot.setPower(0);

                        // Reset timers and move on
                        pathTimer.resetTimer();
                        actionTimer.resetTimer();
                        setPathState(1);
                    }

                }
                break;

            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    follower.followPath(paths.ToArtifactPose);
                    actionTimer.resetTimer();
                    setPathState(2);
                }
                break;

            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the case 1's position */
                if(!follower.isBusy()) {
                    // Start shooter + feeder
                    shoot.setPower(1.0);
                    intake1.setPower(1.0);
                    intake2.setPower(1.0);
                    intake3.setPower(1.0);

                    // Start timer once
                    if (!actionTimer.isRunning()) {  //TODO FIX THIS SHIT
                        actionTimer.resetTimer();
                    }

                    // Shoot for 1.2 seconds
                    if (actionTimer.getElapsedTimeSeconds() > 1.2) {

                        // Stop everything
                        shoot.setPower(0);
                        intake1.setPower(0);
                        intake2.setPower(0);
                        intake3.setPower(0);

                        // Go to next path
                        follower.followPath(paths.PickUpArtifacts);
                        actionTimer.resetTimer();
                        setPathState(3);
                    }
                }
                break;

            case 3:
                // Start path ONCE
                if (pathTimer.getElapsedTimeSeconds() == 0) {
                    follower.followPath(paths.BackToShootPose);
                    pathTimer.resetTimer();
                }
                // Rev shooter the entire time
                shoot.setPower(1.0);

                // If robot is STILL driving → do NOT feed
                if (follower.isBusy()) {
                    intake1.setPower(0);
                    intake2.setPower(0);
                    intake3.setPower(0);
                }
                // Once path finishes → shoot for 0.2 seconds
                else {
                    // Start feed timer once
                    if (!actionTimer.isRunning()) {
                        actionTimer.resetTimer();
                    }

                    intake1.setPower(1.0);
                    intake2.setPower(1.0);
                    intake3.setPower(1.0);

                    if (actionTimer.getElapsedTimeSeconds() >= 1.2) {

                        // Stop everything
                        intake1.setPower(0);
                        intake2.setPower(0);
                        intake3.setPower(0);
                        shoot.setPower(0);

                        // Reset timers and move on
                        pathTimer.resetTimer();
                        actionTimer.resetTimer();
                        setPathState(99);
                    }
                }
                break;
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
    }
}
    