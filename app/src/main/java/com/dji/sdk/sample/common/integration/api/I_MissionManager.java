package com.dji.sdk.sample.common.integration.api;

import dji.sdk.missionmanager.DJIMission;

/**
 * Created by eric7 on 2017-02-21.
 */

public interface I_MissionManager
{
    void pauseMissionExecution(I_CompletionCallback callback);
    void resumeMissionExecution(I_CompletionCallback callback);
    void startMissionExecution(I_CompletionCallback callback);
    void prepareMission(
             DJIMission mission,
             DJIMission.DJIMissionProgressHandler progressHandler,
             I_CompletionCallback callback);
    void stopMissionExecution(I_CompletionCallback callback);
}
