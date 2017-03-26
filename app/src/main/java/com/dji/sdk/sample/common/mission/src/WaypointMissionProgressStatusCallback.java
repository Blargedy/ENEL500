package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_WaypointImageShooter;
import com.dji.sdk.sample.common.mission.api.I_WaypointReachedNotifier;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;

import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIWaypointMission;

import static com.dji.sdk.sample.common.utility.MissionParameters.WAYPOINTS_PER_MISSION;

/**
 * Created by Julia on 2017-03-11.
 */

public class WaypointMissionProgressStatusCallback implements
        I_WaypointMissionProgressStatusCallback
{
    private int waypointIndex_;

    private I_MissionStatusNotifier missionStatusNotifier_;
    private I_WaypointReachedNotifier waypointReachedNotifier_;
    private I_WaypointImageShooter imageShooter_;

    public WaypointMissionProgressStatusCallback(
            I_MissionStatusNotifier missionStatusNotifier,
            I_WaypointReachedNotifier waypointReachedNotifier,
            I_WaypointImageShooter imageShooter)
    {
        missionStatusNotifier_ = missionStatusNotifier;
        waypointReachedNotifier_ = waypointReachedNotifier;
        imageShooter_ = imageShooter;

        waypointIndex_ = 0;
    }

    @Override
    public void missionProgressStatus(DJIMission.DJIMissionProgressStatus progressStatus)
    {
        if (progressStatus.getError() != null)
        {
            missionStatusNotifier_.notifyStatusChanged(progressStatus.getError().getDescription());
        }

        if (progressStatus instanceof DJIWaypointMission.DJIWaypointMissionStatus)
        {
            DJIWaypointMission.DJIWaypointMissionStatus waypointMissionStatus =
                    (DJIWaypointMission.DJIWaypointMissionStatus) progressStatus;

            if (isDroneAtNewWaypoint(waypointMissionStatus))
            {
                waypointReachedNotifier_.notifyWaypointAtIndexHasBeenReached(waypointIndex_);
                imageShooter_.shootPhotoOnWaypoint(waypointIndex_);

                waypointIndex_++;
                missionStatusNotifier_.notifyStatusChanged("At waypoint " + waypointIndex_);
            }
        }
    }

    @Override
    public void resetWaypointCount()
    {
        waypointIndex_ = 0;
    }

    private boolean isDroneAtNewWaypoint(DJIWaypointMission.DJIWaypointMissionStatus status)
    {
        int nextTargetWaypointIndex = (waypointIndex_ + 1) % WAYPOINTS_PER_MISSION;
        return (nextTargetWaypointIndex == status.getTargetWaypointIndex())
                && status.isWaypointReached();
    }
}
