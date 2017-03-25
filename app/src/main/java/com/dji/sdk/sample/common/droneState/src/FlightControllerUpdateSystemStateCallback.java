package com.dji.sdk.sample.common.droneState.src;

import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerUpdateSystemStateCallback;
import com.dji.sdk.sample.common.values.Coordinate;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJILocationCoordinate2D;

/**
 * Created by Julia on 2017-03-25.
 */

public class FlightControllerUpdateSystemStateCallback implements
        I_FlightControllerUpdateSystemStateCallback
{
    private DroneLocationEntity droneLocation_;

    public FlightControllerUpdateSystemStateCallback(
            DroneLocationEntity droneLocation)
    {
        droneLocation_ = droneLocation;
    }

    @Override
    public void onResult(DJIFlightControllerCurrentState state)
    {
        DJILocationCoordinate2D location = state.getAircraftLocation().getCoordinate2D();

        droneLocation_.setDroneLocation(new Coordinate(
                location.getLatitude(),
                location.getLongitude()));
    }
}
