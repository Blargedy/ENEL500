package com.dji.sdk.sample.common.entity;

import com.dji.sdk.sample.common.mission.src.MissionBoundary;

/**
 * Created by Julia on 2017-02-04.
 */

public class InitialMissionModel
{
    private MissionBoundary missionBoundary_;
    private float altitude_;

    public InitialMissionModel()
    {
        altitude_ = 35.0f;
    }

    public MissionBoundary missionBoundary()
    {
        return missionBoundary_;
    }

    public void setMissionBoundary(MissionBoundary missionBoundary)
    {
        missionBoundary_ = missionBoundary;
    }

    public float altitude()
    {
        return altitude_;
    }
}
