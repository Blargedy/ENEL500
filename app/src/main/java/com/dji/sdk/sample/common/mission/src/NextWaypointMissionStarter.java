package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.src.MissionManager;
import com.dji.sdk.sample.common.mission.api.I_NextWaypointMissionStarter;

import dji.common.error.DJIError;
import dji.sdk.missionmanager.DJIWaypointMission;

/**
 * Created by Julia on 2017-03-10.
 */

public class NextWaypointMissionStarter implements
        I_NextWaypointMissionStarter,
        I_CompletionCallback
{
    private static final String TAG = "HydraNextWaypointMissionStarter";

    private enum ExpectedCallback {PREPARE, START}
    ExpectedCallback expectedCallback_;

    private I_MissionManagerSource missionManagerSource_;
    private GeneratedMissionModel generatedMissionModel_;
    private MissionStateEntity missionState_;

    private I_CompletionCallback callback_;

    public NextWaypointMissionStarter(
            I_MissionManagerSource missionManagerSource,
            GeneratedMissionModel generatedMissionModel,
            MissionStateEntity missionState)
    {
        missionManagerSource_ = missionManagerSource;
        generatedMissionModel_ = generatedMissionModel;
        missionState_ = missionState;
    }

    @Override
    public void startNextWaypointMission(I_CompletionCallback callback)
    {
        callback_ = callback;
        if (generatedMissionModel_.waypointMissionCount() > 0)
        {
            DJIWaypointMission mission = generatedMissionModel_.getNextWaypointMission();
            expectedCallback_ = ExpectedCallback.PREPARE;
            missionManagerSource_.getMissionManager().prepareMission(mission, null, this);
        }
        else
        {
            missionState_.setCurrentMissionState(MissionStateEnum.GO_HOME);
            callback(null);
        }
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case PREPARE:
                    expectedCallback_ = ExpectedCallback.START;
                    missionManagerSource_.getMissionManager().startMissionExecution(this);
                    break;
                case START:
                    callback(null);
                    break;
                default:
                    break;
            }
        }
        else
        {
            Log.e(TAG, error.getDescription());
            callback(error);
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
