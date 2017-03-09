package dji.developer.sample.values;

import com.dji.sdk.sample.common.values.Coordinate;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Bill on 15-Feb-2017.
 */

public class TestCoordinate
{
    @Test
    public void willCorrectlyCalculateDistanceBetweenTwoCoordinates()
    {
        Coordinate c1 = new Coordinate(51.080873, -114.130272);
        Coordinate c2 = new Coordinate(51.081239, -114.128617);
        double expectedDistance = 122.48;
        double tolerance = 0.1;

        double actualDistance = c1.distanceApproximationInMeters(c2);

        assertEquals(expectedDistance, actualDistance, tolerance);
    }

    @Test
    public void willDetermineCoincidingCoordinatesAreZeroMetersApart()
    {
        Coordinate c1 = new Coordinate(181.0,181.0);
        Coordinate c2 = new Coordinate(181.0,181.0);
        double expectedDistance = 0.0;
        double tolerance = 1e-8;

        double actualDistance = c1.distanceApproximationInMeters(c2);

        assertEquals(expectedDistance, actualDistance, tolerance);
    }
}
