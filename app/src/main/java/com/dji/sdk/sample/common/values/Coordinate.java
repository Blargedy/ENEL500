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

    //using the haversine method https://en.wikipedia.org/wiki/Haversine_formula
    public double distanceApproximationInMeters(Coordinate coordinate)
    {
        double deltaLatitude = MathUtilities.degreesToRadians(coordinate.latitude_ - latitude_);
        double deltaLongitude = MathUtilities.degreesToRadians(coordinate.longitude_ - longitude_);
        double radiusOfEarthInMeters = 6371008;

        double latitude1 = MathUtilities.degreesToRadians(latitude_);
        double latitude2 = MathUtilities.degreesToRadians(coordinate.latitude_);

        double a = Math.sin(deltaLatitude/2) * Math.sin(deltaLatitude/2) +
                Math.sin(deltaLongitude/2) * Math.sin(deltaLongitude/2) * Math.cos(latitude1) * Math.cos(latitude2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return radiusOfEarthInMeters * c;
    }
}
