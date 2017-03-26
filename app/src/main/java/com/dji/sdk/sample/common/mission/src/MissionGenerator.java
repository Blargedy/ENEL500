package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.Vector;

import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;

import static com.dji.sdk.sample.common.utility.MissionParameters.WAYPOINTS_PER_MISSION;

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
            if (i % WAYPOINTS_PER_MISSION == 0)
            {
                if (i != 0)
                {
                    generatedMissionModel_.addWaypointMission(mission);
                }
                mission = new DJIWaypointMission();
                mission.needRotateGimbalPitch = true;
                mission.autoFlightSpeed = initialMissionModel_.missionSpeed();
            }

            DJIWaypoint waypoint = new DJIWaypoint(
                    waypoints.get(i).latitude_,
                    waypoints.get(i).longitude_,
                    initialMissionModel_.altitude());
            waypoint.gimbalPitch = -90f;
            waypoint.speed = initialMissionModel_.missionSpeed();

            mission.addWaypoint(waypoint);
        }
        generatedMissionModel_.addWaypointMission(mission);
    }
}
