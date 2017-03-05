package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.integration.api.I_FlightController;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;


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

    public void pauseMission()
    {
        missionManagerSource_.getMissionManager().pauseMissionExecution(
                MissionHelper.completionCallback(
                    contextManager_, "Paused mission to transfer photos",
                    "Could not pause mission to transfer photos: "));
    }

    public void resumeMission()
    {
        missionManagerSource_.getMissionManager().resumeMissionExecution(
                MissionHelper.completionCallback(
                contextManager_,"Resumed mission","Could not resume mission:"));
    }

    public void takeOff()
    {
        flightControllerSource_.getFlightController().takeOff(
                MissionHelper.completionCallback(contextManager_,
                    "Taking off Successfully", "Failed to Takeoff"));
    }

    @Override
    public void startMission()
    {
        I_FlightController flightController = flightControllerSource_.getFlightController();
        I_MissionManager missionManager = missionManagerSource_.getMissionManager();

        if(!flightController.getCurrentState().isFlying())
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Aircraft not taken off. Attempting to take off.", Toast.LENGTH_LONG).show();
            takeOff();

            flightController.getFlightLimitation().setMaxFlightRadiusLimitationEnabled(false, null);
            return;
        }
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Attempting to launch mission", Toast.LENGTH_LONG).show();
        }

        if (missionManager != null && flightController.getCurrentState().isFlying())
        {
            try
            {
                flightController.getHomeLocation(new DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D>() {
                    @Override
                    public void onSuccess(DJILocationCoordinate2D djiLocationCoordinate2D) {
                        Toast.makeText(contextManager_.getApplicationContext(), "Home location "+ djiLocationCoordinate2D.getLatitude() +
                                ", " + djiLocationCoordinate2D.getLongitude(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(DJIError djiError) {
                        Toast.makeText(contextManager_.getApplicationContext(), "Could not get home location " + djiError.getDescription(), Toast.LENGTH_LONG).show();
                    }
                });

                missionManager.startMissionExecution(MissionHelper.completionCallback(
                        contextManager_, "Started Mission Successfully ","Failed to Prepare Mission. Exiting"));

            } catch (Throwable e)
            {
                Toast.makeText(contextManager_.getApplicationContext(), "Failed to Prepare Mission. Exiting", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(), "Mission manager is null", Toast.LENGTH_LONG).show();
        }
    }
}
