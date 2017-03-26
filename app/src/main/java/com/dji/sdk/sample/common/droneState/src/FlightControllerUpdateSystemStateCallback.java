package com.dji.sdk.sample.common.droneState.src;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerUpdateSystemStateCallback;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
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
    private Context context_;

    private boolean wasPreviouslyGoingHome_;

    public FlightControllerUpdateSystemStateCallback(
            DroneLocationEntity droneLocation,
            Context context)
    {
        droneLocation_ = droneLocation;
        context_ = context;

        wasPreviouslyGoingHome_ = false;
    }

    @Override
    public void onResult(DJIFlightControllerCurrentState state)
    {
        DJILocationCoordinate2D location = state.getAircraftLocation().getCoordinate2D();

        droneLocation_.setDroneLocation(new Coordinate(
                location.getLatitude(),
                location.getLongitude()));

        if (wasPreviouslyGoingHome_ && !state.isGoingHome() && !state.isFlying())
        {
            Intent intent = new Intent(BroadcastIntentNames.DRONE_REACHED_HOME);
            LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
        }

        wasPreviouslyGoingHome_ = state.isGoingHome();
    }
}
