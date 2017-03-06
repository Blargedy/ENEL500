package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleInitializationCallback;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_CustomMissionBuilder;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.mission.api.I_MissionPreparer;

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
    private I_MissionPreparer missionPreparer_;
    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;

    private I_MissionGenerationCompletionCallback callback_;

    public MissionGenerator(
            I_CustomMissionBuilder customMissionBuilder,
            I_MissionPreparer missionPreparer,
            I_ImageTransferModuleInitializer imageTransferModuleInitializer)
    {
        customMissionBuilder_ = customMissionBuilder;
        missionPreparer_ = missionPreparer;
        imageTransferModuleInitializer_ = imageTransferModuleInitializer;
    }

    public void generateMission(I_MissionGenerationCompletionCallback callback)
    {
        callback_ = callback;
        customMissionBuilder_.buildCustomMission();
        missionPreparer_.prepareMission(this);
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
            Log.e(TAG, "Unable to prepare mission : " + error.getDescription());
        }
    }

    @Override
    public void onImageTransferModuleInitializationCompletion()
    {
        callback_.onMissionGenerationCompletion();
    }
}
