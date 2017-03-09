package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.api.I_CustomMissionBuilder;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.List;
import java.util.Vector;

import dji.internal.analytics.listener.DJIMissionManagerEventListener;
import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.missionstep.DJIGoHomeStep;
import dji.sdk.missionmanager.missionstep.DJIGoToStep;
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
        Vector<Coordinate> waypoints = (Vector<Coordinate>)SwitchBackPathGenerator.generateSwitchback(
                initialMissionModel_.missionBoundary().bottomLeft(),
                initialMissionModel_.missionBoundary().topRight(),
                initialMissionModel_.altitude());

        Vector<DJIMissionStep> missionSteps = new Vector<>();

        for(Coordinate waypoint : waypoints)
        {
            DJIGoToStep goToStep = new DJIGoToStep(
                    waypoint.latitude_,
                    waypoint.longitude_,
                    initialMissionModel_.altitude(),
                    null);
            DJIShootPhotoStep shootPhotoStep = new DJIShootPhotoStep(missionStepCompletionCallback_);

            missionSteps.add(goToStep);
            missionSteps.add(shootPhotoStep);
        }

        //TODO add mission completion callback here
        missionSteps.add(new DJIGoHomeStep(null));

        generatedMissionModel_.djiMission_ = new DJICustomMission(missionSteps);
        generatedMissionModel_.waypoints_ = waypoints;
    }
}
