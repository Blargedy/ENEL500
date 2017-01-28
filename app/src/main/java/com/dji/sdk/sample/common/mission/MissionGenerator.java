package com.dji.sdk.sample.common.mission;

import java.util.List;
import java.util.Vector;

import dji.common.flightcontroller.DJIFlightControllerDataType;
import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.missionstep.DJIMissionStep;

/**
 * Created by Julia on 2017-01-15.
 */

public class MissionGenerator
{
    private float altitude = 1.0f;
    public class Coordinate{
        double latitude;
        double longitude;
    }

    public class Boundary {
        Coordinate topRight;
        Coordinate topLeft;
        Coordinate bottomRight;
        Coordinate bottomLeft;
    }

    public DJIWaypointMission generateMissionWithOneWaypoint(double latitude, double longitude)
    {
        DJIWaypointMission waypointMission = new DJIWaypointMission();
        waypointMission.finishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.GoHome;
        waypointMission.headingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.Auto;
        waypointMission.autoFlightSpeed = 2.0f;

        DJIWaypoint waypoint1 = new DJIWaypoint(latitude, longitude, altitude);
        DJIWaypoint waypoint2 = new DJIWaypoint(latitude, longitude, altitude * 2);

        waypointMission.addWaypoint(waypoint1);
        waypointMission.addWaypoint(waypoint2);

        return waypointMission;
    }

    public DJICustomMission generateMissionWithMultipleWaypoints(Boundary boundary, double altitude){
        List<DJIMissionStep> missionPoints = new Vector<DJIMissionStep>();
        List<Coordinate> switchbackVector= SwitchBackPathGenerator.generateSwitchback(boundary,altitude);
        return new DJICustomMission(missionPoints);
    }
}
