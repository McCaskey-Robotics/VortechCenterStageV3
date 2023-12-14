package org.firstinspires.ftc.teamcode.drive;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

@Config
@Autonomous(group = "drive")
public class BlueLeftAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(12, 64, Math.PI/2);;

        drive.armState = drive.armState.pickup;

        //trajectories for Blue left side
        Trajectory zone1 = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(12,40), 0)
                .build();
        Trajectory zone2 = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(12,40), Math.PI/2)
                .build();
        Trajectory zone3 = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(12,40), Math.PI)
                .build();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        OpenCvCamera cam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        OpenCvPipeline pipeline = new SignalPipeline();
        boolean openCvInitFail[] = {false};

        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                cam.startStreaming(1280, 720, OpenCvCameraRotation.UPSIDE_DOWN);
                cam.setPipeline(pipeline);
            }
            @Override
            public void onError(int errorCode)
            {
                openCvInitFail[0] = true;
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
        cam.showFpsMeterOnViewport(false);

        while(!isStarted()) {
            if(openCvInitFail[0]) {
                telemetry.addLine("WARNING: OpenCV did not open correctly");
            }

            int zone = SignalPipeline.getAnalysis();

            if (zone == 1) {
                telemetry.addLine("Zone 1 (LEFT)");
            }
            else if (zone == 2) {
                telemetry.addLine("Zone 2 (MID)");
            }
            else if (zone == 3) {
                telemetry.addLine("Zone 3 (RIGHT)");
            }
            else {
                telemetry.addLine("Zone Unknown");
            }
            telemetry.update();
        }

        if(opModeIsActive()) {
            int zone = SignalPipeline.getAnalysis();

            Trajectory backBoard = null;

            if (zone == 1) {
                backBoard = drive.trajectoryBuilder(zone1.end())
                        .lineTo(new Vector2d(12, 54 - 6 * zone))
                        .lineTo(new Vector2d(50,54 - 6 * zone))
                        .build();

                drive.followTrajectory(zone1);
            } else if (zone == 2) {
                backBoard = drive.trajectoryBuilder(zone2.end())
                        .lineTo(new Vector2d(12, 54 - 6 * zone))
                        .lineTo(new Vector2d(50, 54 - 6 * zone))
                        .build();

                drive.followTrajectory(zone2);
            } else if (zone == 3) {
                backBoard = drive.trajectoryBuilder(zone3.end())
                        .lineTo(new Vector2d(12, 54 - 6 * zone))
                        .lineTo(new Vector2d(50, 54 - 6 * zone))
                        .build();

                drive.followTrajectory(zone3);
            } else {
                backBoard = drive.trajectoryBuilder(zone2.end())
                        .lineTo(new Vector2d(12,54 - 6 * zone))
                        .lineTo(new Vector2d(50,54 - 6 * zone))
                        .build();

                drive.followTrajectory(zone2);
            }

            Trajectory park = drive.trajectoryBuilder(backBoard.end())
                    .splineTo(new Vector2d(54, 12), 0)
                    .build();

            //bring arm out of chassis
            while (drive.armBase.getCurrentPosition() > -420) {
                drive.updateArm();
            }

            //place pixel
            drive.armBase.setTargetPosition(-1800);
            while (drive.armBase.isBusy()){} //Wait for arm to move
            drive.clawArm.setPosition(0.6);
            sleep(500);
            drive.claw.setPosition(0.9);

            //bring arm back into chassis
            drive.armBase.setTargetPosition(-350);
            drive.clawArm.setPosition(0);
            drive.armState = drive.armState.initial;

            //intake second pixel
            drive.intake.setPower(1);
            sleep(200);
            drive.intake.setPower(0);

            //Drive to the backboard and the correct pix pos
            drive.followTrajectory(backBoard);

            while (drive.armBase.getCurrentPosition() > -420) {
                drive.updateArm();
            }

            //move arm to backboard
            drive.armBase.setTargetPosition(-800);
            drive.clawArm.setPosition(0.5);
            while (drive.armBase.isBusy()) {} //Wait for arm to move
            //drop pixel
            drive.claw.setPosition(0.9);
            sleep(100);

            //bring arm back into chassis
            drive.armBase.setTargetPosition(-350);
            drive.clawArm.setPosition(0);

            //Park
            drive.followTrajectory(park);
        }

    }
}
