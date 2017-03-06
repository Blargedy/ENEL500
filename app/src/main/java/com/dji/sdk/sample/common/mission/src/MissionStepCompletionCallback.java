package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_WaypointReachedHandler;

import dji.common.error.DJIError;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionStepCompletionCallback implements I_CompletionCallback
{
    private static final String TAG = "MissionStepCompletionCallback";

    private I_WaypointReachedHandler waypointReachedHandler_;
    private int waypointCounter_;

    public MissionStepCompletionCallback(
            I_WaypointReachedHandler waypointReachedHandler)
    {
        waypointReachedHandler_ = waypointReachedHandler;
        waypointCounter_ = 0;
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            waypointReachedHandler_.handleWaypointReached(waypointCounter_++);
        }
        else {
            Log.e(TAG, "Unable to reach waypoint " + waypointCounter_ + " : " + error.getDescription());
        }
    }
}

