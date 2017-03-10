package com.dji.sdk.sample.common.mission.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Matthew on 2017-02-08.
 */

public interface I_MissionController
{
    void startMission(I_CompletionCallback callback);
    void pauseMission(I_CompletionCallback callback);
    void resumeMission(I_CompletionCallback callback);
    void stopMission(I_CompletionCallback callback);
}
