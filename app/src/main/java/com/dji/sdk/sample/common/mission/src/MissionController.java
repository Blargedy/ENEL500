package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionController;

/**
 * Created by Matthew on 2017-02-08.
 */

public class MissionController implements
        I_MissionController
{
    private I_MissionManagerSource missionManagerSource_;

    public MissionController(
            I_MissionManagerSource missionManagerSource)
    {
        missionManagerSource_ = missionManagerSource;
    }

    @Override
    public void startMission(I_CompletionCallback callback)
    {
        missionManagerSource_.getMissionManager().startMissionExecution(callback);
    }

    @Override
    public void pauseMission(I_CompletionCallback callback)
    {
        missionManagerSource_.getMissionManager().pauseMissionExecution(callback);
    }

    @Override
    public void resumeMission(I_CompletionCallback callback)
    {
        missionManagerSource_.getMissionManager().resumeMissionExecution(callback);
    }

    @Override
    public void stopMission(I_CompletionCallback callback)
    {
        missionManagerSource_.getMissionManager().stopMissionExecution(callback);
    }
}
