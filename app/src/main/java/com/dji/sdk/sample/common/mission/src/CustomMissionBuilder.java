package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.api.I_CustomMissionBuilder;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.List;
import java.util.Vector;

import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.missionstep.DJIMissionStep;
import dji.sdk.missionmanager.missionstep.DJIShootPhotoStep;
import dji.sdk.missionmanager.missionstep.DJIWaypointStep;

/**
 * Created by eric7 on 2017-02-21.
 */

public class CustomMissionBuilder implements I_CustomMissionBuilder
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private I_ApplicationContextManager contextManager_;
    private MissionStepCompletionCallback missionStepCompletionCallback_;

    public CustomMissionBuilder(
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel,
            I_ApplicationContextManager contextManager,
            MissionStepCompletionCallback missionStepCompletionCallback)
    {
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        contextManager_ = contextManager;
        missionStepCompletionCallback_ = missionStepCompletionCallback;
    }
    public void buildCustomMission()
    {
        List<Coordinate> switchbackVector = SwitchBackPathGenerator.generateSwitchback(
                initialMissionModel_.missionBoundary().bottomLeft(),
                initialMissionModel_.missionBoundary().topRight(),
                initialMissionModel_.altitude());

        Vector<DJIWaypoint> waypoints = new Vector<DJIWaypoint>();
        Vector<DJIWaypointMission> waypointMissions = new Vector<DJIWaypointMission>();
        List<DJIMissionStep> missionSteps = new Vector<DJIMissionStep>();

        for(Coordinate nextPoint : switchbackVector)
        {
            DJIWaypoint waypoint = new DJIWaypoint(
                nextPoint.latitude_,
                nextPoint.longitude_,
                initialMissionModel_.altitude());
            waypoints.add(waypoint);
        }

        int waypointCount = 0;
        int wayPointMissionIndex = -1;

        for (DJIWaypoint waypoint : waypoints)
        {
            if (wayPointMissionIndex == -1 || waypointCount >= 99)
            {
                waypointCount = 0;
                DJIWaypointMission waypointMission = new DJIWaypointMission();
                waypointMission.autoFlightSpeed = 10;

                waypointMissions.add(waypointMission);
                wayPointMissionIndex++;
            }

            waypointMissions.elementAt(wayPointMissionIndex).addWaypoint(waypoint);
            waypointCount++;
        }

        for (DJIWaypointMission waypointMission : waypointMissions)
        {
            DJIMissionStep nextStep = new DJIWaypointStep(
                    waypointMission,
                    missionStepCompletionCallback_);
            missionSteps.add(nextStep);

            DJIShootPhotoStep photoStep = new DJIShootPhotoStep(MissionHelper.completionCallback(
                    contextManager_,"Shot Photo", "Could not shoot photo"));
            missionSteps.add(photoStep);
        }

        generatedMissionModel_.djiMission_ = new DJICustomMission(missionSteps);
        generatedMissionModel_.waypoints_ = waypoints;
    }
}
