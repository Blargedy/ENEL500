package com.dji.sdk.sample.common.presenter.src;

import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.presenter.api.I_MapPresenter;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.Vector;

import dji.sdk.missionmanager.DJIWaypoint;

/**
 * Created by Julia on 2017-03-08.
 */

public class FakeMapPresenter implements I_MapPresenter
{
    @Override
    public MissionBoundary getSurveyAreaBoundary()
    {
        Coordinate topRight = new Coordinate(50.796276, -114.205159);
        Coordinate bottomLeft = new Coordinate(50.795906, -114.206540);

        return new MissionBoundary(topRight, bottomLeft);
    }

    @Override
    public void displayMissionWaypoints(Vector<DJIWaypoint> waypoints) {

    }

    @Override
    public void clearWaypointsFromMap() {

    }
}
