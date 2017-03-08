package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionCancellationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

/**
 * Created by Matthew on 2017-02-08.
 */

public class MissionController implements I_MissionController
{
    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerSource flightControllerSource_;
    private I_ApplicationContextManager contextManager_;

    public MissionController(
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerSource flightControllerSource,
            I_ApplicationContextManager contextManager)
    {
        missionManagerSource_ = missionManagerSource;
        flightControllerSource_ = flightControllerSource;
        contextManager_ = contextManager;
    }

    @Override
    public void startMission(I_CompletionCallback callback)
    {
        missionManagerSource_.getMissionManager().startMissionExecution(callback);
    }

    @Override
    public void pauseMission(I_CompletionCallback callback)
    {
        missionManagerSource_.getMissionManager().pauseMissionExecution(callback);
    }

    @Override
    public void resumeMission(I_CompletionCallback callback)
    {
        missionManagerSource_.getMissionManager().resumeMissionExecution(callback);
    }

    @Override
    public void cancelMission(I_MissionCancellationCompletionCallback callback)
    {
        //TODO Matt will implement cancel missions here
        // mision.stopMissionExecution()
        // go home
        // call callback when the drone is home

    }

    @Override
    public void pauseGoHome()
    {
        //flightController.cancelGoHome()
    }

    @Override
    public void resumeGoHome()
    {
        //flightController.goHome()
    }
}
