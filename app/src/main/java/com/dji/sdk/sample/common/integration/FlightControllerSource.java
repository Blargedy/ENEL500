package com.dji.sdk.sample.common.integration;

import android.util.Log;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.products.DJIAircraft;

/**
 * Created by eric7 on 2017-02-28.
 */

public class FlightControllerSource implements I_FlightControllerSource{

    private static final String TAG = "FlightControllerSource";

    @Override
    public I_FlightController getFlightController()
    {
        DJIAircraft aircraft = (DJIAircraft) DJISampleApplication.getProductInstance();
        if (aircraft != null)
        {
            Log.d(TAG, "Product not null");

            DJIFlightController flightController = aircraft.getFlightController();

            if(flightController != null)
            {
                Log.d(TAG, "Mission manager not null");

                return new FlightController(flightController);
            }
        }
        return null;
    }
}
