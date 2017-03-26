package com.dji.sdk.sample.common.droneState.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.dji.sdk.sample.common.droneState.api.I_FlightControllerInitializer;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.integration.src.DJISampleApplication;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;

import dji.sdk.base.DJIBaseProduct;

/**
 * Created by Julia on 2017-02-04.
 */

public class ProductConnectionChangedDetector
{
    private I_MissionStatusNotifier missionStatusNotifier_;
    private I_FlightControllerInitializer flightControllerInitializer_;

    private BroadcastReceiver receiver_;

    public ProductConnectionChangedDetector(
            Context context,
            I_MissionStatusNotifier missionStatusNotifier,
            I_FlightControllerInitializer flightControllerInitializer)
    {
        missionStatusNotifier_ = missionStatusNotifier;
        flightControllerInitializer_ = flightControllerInitializer;

        registerConnectionChangedReceiver(context);
        updateProgressConnectionStatus();
    }

    private void registerConnectionChangedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.DJI_AIRCRAFT_CONNECTION_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateProgressConnectionStatus();
            }
        };

        context.registerReceiver(receiver_, filter);
    }

    private void updateProgressConnectionStatus()
    {
        DJIBaseProduct product = DJISampleApplication.getProductInstance();
        if (product != null && product.isConnected())
        {
            missionStatusNotifier_.notifyStatusChanged("Product Connected");
            flightControllerInitializer_.initializeFlightController(null);
        }
        else
        {
            missionStatusNotifier_.notifyStatusChanged("Product disconnected");
        }
    }
}
