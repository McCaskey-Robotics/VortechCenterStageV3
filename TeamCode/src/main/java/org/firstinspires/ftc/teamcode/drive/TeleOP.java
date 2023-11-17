package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(group = "drive")
public class TeleOP extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();
        while (!isStopRequested()){
            drive.setWeightedDrivePower(
                    new Pose2d(
                            gamepad1.left_stick_y / 2,
                            gamepad1.left_stick_x / 2,
                            -gamepad1.right_stick_x / 2
                    )
            );

            //Set power to lift
            if (gamepad1.b) {
                drive.lift.setTargetPosition(13500);
                drive.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                drive.lift.setPower(1);
            }
            else if (gamepad1.a) {
                drive.lift.setTargetPosition(100);
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
            if (gamepad1.dpad_up){
                drive.armBase.setPower(0.3);
            }
            else if (gamepad1.dpad_down){
                drive.armBase.setPower(-0.3);
            }
            else {
                drive.armBase.setPower(0);
            }

            if (gamepad2.a){
                drive.claw.setPosition(1);
            }
            else if (gamepad2.b) {
                drive.claw.setPosition(0);
            }

            drive.clawArm.setPosition(drive.clawArm.getPosition() + gamepad2.left_stick_y / 100);

            telemetry.addData("Lift Pos", drive.lift.getCurrentPosition());
            telemetry.addData("Lift Target", drive.lift.getTargetPosition());
            telemetry.update();
        }
    }
}
