package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.I_MissionManager;
import com.dji.sdk.sample.common.integration.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.MissionManager;
import com.dji.sdk.sample.common.integration.MissionManagerSource;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import dji.common.error.DJIError;
import dji.common.flightcontroller.DJILocationCoordinate2D;
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

    public void handleWaypointReached()
    {

    }

    public void resumeMission()
    {

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
                controller.takeOff(MissionHelper.completionCallback(contextManager_, "Taking off Successfully", "Failed to Takeoff"));
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

    @Override
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
            if(!aircraft.getFlightController().getCurrentState().isFlying())
            {
                Toast.makeText(contextManager_.getApplicationContext(),
                        "Aircraft not taken off. Attempting to take off.", Toast.LENGTH_LONG).show();
                takeOff();

                aircraft.getFlightController().getFlightLimitation().setMaxFlightRadiusLimitationEnabled(false, null);
                return;
            }
            else
            {
                Toast.makeText(contextManager_.getApplicationContext(),
                        "Attempting to launch mission", Toast.LENGTH_LONG).show();
            }


            DJIMissionManager missionManager = aircraft.getMissionManager();

            if (missionManager != null && aircraft.getFlightController().getCurrentState().isFlying())
            {
                try
                {
                    DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler()
                    {
                        @Override
                        public void onProgress(DJIMission.DJIProgressType type, float progress) {}
                    };


                    aircraft.getFlightController().getHomeLocation(new DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D>() {
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
        else
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Aircraft is null", Toast.LENGTH_LONG).show();
        }

    }//end startMission

}
