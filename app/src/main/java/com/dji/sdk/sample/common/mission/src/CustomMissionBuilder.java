package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.api.I_CustomMissionBuilder;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.Vector;

import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;

import static dji.sdk.missionmanager.DJIWaypointMission.DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT;

/**
 * Created by eric7 on 2017-02-21.
 */

public class CustomMissionBuilder implements I_CustomMissionBuilder
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;

    public CustomMissionBuilder(
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel)
    {
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
    }
    public void buildCustomMission()
    {
        SwitchBackPathGenerator switchBackPathGenerator = new SwitchBackPathGenerator(
                initialMissionModel_.missionBoundary().bottomLeft(),
                initialMissionModel_.missionBoundary().topRight(),
                initialMissionModel_.altitude());
        Vector<Coordinate> waypoints = (Vector<Coordinate>)switchBackPathGenerator.generateSwitchback();
        generatedMissionModel_.setWaypoints(waypoints);

        DJIWaypointMission mission = new DJIWaypointMission();
        mission.autoFlightSpeed = 10;

        for (int i = 0; i < waypoints.size(); i++)
        {
            if (i % DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT == 0 && i != 0)
            {
                generatedMissionModel_.addWaypointMission(mission);
                mission = new DJIWaypointMission();
                mission.autoFlightSpeed = 10;
            }

            DJIWaypoint waypoint = new DJIWaypoint(
                    waypoints.get(i).latitude_,
                    waypoints.get(i).longitude_,
                    initialMissionModel_.altitude());
            waypoint.addAction(new DJIWaypoint.DJIWaypointAction(
                    DJIWaypoint.DJIWaypointActionType.StartTakePhoto, 0));
            mission.addWaypoint(waypoint);
        }
        generatedMissionModel_.addWaypointMission(mission);
    }
}
