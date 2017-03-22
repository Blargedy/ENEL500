package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_DroneLocationUpdater;
import com.dji.sdk.sample.common.mission.api.I_WaypointReachedNotifier;
import com.dji.sdk.sample.common.utility.I_MissionErrorNotifier;

import dji.common.error.DJIError;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIWaypointMission;

import static dji.sdk.missionmanager.DJIWaypointMission.DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT;

/**
 * Created by Julia on 2017-03-11.
 */

public class WaypointMissionProgressStatusCallback implements
        I_WaypointMissionProgressStatusCallback,
        I_CompletionCallback
{
    private static final String TAG = "HydraWaypointMissionProgressStatusCallback";

    private int waypointIndex_;

    private I_MissionErrorNotifier missionErrorNotifier_;
    private I_WaypointReachedNotifier waypointReachedNotifier_;
    private I_DroneLocationUpdater droneLocationUpdater_;
    private I_CameraSource cameraSource_;

    public WaypointMissionProgressStatusCallback(
            I_MissionErrorNotifier missionErrorNotifier,
            I_WaypointReachedNotifier waypointReachedNotifier,
            I_DroneLocationUpdater droneLocationUpdater,
            I_CameraSource cameraSource)
    {
        missionErrorNotifier_ = missionErrorNotifier;
        waypointReachedNotifier_ = waypointReachedNotifier;
        droneLocationUpdater_ = droneLocationUpdater;
        cameraSource_ = cameraSource;

        waypointIndex_ = 0;
    }

    @Override
    public void missionProgressStatus(DJIMission.DJIMissionProgressStatus progressStatus)
    {
        if (progressStatus instanceof DJIWaypointMission.DJIWaypointMissionStatus)
        {
            DJIWaypointMission.DJIWaypointMissionStatus waypointMissionStatus =
                    (DJIWaypointMission.DJIWaypointMissionStatus) progressStatus;

            int targetWaypointIndex = waypointMissionStatus.getTargetWaypointIndex();
            boolean isWaypointReached = waypointMissionStatus.isWaypointReached();

            if (isDroneAtNewWaypoint(targetWaypointIndex, isWaypointReached))
            {
                waypointReachedNotifier_.notifyWaypointAtIndexHasBeenReached(waypointIndex_);
                cameraSource_.getCamera().shootSinglePhoto(this);
                waypointIndex_++;
            }

            droneLocationUpdater_.updateDroneLocation();
        }
    }

    private boolean isDroneAtNewWaypoint(int missionUpdateTargetWaypoint, boolean isWaypointReached)
    {
        int nextTargetWaypointIndex = (waypointIndex_ + 1) % DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT;
        return (nextTargetWaypointIndex == missionUpdateTargetWaypoint) && isWaypointReached;
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error != null)
        {
            String errorMessage = "Failed to take image on waypoint " + waypointIndex_ +
                    " : " + error.getDescription();
            missionErrorNotifier_.notifyErrorOccurred(errorMessage);
            Log.e(TAG, errorMessage);
        }
    }
}
