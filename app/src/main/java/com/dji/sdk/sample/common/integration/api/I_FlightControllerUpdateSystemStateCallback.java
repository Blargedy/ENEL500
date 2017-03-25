package com.dji.sdk.sample.common.integration.api;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;

/**
 * Created by Julia on 2017-03-25.
 */

public interface I_FlightControllerUpdateSystemStateCallback extends
        DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback
{
    void onResult(DJIFlightControllerCurrentState state);
}
