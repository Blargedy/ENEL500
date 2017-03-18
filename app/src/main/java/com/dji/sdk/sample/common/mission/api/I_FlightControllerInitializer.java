package com.dji.sdk.sample.common.mission.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-03-18.
 */

public interface I_FlightControllerInitializer
{
    void initializeFlightController(I_CompletionCallback callback);
}
