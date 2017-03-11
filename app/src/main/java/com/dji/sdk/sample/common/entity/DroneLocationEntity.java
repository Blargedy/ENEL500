package com.dji.sdk.sample.common.entity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.values.Coordinate;

/**
 * Created by Julia on 2017-03-11.
 */

public class DroneLocationEntity
{
    private Coordinate droneLocation_;

    private Context context_;

    public DroneLocationEntity(Context context)
    {
        context_ = context;
    }

    public void setDroneLocation(Coordinate droneLocation)
    {
        droneLocation_ = droneLocation;

        Intent intent = new Intent(BroadcastIntentNames.DRONE_LOCATION_CHANGED);
        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
    }

    public Coordinate droneLocation()
    {
        return droneLocation_;
    }
}
