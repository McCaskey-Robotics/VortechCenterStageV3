package org.firstinspires.ftc.teamcode.drive;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

public class SignalDetection extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

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
                cam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
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



    }

}
