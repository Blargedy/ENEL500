package com.dji.sdk.sample.common.mission;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;

import java.util.List;
import java.util.Vector;

import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.missionstep.DJIMissionStep;

/**
 * Created by Julia on 2017-01-15.
 */

public class MissionGenerator implements I_MissionGenerator
{
    private float altitude = 1.0f;
    private InitialMissionModel initialMissionModel;
    private GeneratedMissionModel generatedMissionModel;
//    public DJIWaypointMission generateMissionWithOneWaypoint(double latitude, double longitude)
//    {
//        DJIWaypointMission waypointMission = new DJIWaypointMission();
//        waypointMission.finishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.GoHome;
//        waypointMission.headingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.Auto;
//        waypointMission.autoFlightSpeed = 2.0f;
//
//        DJIWaypoint waypoint1 = new DJIWaypoint(latitude, longitude, altitude);
//        DJIWaypoint waypoint2 = new DJIWaypoint(latitude, longitude, altitude * 2);
//
//        waypointMission.addWaypoint(waypoint1);
//        waypointMission.addWaypoint(waypoint2);
//
//        return waypointMission;
//    }

    public MissionGenerator(InitialMissionModel initialMissionModel_, GeneratedMissionModel generatedMissionModel_){
        initialMissionModel = initialMissionModel_;
        generatedMissionModel = generatedMissionModel_;
    }
    public void generateMission(MissionBoundary boundary, double altitude){
        List<Coordinate> switchbackVector= SwitchBackPathGenerator.generateSwitchback(boundary,altitude);

        //produce List of waypoints
        Vector<DJIWaypoint> waypoints_ = new Vector<DJIWaypoint>();

        //Add waypoints to Steps
        List<DJIMissionStep> missionPoints = new Vector<DJIMissionStep>();

        //set entity
        generatedMissionModel.djiMission_ =  new DJICustomMission(missionPoints);
        generatedMissionModel.waypoints_ = waypoints_;

    }
}
