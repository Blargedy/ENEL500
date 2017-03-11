package com.dji.sdk.sample.common.mission.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-03-10.
 */

public interface I_NextWaypointMissionStarter
{
    void startNextWaypointMission(I_CompletionCallback callback);
}
