package dji.developer.sample.mission;

import com.dji.sdk.sample.common.mission.src.SwitchBackPathGenerator;
import com.dji.sdk.sample.common.values.Coordinate;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import java.util.List;
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

    @Test
    public void createsHorizontalSwaths() {
        Coordinate bottomLeft = new Coordinate(51.080873, -114.130272); // SW Corner of EEEL Building
        Coordinate topRight = new Coordinate(51.081239, -114.128617); // NE Corner of EEEL Building
        float altitude = 35;

        SwitchBackPathGenerator switchBackPathGenerator = new SwitchBackPathGenerator(bottomLeft, topRight, altitude);
        List<Coordinate> coordinateVector = switchBackPathGenerator.generateSwitchback();

        int expectedNumberOfCoordinates = 45;
        assertEquals(expectedNumberOfCoordinates, coordinateVector.size());
        double tolerance = 0.000010; // roughly 1 meter
        assertEquals(coordinateVector.get(3).latitude_, 51.080873, tolerance);
        assertEquals(coordinateVector.get(3).longitude_, -114.129917, tolerance);
        assertEquals(coordinateVector.get(26).latitude_, 51.081056, tolerance);
        assertEquals(coordinateVector.get(26).longitude_, -114.129917, tolerance);
    }

    @Test
    public void createsVerticalSwaths() {
        Coordinate bottomLeft = new Coordinate(51.080873, -114.130272); // SW Corner of EEEL Building
        Coordinate topRight = new Coordinate(51.081239, -114.129917); // On North Edge of EEEL Building
        float altitude = 35;

        SwitchBackPathGenerator switchBackPathGenerator = new SwitchBackPathGenerator(bottomLeft, topRight, altitude);
        List<Coordinate> coordinateVector = switchBackPathGenerator.generateSwitchback();

        int expectedNumberOfCoordinates = 12;
        assertEquals(expectedNumberOfCoordinates, coordinateVector.size());
        double tolerance = 0.000010;  // roughly 1 meter
        assertEquals(coordinateVector.get(2).latitude_, 51.081019, tolerance);
        assertEquals(coordinateVector.get(2).longitude_, -114.130272, tolerance);
    }
}
