package com.dji.sdk.sample.common.entity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;

/**
 * Created by Julia on 2017-03-08.
 */

public class MissionStateEntity
{
    private static final String TAG = "MissionStateEntity";

    private MissionStateEnum currentMissionState_;
    private MissionStateEnum previousMissionState_;

    private Context context_;

    public MissionStateEntity(Context context)
    {
        context_ = context;

        currentMissionState_ = MissionStateEnum.INITIALIZING_MAP;
        previousMissionState_ = null;
    }

    public MissionStateEnum getCurrentMissionState()
    {
        return currentMissionState_;
    }

    public void setCurrentMissionState(MissionStateEnum state)
    {
        previousMissionState_ = currentMissionState_;
        currentMissionState_ = state;

        Intent intent = new Intent(BroadcastIntentNames.MISSION_STATE_CHANGED);
        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);

        Log.d(TAG, "Mission state changed from "
                + previousMissionState_.name() + " to " + currentMissionState_.name());
    }
}
