package com.dji.sdk.sample.common.entity;

import com.dji.sdk.sample.common.mission.MissionBoundary;
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
        Coordinate topRight = new Coordinate(51.077176, -114.131086);
        Coordinate bottomLeft = new Coordinate(51.074446, -114.138085);

        missionBoundary_ = new MissionBoundary(topRight, bottomLeft);
        altitude_ = 15.0f;
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
