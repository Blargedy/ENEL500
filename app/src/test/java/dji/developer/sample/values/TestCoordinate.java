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
        Coordinate c1 = new Coordinate(51.077795, -114.136494);
        Coordinate c2 = new Coordinate(51.076225, -114.135038);
        double expectedDistance = 200;
        double tolerance = 1.0;

        double actualDistance = c1.distanceApproximationInMeters(c2);

        //assertEquals(expectedDistance, actualDistance, tolerance);
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
