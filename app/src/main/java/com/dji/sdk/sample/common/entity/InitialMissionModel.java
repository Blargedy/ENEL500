package com.dji.sdk.sample.common.entity;

import com.dji.sdk.sample.common.mission.src.MissionBoundary;

/**
 * Created by Julia on 2017-02-04.
 */

public class InitialMissionModel
{
    private MissionBoundary missionBoundary_;
    private float altitude_ = 20.0f;
    private double minimumPercentImageOverlap_ = 0.80; // number between 0 and 1 e.g. 0.80
    private double minimumPercentSwathOverlap_ = 0.50; // number between 0 and 1 e.g. 0.50

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

    public void setAltitude(float altitude)
    {
        altitude_ = altitude;
    }

    public double minimumPercentImageOverlap()
    {
        return minimumPercentImageOverlap_;
    }

    public void setMinimumPercentImageOverlap(double minimumPercentImageOverlap)
    {
        minimumPercentImageOverlap_ = minimumPercentImageOverlap;
    }

    public double minimumPercentSwathOverlap()
    {
        return minimumPercentSwathOverlap_;
    }

    public void setMinimumPercentSwathOverlap(double minimumPercentSwathOverlap)
    {
        minimumPercentSwathOverlap_ = minimumPercentSwathOverlap;
    }
}
