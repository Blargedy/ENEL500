package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;
import com.dji.sdk.sample.common.integration.DJISampleApplication;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.MissionManager;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.missionmanager.DJIMissionManager;

/**
 * Created by eric7 on 2017-02-21.
 */

public class StepCompletionCallback implements I_CompletionCallback{
    private I_MissionController controller_;
    private I_ApplicationContextManager contextManager_;
    private I_ImageTransferer imageTransferer_;
    private int waypointCounter_;

    public StepCompletionCallback(
            I_MissionController controller,
            I_ImageTransferer imageTransferer,
            I_ApplicationContextManager contextManager)
    {
        controller_ = controller;
        imageTransferer_ = imageTransferer;
        contextManager_ = contextManager;
        waypointCounter_ = 0;
    }

    public void onResult(DJIError error) {
        if (error == null) {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Successfully reached Waypoint "+Integer.toString(waypointCounter_), Toast.LENGTH_SHORT).show();
            handleWaypointReached(waypointCounter_);
            waypointCounter_++;

        } else {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "could not reach waypoint "+Integer.toString(waypointCounter_)+". "+error.getDescription(), Toast.LENGTH_LONG).show();
        }
    }

    void handleWaypointReached(int waypointCount)
    {
//        DJIBaseProduct product = DJISampleApplication.getProductInstance();
//        DJICamera camera = product.getCamera();
//        camera.setCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto, null);
//        camera.startShootPhoto(DJICameraSettingsDef.CameraShootPhotoMode.Single, MissionHelper.completionCallback(contextManager_, "Shot photo", "Could not shoot photo"));
//        camera.stopShootPhoto(null);

        //pause the mission every 5 waypoints
        if(waypointCount%5 == 0)
        {
            controller_.pauseMission();
            imageTransferer_.transferNewImagesFromDrone();
        }
    }

}

