package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MotorDirectionDebugger extends OpMode {
    DcMotor rightRear;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor leftFront;

    @Override
    public void init() {
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");

        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        if (gamepad1.a){
            rightRear.setPower(1);
        } else {
            rightRear.setPower(0);
        }

        if (gamepad1.b){
            rightFront.setPower(1);
        } else {
            rightFront.setPower(0);
        }

        if (gamepad1.y){
            leftRear.setPower(1);
        } else {
            leftRear.setPower(0);
        }

        if (gamepad1.x){
            leftFront.setPower(1);
        } else {
            leftFront.setPower(0);
        }
    }
}
