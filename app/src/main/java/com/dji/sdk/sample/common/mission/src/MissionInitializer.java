package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_BatterySource;
import com.dji.sdk.sample.common.integration.api.I_BatteryStateUpdateCallback;
import com.dji.sdk.sample.common.droneState.api.I_CameraInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.droneState.api.I_FlightControllerInitializer;
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

    private enum ExpectedCallback { INITIALIZE_FLIGHT_CONTROLLER, INITIALIZE_CAMERA}
    private ExpectedCallback expectedCallback_;

    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerInitializer flightControllerInitializer_;
    private I_CameraInitializer cameraInitializer_;
    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;
    private I_CompletionCallback missionExecutionCompletionCallback_;
    private I_WaypointMissionProgressStatusCallback missionProgressStatusCallback_;
    private I_BatterySource batterySource_;
    private I_BatteryStateUpdateCallback batteryStateUpdateCallback_;

    private I_CompletionCallback callback_;

    public MissionInitializer(
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerInitializer flightControllerInitializer,
            I_CameraInitializer cameraInitializer,
            I_ImageTransferModuleInitializer imageTransferModuleInitializer,
            I_CompletionCallback missionExecutionCompletionCallback,
            I_WaypointMissionProgressStatusCallback missionProgressStatusCallback,
            I_BatterySource batterySource,
            I_BatteryStateUpdateCallback batteryStateUpdateCallback)
    {
        missionManagerSource_ = missionManagerSource;
        flightControllerInitializer_ = flightControllerInitializer;
        cameraInitializer_ = cameraInitializer;
        imageTransferModuleInitializer_ = imageTransferModuleInitializer;
        missionExecutionCompletionCallback_ = missionExecutionCompletionCallback;
        missionProgressStatusCallback_ = missionProgressStatusCallback;
        batterySource_ = batterySource;
        batteryStateUpdateCallback_ = batteryStateUpdateCallback;
    }

    public void initializeMissionPriorToTakeoff(I_CompletionCallback callback)
    {
        callback_ = callback;

        I_MissionManager missionManager = missionManagerSource_.getMissionManager();
        missionManager.setMissionExecutionFinishedCallback(missionExecutionCompletionCallback_);
        missionManager.setMissionProgressStatusCallback(missionProgressStatusCallback_);

        imageTransferModuleInitializer_.initializeImageTransferModule();

        batterySource_.getBattery().setBatteryStateUpdateCallback(batteryStateUpdateCallback_);

        expectedCallback_ = ExpectedCallback.INITIALIZE_FLIGHT_CONTROLLER;
        flightControllerInitializer_.initializeFlightController(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case INITIALIZE_FLIGHT_CONTROLLER:
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
