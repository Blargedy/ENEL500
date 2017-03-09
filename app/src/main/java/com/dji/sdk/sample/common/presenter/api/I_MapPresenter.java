package com.dji.sdk.sample.common.presenter.api;

import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.Vector;

import dji.sdk.missionmanager.DJIWaypoint;

/**
 * Created by Julia on 2017-03-08.
 */

public interface I_MapPresenter
{
    MissionBoundary getSurveyAreaBoundary();
    void displayMissionWaypoints(Vector<Coordinate> waypoints);
    void clearMap();

    void enableAllControls();
    void disableAllControls();
}
