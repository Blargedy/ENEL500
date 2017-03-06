package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.values.Coordinate;

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

    public Coordinate topRight()
    {
        return topRight_;
    }

    public Coordinate bottomLeft()
    {
        return bottomLeft_;
    }

    public void setTopRight(Coordinate topRight) { topRight_ = topRight; }

    public void setBottomLeft(Coordinate bottomLeft) {topRight_ = bottomLeft;}
}
