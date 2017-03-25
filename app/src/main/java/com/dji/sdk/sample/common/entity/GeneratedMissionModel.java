package com.dji.sdk.sample.common.entity;

import com.dji.sdk.sample.common.values.Coordinate;

import java.util.ArrayDeque;
import java.util.Vector;

import dji.sdk.missionmanager.DJIWaypointMission;

/**
 * Created by Julia on 2017-02-04.
 */

public class GeneratedMissionModel
{
    private ArrayDeque<DJIWaypointMission> waypointMissions_;
    private Vector<Coordinate> waypoints_;

    public GeneratedMissionModel()
    {
        waypointMissions_ = new ArrayDeque<>();
    }

    public void addWaypointMission(DJIWaypointMission waypointMission)
    {
        waypointMissions_.add(waypointMission);
    }

    public DJIWaypointMission getNextWaypointMission()
    {
        return waypointMissions_.remove();
    }

    public int waypointMissionCount()
    {
        return waypointMissions_.size();
    }

    public void clearWaypointMissions()
    {
        waypointMissions_.clear();
    }

    public Vector<Coordinate> waypoints()
    {
        return waypoints_;
    }

    public void setWaypoints(Vector<Coordinate> waypoints)
    {
        waypoints_ = waypoints;
    }
}
