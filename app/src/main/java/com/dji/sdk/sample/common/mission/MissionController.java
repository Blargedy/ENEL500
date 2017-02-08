package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;


/**
 * Created by Matthew on 2017-02-08.
 */

public class MissionController implements I_MissionController {
    private GeneratedMissionModel missionModel_;
    private I_ApplicationContextManager contextManager_;

    public MissionController(
            I_ApplicationContextManager contextManager,
            GeneratedMissionModel missionModel)
    {
        contextManager_ = contextManager;
        missionModel_ = missionModel;
    }

    public void takeOff()
    {
        DJIBaseProduct baseProduct = DJISDKManager.getInstance().getDJIProduct();
        DJIAircraft aircraft = null;
        if(null != baseProduct)
        {
            if(baseProduct instanceof DJIAircraft)
            {
                aircraft = (DJIAircraft) baseProduct;
            }
            else
            {
                Toast.makeText(contextManager_.getApplicationContext(),
                        "no instance of DJIAircaft", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "baseProduct is null", Toast.LENGTH_LONG).show();
        }

        if(aircraft != null)
        {
            DJIFlightController controller = aircraft.getFlightController();

            if(controller != null)
            {
                controller.takeOff(new DJICommonCallbacks.DJICompletionCallback()
                {
                    @Override
                    public void onResult(DJIError error) {
                        if (error == null)
                        {
                            Toast.makeText(contextManager_.getApplicationContext(), "Taking off Successfully", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(contextManager_.getApplicationContext(), "Failed to Takeoff", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(contextManager_.getApplicationContext(),
                        "Flight Controller is null", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Aircraft is null", Toast.LENGTH_LONG).show();
        }
    }

    public void startMission()
    {
        DJIBaseProduct baseProduct = DJISDKManager.getInstance().getDJIProduct();
        DJIAircraft aircraft = null;
        if(null != baseProduct)
        {
            if(baseProduct instanceof DJIAircraft)
            {
                aircraft = (DJIAircraft) baseProduct;
            }
            else
            {
                Toast.makeText(contextManager_.getApplicationContext(),
                        "no instance of DJIAircaft", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "baseProduct is null", Toast.LENGTH_LONG).show();
        }

        if(aircraft != null)
        {
            DJIMissionManager missionManager = aircraft.getMissionManager();

            if (missionManager != null)
            {
                try
                {
                    DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler()
                    {
                        @Override
                        public void onProgress(DJIMission.DJIProgressType type, float progress) {}
                    };


                    missionManager.prepareMission(missionModel_.djiMission_, progressHandler, new DJICommonCallbacks.DJICompletionCallback()
                    {
                        @Override
                        public void onResult(DJIError error) {
                            if (error == null)
                            {
                                Toast.makeText(contextManager_.getApplicationContext(), "Prepared Mission Successfully", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(contextManager_.getApplicationContext(), "Failed to Prepare Mission", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    missionManager.startMissionExecution(new DJICommonCallbacks.DJICompletionCallback()
                    {
                        @Override
                        public void onResult(DJIError error) {
                            if (error == null)
                            {
                                Toast.makeText(contextManager_.getApplicationContext(), "Started Mission Successfully", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(contextManager_.getApplicationContext(), "Failed to Start Mission", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


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
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Aircraft is null", Toast.LENGTH_LONG).show();
        }

    }//end startMission
}
