package com.dji.sdk.sample.common.droneState.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.droneState.api.I_FlightControllerInitializer;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-18.
 */

public class FlightControllerInitializer implements
        I_FlightControllerInitializer,
        I_CompletionCallback
{
    private static final String TAG = "HydraFlightControllerInitializer";

    private enum ExpectedCallback { SET_HOME_LOCATION, SET_GO_HOME_BATTERY_THRESHOLD }
    private ExpectedCallback expectedCallback_;
    private I_CompletionCallback callback_;

    private I_FlightControllerSource flightControllerSource_;

    public FlightControllerInitializer(
            I_FlightControllerSource flightControllerSource)
    {
        flightControllerSource_ = flightControllerSource;
    }

    @Override
    public void initializeFlightController(I_CompletionCallback callback)
    {
        callback_ = callback;

        expectedCallback_ = ExpectedCallback.SET_HOME_LOCATION;
        flightControllerSource_.getFlightController().setHomeLocationUsingAircraftCurrentLocation(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case SET_HOME_LOCATION:
                    expectedCallback_ = ExpectedCallback.SET_GO_HOME_BATTERY_THRESHOLD;
                    flightControllerSource_.getFlightController().setGoHomeBatteryThreshold(25, this);
                    break;

                case SET_GO_HOME_BATTERY_THRESHOLD:
                    callback(null);
                    break;

                default:
                    break;
            }
        }
        else
        {
            Log.e(TAG, error.toString());
            callback(error);
        }
    }

    private void callback(DJIError error)
    {
        if (callback_!= null)
        {
            callback_.onResult(error);
        }
    }
}
