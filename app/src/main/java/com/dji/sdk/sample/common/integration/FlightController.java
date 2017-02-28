package com.dji.sdk.sample.common.integration;

import dji.sdk.flightcontroller.DJIFlightController;

/**
 * Created by eric7 on 2017-02-28.
 */

public class FlightController implements I_FlightController {
    DJIFlightController flightController_;

    FlightController(DJIFlightController flightController)
    {
        flightController_ = flightController;
    }
}
