package dji.developer.sample.mission;

import com.dji.sdk.sample.common.mission.MissionGenerator;
import org.junit.Test;
import java.util.Random;
import dji.sdk.missionmanager.DJIWaypointMission;

import static org.junit.Assert.assertEquals;

/**
 * Created by Julia on 2017-01-16.
 */

public class TestMissionGenerator
{
    private Random randomGenerator = new Random();
//    private MissionGenerator missionGenerator = new MissionGenerator();

    @Test
    public void willSetTheFinishedActionForTheMission() throws Exception {
//        This test is crashing due to some internal byte exception in DJI code.
//        TODO Need to fix the way the DJI SDK is compiled into unit tests

//        DJIWaypointMission mission = missionGenerator.generateMissionWithOneWaypoint(
//                randomGenerator.nextDouble(),
//                randomGenerator.nextDouble());
//
//        assertEquals(
//                "Finished action not equal",
//                DJIWaypointMission.DJIWaypointMissionFinishedAction.GoHome,
//                mission.finishedAction);
    }
}
