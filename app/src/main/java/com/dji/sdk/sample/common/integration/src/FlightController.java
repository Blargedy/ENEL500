package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightController;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.sdk.flightcontroller.DJIFlightLimitation;

/**
 * Created by eric7 on 2017-02-28.
 */

public class FlightController implements I_FlightController
{
    DJIFlightController flightController_;

    FlightController(DJIFlightController flightController)
    {
        flightController_ = flightController;
    }

    @Override
    public void setHomeLocationUsingAircraftCurrentLocation(I_CompletionCallback callback)
    {
        flightController_.setHomeLocationUsingAircraftCurrentLocation(callback);
    }

    @Override
    public void setGoHomeBatteryThreshold(int threshold, I_CompletionCallback callback)
    {
        flightController_.setGoHomeBatteryThreshold(threshold, callback);
    }

    @Override
    public void goHome(I_CompletionCallback callback)
    {
        flightController_.goHome(callback);
    }

    @Override
    public void cancelGoHome(I_CompletionCallback callback)
    {
        flightController_.cancelGoHome(callback);
    }

    @Override
    public DJILocationCoordinate2D getAircraftLocation()
    {
        return flightController_.getCurrentState().getAircraftLocation().getCoordinate2D();
    }
}
