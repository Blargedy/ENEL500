package com.dji.sdk.sample.common.integration;

import dji.sdk.flightcontroller.DJIFlightController;

/**
 * Created by eric7 on 2017-02-21.
 */

public interface I_MissionManager {

    public void pauseMissionExecution(I_CompletionCallback callback);
    public void resumeMissionExecution(I_CompletionCallback callback);
    public DJIFlightController getFlightController();

}
