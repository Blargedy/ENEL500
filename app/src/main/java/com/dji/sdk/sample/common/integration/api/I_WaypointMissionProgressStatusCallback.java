package com.dji.sdk.sample.common.integration.api;

import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIMissionManager;

/**
 * Created by Julia on 2017-03-11.
 */

public interface I_WaypointMissionProgressStatusCallback
        extends DJIMissionManager.MissionProgressStatusCallback
{
    void missionProgressStatus(DJIMission.DJIMissionProgressStatus progressStatus);

    void waypointReached();
    void resetWaypointCount();
}
