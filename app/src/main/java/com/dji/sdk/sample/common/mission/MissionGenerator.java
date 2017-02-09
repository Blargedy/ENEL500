package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.missionstep.DJIMissionStep;
import dji.sdk.missionmanager.missionstep.DJIWaypointStep;

/**
 * Created by Julia on 2017-01-15.
 */

public class MissionGenerator implements I_MissionGenerator
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private I_ApplicationContextManager contextManager_;

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

    public MissionGenerator(I_ApplicationContextManager contextManager, InitialMissionModel initialMissionModel, GeneratedMissionModel generatedMissionModel){
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        contextManager_ = contextManager;
    }
    public void generateMission(){
        List<Coordinate> switchbackVector= SwitchBackPathGenerator.generateSwitchback(initialMissionModel_.missionBoundary().bottomLeft(),
                initialMissionModel_.missionBoundary().topRight(), initialMissionModel_.altitude());

        //produce List of waypoints
        Vector<DJIWaypoint> waypoints = new Vector<DJIWaypoint>();
        Vector<DJIWaypointMission> waypointMissions = new Vector<DJIWaypointMission>();
        List<DJIMissionStep> missionSteps = new Vector<DJIMissionStep>();

        Iterator switchBackIter = switchbackVector.iterator();
        while(switchBackIter.hasNext()){
            Coordinate nextPoint = (Coordinate) switchBackIter.next();
            waypoints.add(new DJIWaypoint(nextPoint.latitude_, nextPoint.latitude_, initialMissionModel_.altitude()));
        }

        Iterator waypointIter = waypoints.iterator();
        int waypointCount = 0;
        int wayPointMissionIndex = -1;

        while(waypointIter.hasNext()) {
            if(wayPointMissionIndex == -1 || waypointCount >= 100){
                DJIWaypointMission waypointMission = new DJIWaypointMission();
                waypointMission.autoFlightSpeed = 30;
                waypointMission.goFirstWaypointMode = DJIWaypointMission.DJIWaypointMissionGotoWaypointMode.PointToPoint;
                waypointMission.needExitMissionOnRCSignalLost = true;
                waypointMission.needRotateGimbalPitch = false;
                waypointMission.maxFlightSpeed = 30;


                waypointMissions.add(waypointMission);
                wayPointMissionIndex++;
                waypointMissions.elementAt(waypointCount).addWaypoint((DJIWaypoint) waypointIter.next());
            }
            else {
                waypointMissions.elementAt(waypointCount).addWaypoint((DJIWaypoint) waypointIter.next());
                waypointCount++;
            }
        }

        Iterator waypointMissionIter = waypointMissions.iterator();

        while(waypointMissionIter.hasNext()){
            missionSteps.add(new DJIWaypointStep((DJIWaypointMission) waypointMissionIter.next(), new DJICommonCallbacks.DJICompletionCallback(){
                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Successfully Generated Mission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                error.getDescription(), Toast.LENGTH_LONG).show();
                    }
                }
            }));
        }

        //set entity
        generatedMissionModel_.djiMission_ =  new DJICustomMission(missionSteps);
        generatedMissionModel_.waypoints_ = waypoints;

    }
}
