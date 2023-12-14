//package org.firstinspires.ftc.teamcode.drive;
//
//
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.geometry.Vector2d;
//import com.acmerobotics.roadrunner.trajectory.Trajectory;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvCameraRotation;
//import org.openftc.easyopencv.OpenCvPipeline;
//
//@Config
//@Autonomous(group = "drive")
//public class Auto extends LinearOpMode {
//    @Override
//    public void runOpMode() throws InterruptedException {
//        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
//
//        Pose2d startPose;
//
//        drive.armState = drive.armState.pickup;
//
//        int startingPosition = 1;
//
//        //trajectories for Red Left side
//        Trajectory RLzone1 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-40,-36), -Math.PI)
//                .build();
//        Trajectory RLzone2 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-40,-36), -Math.PI/2)
//                .build();
//        Trajectory RLzone3 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-40,-36), 0)
//                .build();
//        Trajectory RLzoneUnknown = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-12,-36), 0)
//                .build();
//
//        //trajectories for Red Right side
//        Trajectory RRzone1 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-40,12), -Math.PI)
//                .build();
//        Trajectory RRzone2 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-40,12), -Math.PI/2)
//                .build();
//        Trajectory RRzone3 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-40,12), 0)
//                .build();
//        Trajectory RRzoneUnknown = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(-12,-12), 0)
//                .build();
//
//        //trajectories for Blue Left side
//        Trajectory BLzone1 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(40,12), 0)
//                .build();
//        Trajectory BLzone2 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(40,12), Math.PI/2)
//                .build();
//        Trajectory BLzone3 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(40,12), Math.PI)
//                .build();
//        Trajectory BLzoneUnknown = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(12,-12), 0)
//                .build();
//
//        //trajectories for Blue Right side
//        Trajectory BRzone1 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(40,-36), 0)
//                .build();
//        Trajectory BRzone2 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(40,-36), Math.PI/2)
//                .build();
//        Trajectory BRzone3 = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(40,-36), Math.PI)
//                .build();
//        Trajectory BRzoneUnknown = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(12,-36), 0)
//                .build();
//
//
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        OpenCvCamera cam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
//        OpenCvPipeline pipeline = new SignalPipeline();
//        boolean openCvInitFail[] = {false};
//
//        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
//        {
//            @Override
//            public void onOpened()
//            {
//                // Usually this is where you'll want to start streaming from the camera (see section 4)
//                cam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
//                cam.setPipeline(pipeline);
//            }
//            @Override
//            public void onError(int errorCode)
//            {
//                openCvInitFail[0] = true;
//                /*
//                 * This will be called if the camera could not be opened
//                 */
//            }
//        });
//        cam.showFpsMeterOnViewport(false);
//
//        while(!isStarted()) {
//            //Set position
//            if (gamepad1.a){
//                startingPosition += 1;
//
//                if (startingPosition > 4)
//                {
//                    startingPosition = 1;
//                }
//            }
//
//            //Lock in position
//            if (gamepad1.b) {
//                if (startingPosition == 1) {
//                    startPose = new Pose2d(-64, -36, -Math.PI/2);
//                }
//                else if (startingPosition == 2) {
//                    startPose = new Pose2d(-64, 12, -Math.PI/2);
//                }
//                else if (startingPosition == 3) {
//                    startPose = new Pose2d(64, -36, Math.PI/2);
//                }
//                else {
//                    startPose = new Pose2d(64, 12, Math.PI/2);
//                }
//                drive.setPoseEstimate(startPose);
//            }
//
//            if(openCvInitFail[0]) {
//                telemetry.addLine("WARNING: OpenCV did not open correctly");
//            }
//
//            int zone = SignalPipeline.getAnalysis();
//
//            if (zone == 1) {
//                telemetry.addLine("Zone 1 (LEFT)");
//            }
//            else if (zone == 2) {
//                telemetry.addLine("Zone 2 (MID)");
//            }
//            else if (zone == 3) {
//                telemetry.addLine("Zone 3 (RIGHT)");
//            }
//            else {
//                telemetry.addLine("Zone Unknown");
//            }
//            telemetry.update();
//        }
//
//        if(opModeIsActive()) {
//            int zone = SignalPipeline.getAnalysis();
//
//            Trajectory RLbackBoard = drive.trajectoryBuilder(RLzone2.end())
//                    .lineTo(new Vector2d(-30 - 6 * zone, -36))
//                    .lineTo(new Vector2d(-30 - 6 * zone, 50))
//                    .build();
//            Trajectory RLpark = drive.trajectoryBuilder(RLbackBoard.end())
//                    .splineTo(new Vector2d(-12, 54), 0)
//                    .build();
//
//            Trajectory RRbackBoard = drive.trajectoryBuilder(RRzone2.end())
//                    .lineTo(new Vector2d(-30 - 6 * zone, 12))
//                    .lineTo(new Vector2d(-30 - 6 * zone, 50))
//                    .build();
//            Trajectory RRpark = drive.trajectoryBuilder(RRbackBoard.end())
//                    .splineTo(new Vector2d(-12, 54), 0)
//                    .build();
//
//            Trajectory BLbackBoard = drive.trajectoryBuilder(BLzone2.end())
//                    .lineTo(new Vector2d(30 + 6 * zone, 12))
//                    .lineTo(new Vector2d(30 + 6 * zone, 50))
//                    .build();
//            Trajectory BLpark = drive.trajectoryBuilder(BLbackBoard.end())
//                    .splineTo(new Vector2d(12, 54), 0)
//                    .build();
//
//            Trajectory BRbackBoard = drive.trajectoryBuilder(BRzone2.end())
//                    .lineTo(new Vector2d(30 + 6 * zone, -36))
//                    .lineTo(new Vector2d(30 + 6 * zone, 50))
//                    .build();
//            Trajectory BRpark = drive.trajectoryBuilder(BRbackBoard.end())
//                    .splineTo(new Vector2d(12, -54), 0)
//                    .build();
//
//            if (startingPosition == 1) {
//                //Drive to the correct zone
//                if (zone == 1) {
//                    drive.followTrajectoryAsync(RLzone1);
//                } else if (zone == 2) {
//                    drive.followTrajectoryAsync(RLzone2);
//                } else if (zone == 3) {
//                    drive.followTrajectoryAsync(RLzone3);
//                } else {
//                    drive.followTrajectoryAsync(RLzoneUnknown);
//                }
//            }
//            else if (startingPosition == 2) {
//                //Drive to the correct zone
//                if (zone == 1) {
//                    drive.followTrajectoryAsync(RRzone1);
//                } else if (zone == 2) {
//                    drive.followTrajectoryAsync(RRzone2);
//                } else if (zone == 3) {
//                    drive.followTrajectoryAsync(RRzone3);
//                } else {
//                    drive.followTrajectoryAsync(RRzoneUnknown);
//                }
//            }
//            else if (startingPosition == 3) {
//                //Drive to the correct zone
//                if (zone == 1) {
//                    drive.followTrajectoryAsync(BLzone1);
//                } else if (zone == 2) {
//                    drive.followTrajectoryAsync(BLzone2);
//                } else if (zone == 3) {
//                    drive.followTrajectoryAsync(BLzone3);
//                } else {
//                    drive.followTrajectoryAsync(BLzoneUnknown);
//                }
//            }
//            else {
//                //Drive to the correct zone
//                if (zone == 1) {
//                    drive.followTrajectoryAsync(BRzone1);
//                } else if (zone == 2) {
//                    drive.followTrajectoryAsync(BRzone2);
//                } else if (zone == 3) {
//                    drive.followTrajectoryAsync(BRzone3);
//                } else {
//                    drive.followTrajectoryAsync(BRzoneUnknown);
//                }
//            }
//
//            //bring arm out of chassis
//            while (drive.armBase.getCurrentPosition() > -420) {
//                drive.updateArm();
//            }
//
//            //place pixel
//            drive.armBase.setTargetPosition(-1800);
//            drive.clawArm.setPosition(0.6);
//            sleep(500);
//            drive.claw.setPosition(0.9);
//
//            //bring arm back into chassis
//            drive.armBase.setTargetPosition(40);
//            drive.clawArm.setPosition(0.18);
//            drive.armState = drive.armState.pickup;
//
//            //intake second pixel
//            drive.intake.setPower(1);
//            sleep(200);
//            drive.intake.setPower(0);
//
//            //Drive to the backboard and the correct pix pos
//            drive.followTrajectory(RLbackBoard);
//
//            drive.updateArm();
//
//            //wait for arm to finish moving
//            while (drive.armBase.isBusy()) {
//            }
//            //move arm to backboard
//            drive.armBase.setTargetPosition(-800);
//            drive.clawArm.setPosition(0.5);
//            while (drive.armBase.isBusy()) {
//            }
//            //drop pixel
//            drive.claw.setPosition(0.9);
//            sleep(100);
//
//            //bring arm back into chassis
//            drive.armBase.setTargetPosition(40);
//            drive.clawArm.setPosition(0.18);
//
//            //Park
//            drive.followTrajectoryAsync(RLpark);
//
//        }
//
//    }
//}
