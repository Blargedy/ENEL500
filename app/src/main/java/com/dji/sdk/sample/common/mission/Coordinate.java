package com.dji.sdk.sample.common.mission;

/**
 * Created by Julia on 2017-02-04.
 */

public class Coordinate {
    double latitude_;
    double longitude_;

    public Coordinate()
    {
        latitude_ = 0.0;
        longitude_ = 0.0;
    }

    public Coordinate(double latitude, double longitude)
    {
        latitude_ = latitude;
        longitude_ = longitude;
    }
}
