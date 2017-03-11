package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_FlightController;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;

/**
 * Created by eric7 on 2017-02-28.
 */

public class FlightControllerSource implements I_FlightControllerSource
{
    @Override
    public I_FlightController getFlightController()
    {
        return new FlightController(DJISampleApplication.getAircraftInstance().getFlightController());
    }
}
