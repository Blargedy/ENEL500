package com.dji.sdk.sample.common.mission;

import com.dji.sdk.sample.common.utility.DJISampleApplication;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightController;

/**
 * Created by Julia on 2017-01-18.
 */

public class FlightController
{
    public void takeoff(DJICommonCallbacks.DJICompletionCallback callback)
    {
        DJIFlightController flightController =
                DJISampleApplication.getAircraftInstance().getFlightController();

        flightController.takeOff(callback);
    }

    public void land(DJICommonCallbacks.DJICompletionCallback callback)
    {
        DJIFlightController flightController =
                DJISampleApplication.getAircraftInstance().getFlightController();

        flightController.autoLanding(callback);
    }

    public void turnOffMotors()
    {
        DJIFlightController flightController =
                DJISampleApplication.getAircraftInstance().getFlightController();

        flightController.turnOffMotors(null);
    }

    public boolean isAltitudeZero()
    {
        DJIFlightController flightController =
                DJISampleApplication.getAircraftInstance().getFlightController();

        DJIFlightControllerCurrentState currentState = flightController.getCurrentState();

        return currentState.getAircraftLocation().getAltitude() == 0.0f;
    }
}
