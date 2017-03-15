package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_DroneLocationUpdater;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.api.I_WaypointReachedNotifier;

import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIWaypointMission;

import static dji.sdk.missionmanager.DJIWaypointMission.DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT;

/**
 * Created by Julia on 2017-03-11.
 */

public class WaypointMissionProgressStatusCallback implements I_WaypointMissionProgressStatusCallback
{
    private static final String TAG = "WaypointMissionProgressStatusCallback";

    private static final int IMAGE_TRANSFER_DELAY = 5;
    private int waypointIndex_;

    private I_MissionPeriodicImageTransferInitiator imageTransferInitiator_;
    private I_WaypointReachedNotifier waypointReachedNotifier_;
    private I_DroneLocationUpdater droneLocationUpdater_;

    public WaypointMissionProgressStatusCallback(
            I_MissionPeriodicImageTransferInitiator imageTransferInitiator,
            I_WaypointReachedNotifier waypointReachedNotifier,
            I_DroneLocationUpdater droneLocationUpdater)
    {
        imageTransferInitiator_ = imageTransferInitiator;
        waypointReachedNotifier_ = waypointReachedNotifier;
        droneLocationUpdater_ = droneLocationUpdater;
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
                initiateImageTransferIfNecessary();
                waypointReachedNotifier_.notifyWaypointAtIndexHasBeenReached(waypointIndex_);
                waypointIndex_++;
            }

            droneLocationUpdater_.updateDroneLocation();
        }
    }

    private void initiateImageTransferIfNecessary()
    {
        if (waypointIndex_ != 0 && waypointIndex_ % IMAGE_TRANSFER_DELAY == 0)
        {
            imageTransferInitiator_.initiateImageTransfer();
        }
    }

    private boolean isDroneAtNewWaypoint(int missionUpdateTargetWaypoint, boolean isWaypointReached)
    {
        int nextTargetWaypointIndex = (waypointIndex_ + 1) % DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT;

        return (nextTargetWaypointIndex == missionUpdateTargetWaypoint)
                && isWaypointReached;
    }
}
