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

        boolean pressed = false;

        waitForStart();
        while (!isStopRequested()){
            if (gamepad1.left_stick_button){
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x,
                                -gamepad1.right_stick_x
                        )
                );
            }
            else {
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y/2,
                                -gamepad1.left_stick_x/2,
                                -gamepad1.right_stick_x/2
                        )
                );
            }


            //Set power to lift
            if (gamepad1.b) {
                drive.lift.setTargetPosition(13000);
                drive.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                drive.lift.setPower(1);
            }
            else if (gamepad1.a) {
                drive.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                drive.lift.setPower(-1);
            }
            else if (gamepad1.x) {
                drive.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                drive.lift.setPower(1);
            }
            else if (drive.lift.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                drive.lift.setPower(0);
            }

            //Set power to intake
            if (gamepad1.right_bumper) {
                drive.intake.setPower(-gamepad1.right_trigger/1.5);
            }
            else {
                drive.intake.setPower(gamepad1.right_trigger/1.5);
            }
            //Set power to arm
            if (drive.armState == drive.armState.manual){
                drive.armBase.setPower(gamepad2.right_stick_y / 2);
            }

            //set claw open and closed
            if (gamepad2.a){
                drive.claw.setPosition(0.4);
            }
            else if (gamepad2.b) {
                drive.claw.setPosition(0);
            }

            //set and release airplane launcher
            if (gamepad1.dpad_up) {
                drive.launcher.setPosition(0.42);
            }
            else if (gamepad1.dpad_down) {
                drive.launcher.setPosition(0.6);
            }

            //rotate claw forward and backwards
            if (gamepad2.left_stick_y > 0.1) {
                drive.clawArm.setPosition(drive.clawArm.getPosition() - 0.005*gamepad2.left_stick_y);
            }
            else if (gamepad2.left_stick_y < -0.1 && drive.clawArm.getPosition() < 0.8) {
                drive.clawArm.setPosition(drive.clawArm.getPosition() - 0.005*gamepad2.left_stick_y);
            }

            //switch arm mode between auto + manual
            if (gamepad2.right_bumper) {
                drive.armState = drive.armState.initial;
            }
            if (gamepad2.left_bumper){
                drive.armState = drive.armState.manual;
            }

            if (gamepad2.dpad_left) {
                drive.armBase.setTargetPosition(-1100);
                drive.armBase.setPower(1);
                drive.clawArm.setPosition(0.57);
            }

            //Arm automation
            drive.updateArm();

            //reset arm encoder
            if (gamepad2.y){
                drive.armBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                drive.armBase.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (gamepad2.dpad_up) {
                drive.armResetter.setPosition(0);
            }
            if (gamepad2.dpad_down) {
                drive.armResetter.setPosition(0.19);
            }

            telemetry.addData("Arm Pos", drive.armBase.getCurrentPosition());
            telemetry.addData("Claw Pos", drive.claw.getPosition());
            telemetry.addData("Claw Angle Pos", drive.clawArm.getPosition());
            telemetry.addData("lift pos", drive.lift.getCurrentPosition());
            telemetry.addData("state", drive.armState);
            telemetry.addData("Arm mode", drive.armBase.getMode());
            telemetry.addData("Resetter", drive.armResetter.getPosition());
            telemetry.update();
        }
    }
}
