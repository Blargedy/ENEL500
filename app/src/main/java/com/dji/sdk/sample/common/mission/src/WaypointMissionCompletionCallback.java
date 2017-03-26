package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_NextWaypointMissionStarter;
import com.dji.sdk.sample.common.mission.api.I_WaypointReachedNotifier;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;

import dji.common.error.DJIError;
import dji.sdk.missionmanager.DJIWaypointMission;

/**
 * Created by Julia on 2017-03-10.
 */

public class WaypointMissionCompletionCallback implements I_CompletionCallback
{
    private static final String TAG = "HydraWaypointMissionCompletionCallback";

    private I_MissionStatusNotifier missionStatusNotifier_;
    private I_WaypointReachedNotifier waypointReachedNotifier_;
    private I_NextWaypointMissionStarter nextWaypointMissionStarter_;
    private I_WaypointMissionProgressStatusCallback missionProgressStatusCallback_;

    private DJIWaypointMission.DJIWaypointMissionStatus missionCompletionStatusUpdate_;

    public WaypointMissionCompletionCallback(
            I_MissionStatusNotifier missionStatusNotifier,
            I_WaypointReachedNotifier waypointReachedNotifier,
            I_NextWaypointMissionStarter nextWaypointMissionStarter,
            I_WaypointMissionProgressStatusCallback missionProgressStatusCallback)
    {
        missionStatusNotifier_ = missionStatusNotifier;
        waypointReachedNotifier_ = waypointReachedNotifier;
        nextWaypointMissionStarter_ = nextWaypointMissionStarter;
        missionProgressStatusCallback_ = missionProgressStatusCallback;
        missionCompletionStatusUpdate_ = new DJIWaypointMission.DJIWaypointMissionStatus(
                0, true, DJIWaypointMission.DJIWaypointMissionExecutionState.BeginAction, null);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            missionProgressStatusCallback_.missionProgressStatus(missionCompletionStatusUpdate_);
            nextWaypointMissionStarter_.startNextWaypointMission(null);
        }
        else
        {
            missionStatusNotifier_.notifyStatusChanged(error.getDescription());
            Log.e(TAG, error.getDescription());
        }
    }
}
