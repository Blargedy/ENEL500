package dji.developer.sample.mission;

import com.dji.sdk.sample.common.mission.src.SwitchBackPathGenerator;
import com.dji.sdk.sample.common.values.Coordinate;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import java.util.Vector;

/**
 * Created by Bill on 01-Mar-2017.
 */

public class TestSwitchBackPathGenerator {
    @Test
    public void willAddLinearlySpacedIntermediateCoordinates() {
        Coordinate bottomLeft = new Coordinate(0.0, 0.0);
        Coordinate topRight = new Coordinate(10.0, 100.0);
        double tolerance = 1e-8;

        Vector<Coordinate> coordinateList = new Vector<>();
        coordinateList.add(bottomLeft);
        coordinateList.add(topRight);

        SwitchBackPathGenerator.insertLinearlyDistributedCoordinates(coordinateList, 0, 3);

        assertEquals(0.0, coordinateList.get(0).latitude_, tolerance);
        assertEquals(0.0, coordinateList.get(0).longitude_, tolerance);
        assertEquals(2.5, coordinateList.get(1).latitude_, tolerance);
        assertEquals(25.0, coordinateList.get(1).longitude_, tolerance);
        assertEquals(5.0, coordinateList.get(2).latitude_, tolerance);
        assertEquals(50.0, coordinateList.get(2).longitude_, tolerance);
        assertEquals(7.5, coordinateList.get(3).latitude_, tolerance);
        assertEquals(75.0, coordinateList.get(3).longitude_, tolerance);
        assertEquals(10.0, coordinateList.get(4).latitude_, tolerance);
        assertEquals(100.0, coordinateList.get(4).longitude_, tolerance);
    }
}
