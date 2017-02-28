package com.dji.sdk.sample.common.mission;

/**
 * Created by Matthew on 2017-02-08.
 */

public interface I_MissionController {
    void startMission();
    void resumeMission();
    void handleWaypointReached();
}
