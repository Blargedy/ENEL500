package com.dji.sdk.sample.common.mission.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.entity.MissionStateEnum;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-07.
 */

public class MissionExecutor implements
        I_MissionGenerationCompletionCallback,
        I_CompletionCallback
{
    private static final String TAG = "MissionExecutor";

    private BroadcastReceiver receiver_;

    private I_MissionGenerator missionGenerator_;
    private I_MissionManagerSource missionManagerSource_;
    private MissionStateEntity missionState_;

    public MissionExecutor(
            Context context,
            I_MissionGenerator missionGenerator,
            I_MissionManagerSource missionManagerSource,
            MissionStateEntity missionState)
    {
        missionGenerator_ = missionGenerator;
        missionManagerSource_ = missionManagerSource;
        missionState_ = missionState;

        registerMissionStateChangedReceiver(context);
    }

    private void registerMissionStateChangedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.MISSION_STATE_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                missionStateChanged();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver_, filter);
    }

    private void missionStateChanged()
    {
        switch (missionState_.getCurrentMissionState())
        {
            case GENERATE_MISSION:
                missionGenerator_.generateMission(this);
                break;

            case START_MISSION:
                missionManagerSource_.getMissionManager().startMissionExecution(this);
                break;

            case HOVER_NOW:
                missionManagerSource_.getMissionManager().pauseMissionExecution(this);
                break;

            case RESUME_MISSION:
                missionManagerSource_.getMissionManager().resumeMissionExecution(this);
                break;

            default:
                break;
        }
    }

    @Override
    public void onMissionGenerationCompletion()
    {
        changeMissionStateAfterCommandIsExecuted();
    }

    @Override
    public void onResult(DJIError error)
    {
        if(error == null)
        {
            changeMissionStateAfterCommandIsExecuted();
        }
        else
        {
            Log.e(TAG, error.getDescription());
        }
    }

    private void changeMissionStateAfterCommandIsExecuted()
    {
        switch (missionState_.getCurrentMissionState())
        {
            case GENERATE_MISSION:
                missionState_.setCurrentMissionState(MissionStateEnum.VIEW_MISSION);
                break;

            case START_MISSION:
                missionState_.setCurrentMissionState(MissionStateEnum.MISSION_EXECUTING);
                break;

            case HOVER_NOW:
                missionState_.setCurrentMissionState(MissionStateEnum.HOVERING);
                break;

            case RESUME_MISSION:
                missionState_.setCurrentMissionState(MissionStateEnum.MISSION_EXECUTING);
                break;

            default:
                break;
        }
    }
}