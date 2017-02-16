package com.dji.sdk.sample.common.utility;

/**
 * Created by Bill on 15-Feb-2017.
 */

public class MathUtilities
{
    static public double degreesToRadians(double degrees)
    {
        return degrees*Math.PI/180.0;
    }

    static public double radiansToDegrees(double radians)
    {
        return radians*180.0/Math.PI;
    }
}
