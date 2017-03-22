package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.Vector;

import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;

import static dji.sdk.missionmanager.DJIWaypointMission.DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionGenerator implements I_MissionGenerator
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private SwitchBackPathGenerator switchBackPathGenerator_;

    public MissionGenerator(
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel,
            SwitchBackPathGenerator switchBackPathGenerator)
    {
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        switchBackPathGenerator_ = switchBackPathGenerator;
    }
    public void generateMission()
    {
        Vector<Coordinate> waypoints = switchBackPathGenerator_.generateSwitchback();
        generatedMissionModel_.setWaypoints(waypoints);

        DJIWaypointMission mission = null;
        for (int i = 0; i < waypoints.size(); i++)
        {
            if (i % DJI_WAYPOINT_MISSION_MAXIMUM_WAYPOINT_COUNT == 0)
            {
                if (i != 0)
                {
                    generatedMissionModel_.addWaypointMission(mission);
                }
                mission = new DJIWaypointMission();
                mission.needRotateGimbalPitch = true;
                mission.autoFlightSpeed = 5;
            }

            DJIWaypoint waypoint = new DJIWaypoint(
                    waypoints.get(i).latitude_,
                    waypoints.get(i).longitude_,
                    initialMissionModel_.altitude());
            waypoint.gimbalPitch = -90f;
            mission.addWaypoint(waypoint);
        }
        generatedMissionModel_.addWaypointMission(mission);
    }
}
