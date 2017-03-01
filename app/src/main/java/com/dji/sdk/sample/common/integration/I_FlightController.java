package com.dji.sdk.sample.common.integration;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightLimitation;

/**
 * Created by eric7 on 2017-02-28.
 */

public interface I_FlightController {
    public void takeOff(I_CompletionCallback callback);
    public DJIFlightControllerCurrentState getCurrentState();
    public void getHomeLocation(DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D> callback);
    public DJIFlightLimitation getFlightLimitation();
    public void setHomeLocationUsingAircraftCurrentLocation(I_CompletionCallback callback);
}
