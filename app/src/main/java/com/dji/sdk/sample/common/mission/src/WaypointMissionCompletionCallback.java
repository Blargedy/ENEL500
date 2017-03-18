package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_NextWaypointMissionStarter;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-10.
 */

public class WaypointMissionCompletionCallback implements I_CompletionCallback
{
    private static final String TAG = "HydraWaypointMissionCompletionCallback";

    private I_NextWaypointMissionStarter nextWaypointMissionStarter_;

    public WaypointMissionCompletionCallback(
            I_NextWaypointMissionStarter nextWaypointMissionStarter)
    {
        nextWaypointMissionStarter_ = nextWaypointMissionStarter;
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            nextWaypointMissionStarter_.startNextWaypointMission(null);
        }
        else
        {
            Log.e(TAG, error.getDescription());
        }
    }
}
