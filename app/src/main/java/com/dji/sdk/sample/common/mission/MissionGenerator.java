package com.dji.sdk.sample.common.mission;

import android.util.Log;
import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleInitializationCallback;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionPreparer;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.error.DJIError;
import dji.sdk.missionmanager.DJIMission;

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
    private I_ApplicationContextManager contextManager_;

    public MissionGenerator(
            I_CustomMissionBuilder customMissionBuilder,
            I_MissionPreparer missionPreparer,
            I_ImageTransferModuleInitializer imageTransferModuleInitializer,
            I_ApplicationContextManager contextManager)
    {
        customMissionBuilder_ = customMissionBuilder;
        missionPreparer_ = missionPreparer;
        imageTransferModuleInitializer_ = imageTransferModuleInitializer;
        contextManager_ = contextManager;
    }

    public void generateMission()
    {
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
        Toast.makeText(contextManager_.getApplicationContext(), "Success: generated mission" , Toast.LENGTH_SHORT).show();
    }
}
