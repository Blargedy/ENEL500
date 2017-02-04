package com.dji.sdk.sample.common.mission;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionBoundary {
    private Coordinate topRight_;
    private Coordinate bottomLeft_;

    public MissionBoundary()
    {
        topRight_ = new Coordinate();
        bottomLeft_ = new Coordinate();
    }

    public MissionBoundary(Coordinate topRight, Coordinate bottomLeft)
    {
        topRight_ = topRight;
        bottomLeft_ = bottomLeft;
    }

    Coordinate topRight()
    {
        return topRight_;
    }

    Coordinate bottomLeft()
    {
        return bottomLeft_;
    }
}
