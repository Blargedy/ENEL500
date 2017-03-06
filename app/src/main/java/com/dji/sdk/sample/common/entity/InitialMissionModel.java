package com.dji.sdk.sample.common.entity;

import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.values.Coordinate;

/**
 * Created by Julia on 2017-02-04.
 */

public class InitialMissionModel
{
    private MissionBoundary missionBoundary_;
    private float altitude_;

    public InitialMissionModel()
    {
        //temporary
        Coordinate topRight = new Coordinate(50.796276, -114.205159);
        Coordinate bottomLeft = new Coordinate(50.795906, -114.206540);

        missionBoundary_ = new MissionBoundary(topRight, bottomLeft);
        altitude_ = 15.0f;
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
