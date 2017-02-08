package com.dji.sdk.sample.common.mission;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.missionstep.DJIMissionStep;

/**
 * Created by Julia on 2017-01-15.
 */

public class MissionGenerator implements I_MissionGenerator
{
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
    public void generateMission(){
        List<Coordinate> switchbackVector= SwitchBackPathGenerator.generateSwitchback(initialMissionModel.missionBoundary().bottomLeft(),
                initialMissionModel.missionBoundary().topRight(),initialMissionModel.altitude());

        //produce List of waypoints
        Vector<DJIWaypoint> waypoints = new Vector<DJIWaypoint>();
        Vector<DJIWaypointMission> waypointMissions = new Vector<DJIWaypointMission>();
        List<DJIMissionStep> missionSteps = new Vector<DJIMissionStep>();

        Iterator switchBackIter = switchbackVector.iterator();
        while(switchBackIter.hasNext()){
            Coordinate nextPoint = (Coordinate) switchBackIter.next();
            waypoints.add(new DJIWaypoint(nextPoint.latitude_, nextPoint.latitude_, initialMissionModel.altitude()));
        }

        Iterator waypointIter = waypoints.iterator();
        int waypointCount = 0;
        int wayPointMissionIndex = -1;

        while(waypointIter.hasNext()) {
            if(wayPointMissionIndex == -1){
                waypointMissions.add(new DJIWaypointMission());
                wayPointMissionIndex++;
                waypointMissions.elementAt(waypointCount).addWaypoint((DJIWaypoint) waypointIter.next());
            }
            else if(waypointCount < 100){
                waypointMissions.elementAt(waypointCount).addWaypoint((DJIWaypoint) waypointIter.next());
                waypointCount++;
            }
            else{
                waypointMissions.add(new DJIWaypointMission());
                wayPointMissionIndex++;
            }
        }
        

        //set entity
        generatedMissionModel.djiMission_ =  new DJICustomMission(missionSteps);
        generatedMissionModel.waypoints_ = waypoints;

    }
}
