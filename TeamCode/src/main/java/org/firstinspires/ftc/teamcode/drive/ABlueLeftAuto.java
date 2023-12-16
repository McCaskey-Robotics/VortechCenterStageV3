package org.firstinspires.ftc.teamcode.drive;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

@Config
@Autonomous(group = "drive")
public class ABlueLeftAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(12, 64,-Math.PI/2);;
        drive.setPoseEstimate(startPose);

        drive.armState = drive.armState.pickup;

        //trajectories for Blue Right side
        //Zone 2 trajectories
        Trajectory traj1 = drive.trajectoryBuilder(startPose)
                .lineTo(new Vector2d(12,32)) //drive to tape
                .build();
        TrajectorySequence traj12 = drive.trajectorySequenceBuilder(traj1.end())
                .lineTo(new Vector2d(12, 38)) //reset to center of square
                .turn(Math.PI/2)
                .lineTo(new Vector2d(12, 64))
                .lineTo(new Vector2d(62, 64)) //park
                .build();
        //Zone 1 trajectories
        TrajectorySequence traj2 = drive.trajectorySequenceBuilder(traj1.end())
                .turn(Math.PI/2)
                .lineTo(new Vector2d(16, 32))
                .build();
        TrajectorySequence traj22 = drive.trajectorySequenceBuilder(traj2.end())
                .lineTo(new Vector2d(12,32))
                .lineTo(new Vector2d(12, 64))
                .lineTo(new Vector2d(62, 64))
                .build();
        //Zone 3 trajectories
        TrajectorySequence traj3 = drive.trajectorySequenceBuilder(traj1.end())
                .turn(-Math.PI/2)
                .lineTo(new Vector2d(11, 32))
                .build();
        TrajectorySequence traj32 = drive.trajectorySequenceBuilder(traj3.end())
                .lineTo(new Vector2d(14, 32))
                .lineTo(new Vector2d(14, 64))
                .lineTo(new Vector2d(62,64))
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

            drive.followTrajectory(traj1);

            //Turn to correct zone
            if (zone == 1) {
                drive.followTrajectorySequence(traj2);
            } else if (zone == 3) {
                drive.followTrajectorySequence(traj3);
            }

            //place pixel
            drive.intake.setPower(-0.3);
            sleep(1000);
            drive.intake.setPower(0);

            if (zone == 1) {
                drive.followTrajectorySequence(traj22);
            } else if (zone == 2) {
                drive.followTrajectorySequence(traj12);
            }
            else if (zone == 3) {
                drive.followTrajectorySequence(traj32);
            }
        }
    }
}
