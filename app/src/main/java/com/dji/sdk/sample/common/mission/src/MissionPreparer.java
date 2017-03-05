package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionPreparer;

import dji.common.error.DJIError;
import dji.sdk.missionmanager.DJIMission;

/**
 * Created by Julia on 2017-03-05.
 */

public class MissionPreparer implements
        I_MissionPreparer,
        I_CompletionCallback
{
    private static final String TAG = "MissionPreparer";

    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerSource flightControllerSource_;
    private GeneratedMissionModel generatedMissionModel_;

    private I_CompletionCallback callback_;

    public MissionPreparer(
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerSource flightControllerSource,
            GeneratedMissionModel generatedMissionModel)
    {
        missionManagerSource_ = missionManagerSource;
        flightControllerSource_ = flightControllerSource;
        generatedMissionModel_ = generatedMissionModel;
    }

    @Override
    public void prepareMission(I_CompletionCallback callback) {
        callback_ = callback;
        flightControllerSource_.getFlightController().
                setHomeLocationUsingAircraftCurrentLocation(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            DJIMission mission = generatedMissionModel_.djiMission_;
            missionManagerSource_.getMissionManager().prepareMission(mission, null, callback_);
        }
        else
        {
            Log.e(TAG, "Unable to set set home location : " + error.getDescription());
        }
    }
}