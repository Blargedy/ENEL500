package com.dji.sdk.sample.common.values;

import com.dji.sdk.sample.common.utility.MathUtilities;

/**
 * Created by Julia on 2017-02-04.
 */

public class Coordinate {
    public double latitude_;
    public double longitude_;

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

    public double distanceApproximationInMeters(Coordinate coordinate)
    {
        double deltaLatitude = coordinate.latitude_ - latitude_;
        double deltaLongitude = coordinate.longitude_ - longitude_;
        double deltaDegrees = Math.sqrt(deltaLatitude*deltaLatitude + deltaLongitude*deltaLongitude);
        double deltaRadians = MathUtilities.degreesToRadians(deltaDegrees);
        double radiusOfEarthInMeters = 6371008;
        return deltaRadians*radiusOfEarthInMeters;
    }
}
