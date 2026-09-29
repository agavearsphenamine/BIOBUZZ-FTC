package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "outTakeTest")
public class outTakeTest extends LinearOpMode {

    double speedCoefficient = 1;

    DcMotor shoot;

    @Override
    public void runOpMode() throws InterruptedException {

        shoot = hardwareMap.get(DcMotor.class, "shoot");
        shoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoot.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            if (gamepad1.y) {
                shoot.setPower(speedCoefficient);
            } else {
                shoot.setPower(0);
            }

            telemetry.addLine("Press y to run the motor");

            telemetry.update();
        }
    }
}