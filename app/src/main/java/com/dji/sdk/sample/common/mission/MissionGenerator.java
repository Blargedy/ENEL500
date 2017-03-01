package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.I_MissionManager;
import com.dji.sdk.sample.common.integration.I_MissionManagerSource;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by Julia on 2017-01-15.
 */

public class MissionGenerator implements I_MissionGenerator
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private I_ApplicationContextManager contextManager_;
    private CustomMissionBuilder customMissionBuilder_;
    private StepCompletionCallback stepCompletionCallback_;
    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerSource flightControllerSource_;
    private I_ImageTransferer imageTransferer_;

    public MissionGenerator(I_ApplicationContextManager contextManager,
                            InitialMissionModel initialMissionModel,
                            GeneratedMissionModel generatedMissionModel,
                            I_MissionManagerSource missionManagerSource,
                            I_FlightControllerSource flightControllerSource,
                            StepCompletionCallback stepCompletionCallback){
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        missionManagerSource_ = missionManagerSource;
        flightControllerSource_ = flightControllerSource;
        contextManager_ = contextManager;
        stepCompletionCallback_ = stepCompletionCallback;
        customMissionBuilder_ = new CustomMissionBuilder(initialMissionModel, generatedMissionModel, contextManager, stepCompletionCallback_);
    }

    public void generateMission(){

        customMissionBuilder_.buildCustomMission();
//        DJIAircraft aircraft = (DJIAircraft) DJISDKManager.getInstance().getDJIProduct();
//        DJIMissionManager missionManager = aircraft.getMissionManager();


        DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler()
        {
            @Override
            public void onProgress(DJIMission.DJIProgressType type, float progress) {}
        };

        flightControllerSource_.getFlightController().setHomeLocationUsingAircraftCurrentLocation(new I_CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError == null)
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "set home location current" , Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "can't set home location" + djiError.getDescription(), Toast.LENGTH_LONG).show();
                }
            }
        });

        missionManagerSource_.getMissionManager().prepareMission(generatedMissionModel_.djiMission_, progressHandler, new I_CompletionCallback()
        {
            @Override
            public void onResult(DJIError error) {
                if (error == null)
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "Prepared Mission Successfully", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "Failed to Prepare Mission " + error.getDescription(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
