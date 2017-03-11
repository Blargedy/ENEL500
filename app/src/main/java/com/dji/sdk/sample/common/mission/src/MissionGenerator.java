package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleInitializationCallback;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_CustomMissionBuilder;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-01-15.
 */

public class MissionGenerator implements
        I_MissionGenerator,
        I_CompletionCallback,
        I_ImageTransferModuleInitializationCallback
{
    private static final String TAG = "MissionGenerator";

    private I_CustomMissionBuilder customMissionBuilder_;
    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerSource flightControllerSource_;
    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;
    private I_CompletionCallback missionExecutionCompletionCallback_;
    private I_WaypointMissionProgressStatusCallback missionProgressStatusCallback_;

    private I_MissionGenerationCompletionCallback callback_;

    public MissionGenerator(
            I_CustomMissionBuilder customMissionBuilder,
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerSource flightControllerSource,
            I_ImageTransferModuleInitializer imageTransferModuleInitializer,
            I_CompletionCallback missionExecutionCompletionCallback,
            I_WaypointMissionProgressStatusCallback missionProgressStatusCallback)
    {
        customMissionBuilder_ = customMissionBuilder;
        missionManagerSource_ = missionManagerSource;
        flightControllerSource_ = flightControllerSource;
        imageTransferModuleInitializer_ = imageTransferModuleInitializer;
        missionExecutionCompletionCallback_ = missionExecutionCompletionCallback;
        missionProgressStatusCallback_ = missionProgressStatusCallback;
    }

    public void generateMission(I_MissionGenerationCompletionCallback callback)
    {
        callback_ = callback;
        customMissionBuilder_.buildCustomMission();

        I_MissionManager missionManager = missionManagerSource_.getMissionManager();
        missionManager.setMissionExecutionFinishedCallback(missionExecutionCompletionCallback_);
        missionManager.setMissionProgressStatusCallback(missionProgressStatusCallback_);

        flightControllerSource_.getFlightController().
                setHomeLocationUsingAircraftCurrentLocation(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            imageTransferModuleInitializer_.initializeImageTransferModulePriorToFlight(this);
        }
        else
        {
            Log.e(TAG, "Unable to set home location : " + error.getDescription());
        }
    }

    @Override
    public void onImageTransferModuleInitializationCompletion()
    {
        callback_.onMissionGenerationCompletion();
    }
}
