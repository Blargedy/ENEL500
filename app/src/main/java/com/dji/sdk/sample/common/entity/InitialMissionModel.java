package com.dji.sdk.sample.common.entity;

import com.dji.sdk.sample.common.mission.MissionBoundary;

/**
 * Created by Julia on 2017-02-04.
 */

public class InitialMissionModel
{
    private MissionBoundary missionBoundary_;
    private float altitude_;

    public InitialMissionModel()
    {
        missionBoundary_ = new MissionBoundary();
        altitude_ = 5.0f;
    }

    public MissionBoundary missionBoundary()
    {
        return missionBoundary_;
    }

    public float altitude()
    {
        return altitude_;
    }
}
