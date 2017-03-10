package com.dji.sdk.sample.common.integration.api;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightLimitation;

/**
 * Created by eric7 on 2017-02-28.
 */

public interface I_FlightController {
    void takeOff(I_CompletionCallback callback);
    DJIFlightControllerCurrentState getCurrentState();
    void getHomeLocation(DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D> callback);
    DJIFlightLimitation getFlightLimitation();
    void setHomeLocationUsingAircraftCurrentLocation(I_CompletionCallback callback);

    void goHome(I_CompletionCallback callback);
    void cancelGoHome(I_CompletionCallback callback);
}
