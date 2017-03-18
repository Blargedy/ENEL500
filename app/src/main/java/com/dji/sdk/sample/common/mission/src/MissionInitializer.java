package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.mission.api.I_CameraInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionInitializer;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-16.
 */

public class MissionInitializer implements
        I_MissionInitializer,
        I_CompletionCallback
{
    private static final String TAG = "HydraMissionInitializer";

    private enum ExpectedCallback { SET_HOME_LOCATION, INITIALIZE_CAMERA}
    private MissionInitializer.ExpectedCallback expectedCallback_;

    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerSource flightControllerSource_;
    private I_CameraInitializer cameraInitializer_;
    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;
    private I_CompletionCallback missionExecutionCompletionCallback_;
    private I_WaypointMissionProgressStatusCallback missionProgressStatusCallback_;

    private I_CompletionCallback callback_;

    public MissionInitializer(
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerSource flightControllerSource,
            I_CameraInitializer cameraInitializer,
            I_ImageTransferModuleInitializer imageTransferModuleInitializer,
            I_CompletionCallback missionExecutionCompletionCallback,
            I_WaypointMissionProgressStatusCallback missionProgressStatusCallback)
    {
        missionManagerSource_ = missionManagerSource;
        flightControllerSource_ = flightControllerSource;
        cameraInitializer_ = cameraInitializer;
        imageTransferModuleInitializer_ = imageTransferModuleInitializer;
        missionExecutionCompletionCallback_ = missionExecutionCompletionCallback;
        missionProgressStatusCallback_ = missionProgressStatusCallback;
    }

    public void initializeMissionPriorToTakeoff(I_CompletionCallback callback)
    {
        callback_ = callback;

        I_MissionManager missionManager = missionManagerSource_.getMissionManager();
        missionManager.setMissionExecutionFinishedCallback(missionExecutionCompletionCallback_);
        missionManager.setMissionProgressStatusCallback(missionProgressStatusCallback_);

        imageTransferModuleInitializer_.initializeImageTransferModule();

        expectedCallback_ = MissionInitializer.ExpectedCallback.SET_HOME_LOCATION;
        flightControllerSource_.getFlightController().setHomeLocationUsingAircraftCurrentLocation(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case SET_HOME_LOCATION:
                    expectedCallback_ = ExpectedCallback.INITIALIZE_CAMERA;
                    cameraInitializer_.initializeCameraForMission(this);
                    break;
                case INITIALIZE_CAMERA:
                    callback(null);
                    break;
                default:
                    break;
            }
        }
        else
        {
            Log.e(TAG, error.toString());
            callback_.onResult(error);
        }
    }

    private void callback(DJIError error)
    {
        if (callback_ != null)
        {
            callback_.onResult(error);
        }
    }
}
