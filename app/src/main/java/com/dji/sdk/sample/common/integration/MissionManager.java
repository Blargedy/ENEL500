package com.dji.sdk.sample.common.integration;


import dji.sdk.missionmanager.DJIMissionManager;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionManager implements I_MissionManager{
    DJIMissionManager missionManager_;

    MissionManager(DJIMissionManager missionManager)
    {
        missionManager_ = missionManager;
    }

    @Override
    public void pauseMissionExecution(I_CompletionCallback callback)
    {
        missionManager_.pauseMissionExecution(callback);
    }

    @Override
    public void resumeMissionExecution(I_CompletionCallback callback)
    {
        missionManager_.resumeMissionExecution(callback);
    }
}
