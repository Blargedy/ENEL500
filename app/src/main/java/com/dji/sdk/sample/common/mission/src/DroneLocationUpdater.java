package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.mission.api.I_DroneLocationUpdater;
import com.dji.sdk.sample.common.values.Coordinate;

import dji.common.flightcontroller.DJILocationCoordinate2D;

/**
 * Created by Julia on 2017-03-11.
 */

public class DroneLocationUpdater implements I_DroneLocationUpdater
{
    private DroneLocationEntity droneLocation_;
    private I_FlightControllerSource flightControllerSource_;

    public DroneLocationUpdater(
            DroneLocationEntity droneLocation,
            I_FlightControllerSource flightControllerSource)
    {
        droneLocation_ = droneLocation;
        flightControllerSource_ = flightControllerSource;
    }

    @Override
    public void updateDroneLocation()
    {
        DJILocationCoordinate2D location =
                flightControllerSource_.getFlightController().getAircraftLocation();

        droneLocation_.setDroneLocation(new Coordinate(
                location.getLatitude(),
                location.getLongitude()));
    }
}
