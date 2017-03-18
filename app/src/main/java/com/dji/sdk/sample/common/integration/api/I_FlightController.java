package com.dji.sdk.sample.common.integration.api;

import dji.common.flightcontroller.DJILocationCoordinate2D;

/**
 * Created by eric7 on 2017-02-28.
 */

public interface I_FlightController
{
    void setHomeLocationUsingAircraftCurrentLocation(I_CompletionCallback callback);
    void setGoHomeBatteryThreshold(int threshold, I_CompletionCallback callback);
    void goHome(I_CompletionCallback callback);
    void cancelGoHome(I_CompletionCallback callback);
    DJILocationCoordinate2D getAircraftLocation();
}
