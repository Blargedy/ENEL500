package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.values.Coordinate;

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
 * Created by eric7 on 2017-02-21.
 */

public class CustomMissionBuilder implements I_CustomMissionBuilder {
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private I_ApplicationContextManager contextManager_;
    private StepCompletionCallback stepCompletionCallback_;

    public CustomMissionBuilder(
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel,
            I_ApplicationContextManager contextManager,
            StepCompletionCallback stepCompletionCallback){

        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        contextManager_ = contextManager;
        stepCompletionCallback_ = stepCompletionCallback;
    }
    public void buildCustomMission() {
        List<Coordinate> switchbackVector = SwitchBackPathGenerator.generateSwitchback(initialMissionModel_.missionBoundary().bottomLeft(),
                initialMissionModel_.missionBoundary().topRight(), initialMissionModel_.altitude());

        //produce List of waypoints
        Vector<DJIWaypoint> waypoints = new Vector<DJIWaypoint>();
        Vector<DJIWaypointMission> waypointMissions = new Vector<DJIWaypointMission>();
        List<DJIMissionStep> missionSteps = new Vector<DJIMissionStep>();

        Iterator switchBackIter = switchbackVector.iterator();
        while (switchBackIter.hasNext()) {
            Coordinate nextPoint = (Coordinate) switchBackIter.next();
            waypoints.add(new DJIWaypoint(nextPoint.latitude_, nextPoint.longitude_, initialMissionModel_.altitude()));
        }

        Iterator waypointIter = waypoints.iterator();
        int waypointCount = 0;
        int wayPointMissionIndex = -1;

        while (waypointIter.hasNext()) {
            if (wayPointMissionIndex == -1 || waypointCount >= 99) {
                waypointCount = 0;
                DJIWaypointMission waypointMission = new DJIWaypointMission();
                waypointMission.autoFlightSpeed = 10;

                waypointMissions.add(waypointMission);
                wayPointMissionIndex++;
                waypointMissions.elementAt(wayPointMissionIndex).addWaypoint((DJIWaypoint) waypointIter.next());
                waypointCount++;
            } else {
                waypointMissions.elementAt(wayPointMissionIndex).addWaypoint((DJIWaypoint) waypointIter.next());
                waypointCount++;
            }
        }

        Iterator waypointMissionIter = waypointMissions.iterator();

        while (waypointMissionIter.hasNext()) {
            DJIMissionStep nextStep = new DJIWaypointStep((DJIWaypointMission) waypointMissionIter.next(), stepCompletionCallback_);
            missionSteps.add(nextStep);
        }

        //set entity
        generatedMissionModel_.djiMission_ = new DJICustomMission(missionSteps);
    }
}
