package com.dji.sdk.sample.common.entity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;

/**
 * Created by Julia on 2017-03-08.
 */

public class MissionStateEntity
{
    private MissionStateEnum currentMissionState_;
    private MissionStateEnum previousMissionState_;

    private Context context_;

    public MissionStateEntity(Context context)
    {
        context_ = context;

        currentMissionState_ = MissionStateEnum.SELECT_AREA;
        previousMissionState_ = null;
    }

    public MissionStateEnum getCurrentMissionState()
    {
        return currentMissionState_;
    }

    public MissionStateEnum getPreviousMissionState()
    {
        return previousMissionState_;
    }

    public void setCurrentMissionState(MissionStateEnum state)
    {
        previousMissionState_ = currentMissionState_;
        currentMissionState_ = state;

        Intent intent = new Intent(BroadcastIntentNames.MISSION_STATE_CHANGED);
        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
    }
}
