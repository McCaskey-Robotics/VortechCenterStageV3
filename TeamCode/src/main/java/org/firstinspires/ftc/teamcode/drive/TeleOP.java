package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(group = "drive")
public class TeleOP extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();
        while (!isStopRequested()){
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            //Set power to lift
            if (gamepad1.b) {
                drive.lift.setTargetPosition(13000);
                drive.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                drive.lift.setPower(1);
            }
            else if (gamepad1.a) {
                drive.lift.setTargetPosition(-500);
                drive.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                drive.lift.setPower(1);
            }

            //Set power to intake
            if (gamepad1.right_bumper) {
                drive.intake.setPower(-gamepad1.right_trigger);
            }
            else {
                drive.intake.setPower(gamepad1.right_trigger);
            }

            //Set power to arm
            drive.armBase.setPower(gamepad2.right_stick_y / 2);

            //set claw open and closed
            if (gamepad2.a){
                drive.claw.setPosition(0.9);
            }
            else if (gamepad2.b) {
                drive.claw.setPosition(0);
            }

            //set and release airplane launcher
            if (gamepad2.dpad_up) {
                drive.launcher.setPosition(0.42);
            }
            else if (gamepad2.dpad_down) {
                drive.launcher.setPosition(0.6);
            }

            //rotate claw forward and backwards
            if (gamepad2.left_stick_y > 0.1) {
                drive.clawArm.setPosition(drive.clawArm.getPosition() - 0.003*gamepad2.left_stick_y);
            }
            else if (gamepad2.left_stick_y < -0.1 && drive.clawArm.getPosition() < 0.8) {
                drive.clawArm.setPosition(drive.clawArm.getPosition() - 0.003*gamepad2.left_stick_y);
            }

            //switch arm mode between auto + manual
            if (gamepad2.x){
                if (drive.armState == drive.armState.manual){
                    drive.armState = drive.armState.prePickup;
                }
                else {
                    drive.armState = drive.armState.manual;
                }
            }
            //Arm automation
            drive.updateArm(gamepad2.x);

            telemetry.addData("Arm Pos", drive.armBase.getCurrentPosition());
            telemetry.addData("Target Arm Pos", drive.armBase.getTargetPosition());
            telemetry.addData("Claw Pos", drive.clawArm.getPosition());
            telemetry.addData("lift pos", drive.lift.getCurrentPosition());
            telemetry.update();
        }
    }
}
