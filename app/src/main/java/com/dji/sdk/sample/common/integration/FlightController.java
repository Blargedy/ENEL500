package com.dji.sdk.sample.common.integration;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.sdk.flightcontroller.DJIFlightLimitation;

/**
 * Created by eric7 on 2017-02-28.
 */

public class FlightController implements I_FlightController {
    DJIFlightController flightController_;

    FlightController(DJIFlightController flightController)
    {
        flightController_ = flightController;
    }

    @Override
    public void takeOff(I_CompletionCallback callback)
    {
        flightController_.takeOff(callback);
    }

    @Override
    public DJIFlightControllerCurrentState getCurrentState()
    {
        return flightController_.getCurrentState();
    }

    @Override
    public void getHomeLocation(DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D> callback) {
        flightController_.getHomeLocation(callback);
    }

    @Override
    public DJIFlightLimitation getFlightLimitation()
    {
        return flightController_.getFlightLimitation();
    }
}
