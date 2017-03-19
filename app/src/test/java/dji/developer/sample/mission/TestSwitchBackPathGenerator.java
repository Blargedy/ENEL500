package dji.developer.sample.mission;

import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.values.MissionBoundary;
import com.dji.sdk.sample.common.mission.src.SwitchBackPathGenerator;
import com.dji.sdk.sample.common.values.Coordinate;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by Bill on 01-Mar-2017.
 */

public class TestSwitchBackPathGenerator
{
    private InitialMissionModel initialMissionModel_ = mock(InitialMissionModel.class);

    private SwitchBackPathGenerator patient_ = new SwitchBackPathGenerator(
            initialMissionModel_);

    @Test
    public void willAddLinearlySpacedIntermediateCoordinates() {
        Coordinate bottomLeft = new Coordinate(0.0, 0.0);
        Coordinate topRight = new Coordinate(10.0, 100.0);
        double tolerance = 1e-8;

        Vector<Coordinate> coordinateList = new Vector<>();
        coordinateList.add(bottomLeft);
        coordinateList.add(topRight);

        patient_.insertLinearlyDistributedCoordinates(coordinateList, 0, 3);

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
    public void calculatesMinimumSwathSpacing() {
        Coordinate bottomLeft = new Coordinate(51.080873, -114.130272); // SW Corner of EEEL Building
        Coordinate topRight = new Coordinate(51.081239, -114.128617); // NE Corner of EEEL Building
        float altitude = 35;
        double minimumPercentSwathOverlap = 0.50; // 50% overlap
        MissionBoundary missionBoundary = new MissionBoundary(topRight, bottomLeft);
        when(initialMissionModel_.missionBoundary()).thenReturn(missionBoundary);
        when(initialMissionModel_.altitude()).thenReturn(altitude);
        when(initialMissionModel_.minimumPercentSwathOverlap()).thenReturn(minimumPercentSwathOverlap);

        double maximumSwathSpacingInMeters = patient_.calculateSwathSpacing(
                initialMissionModel_.minimumPercentSwathOverlap(),
                altitude);

        double expectedSwathSpacing = 28.0;
        double tolerance = 1e-8;
        assertEquals(expectedSwathSpacing, maximumSwathSpacingInMeters, tolerance);
    }

    @Test
    public void calculatesMinimumImageSpacing() {
        Coordinate bottomLeft = new Coordinate(51.080873, -114.130272); // SW Corner of EEEL Building
        Coordinate topRight = new Coordinate(51.081239, -114.128617); // NE Corner of EEEL Building
        float altitude = 35;
        double minimumPercentImageOverlap = 0.80; // 80% overlap
        MissionBoundary missionBoundary = new MissionBoundary(topRight, bottomLeft);
        when(initialMissionModel_.missionBoundary()).thenReturn(missionBoundary);
        when(initialMissionModel_.altitude()).thenReturn(altitude);
        when(initialMissionModel_.minimumPercentImageOverlap()).thenReturn(minimumPercentImageOverlap);

        double maximumImageSpacingInMeters = patient_.calculateImageSpacing(
                initialMissionModel_.minimumPercentImageOverlap(),
                altitude);

        double expectedImageSpacing = 8.4;
        double tolerance = 1e-8;
        assertEquals(expectedImageSpacing, maximumImageSpacingInMeters, tolerance);
    }

    @Test
    public void createsHorizontalSwaths() {
        Coordinate bottomLeft = new Coordinate(51.080873, -114.130272); // SW Corner of EEEL Building
        Coordinate topRight = new Coordinate(51.081239, -114.128617); // NE Corner of EEEL Building
        float altitude = 35;
        double minimumPercentSwathOverlap = 0.50; // 50% overlap
        double minimumPercentImageOverlap = 0.80; // 80% overlap
        MissionBoundary missionBoundary = new MissionBoundary(topRight, bottomLeft);
        when(initialMissionModel_.missionBoundary()).thenReturn(missionBoundary);
        when(initialMissionModel_.altitude()).thenReturn(altitude);
        when(initialMissionModel_.minimumPercentSwathOverlap()).thenReturn(minimumPercentSwathOverlap);
        when(initialMissionModel_.minimumPercentImageOverlap()).thenReturn(minimumPercentImageOverlap);

        List<Coordinate> coordinateVector = patient_.generateSwitchback();

        //assertEquals(35.0,patient_.initialMissionModel_.altitude(),1e-8);
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
        double minimumPercentSwathOverlap = 0.50; // 50% overlap
        double minimumPercentImageOverlap = 0.80; // 80% overlap
        MissionBoundary missionBoundary = new MissionBoundary(topRight, bottomLeft);
        when(initialMissionModel_.missionBoundary()).thenReturn(missionBoundary);
        when(initialMissionModel_.altitude()).thenReturn(altitude);
        when(initialMissionModel_.minimumPercentSwathOverlap()).thenReturn(minimumPercentSwathOverlap);
        when(initialMissionModel_.minimumPercentImageOverlap()).thenReturn(minimumPercentImageOverlap);

        List<Coordinate> coordinateVector = patient_.generateSwitchback();

        int expectedNumberOfCoordinates = 12;
        assertEquals(expectedNumberOfCoordinates, coordinateVector.size());
        double tolerance = 0.000010;  // roughly 1 meter
        assertEquals(coordinateVector.get(2).latitude_, 51.081019, tolerance);
        assertEquals(coordinateVector.get(2).longitude_, -114.130272, tolerance);
    }
}
