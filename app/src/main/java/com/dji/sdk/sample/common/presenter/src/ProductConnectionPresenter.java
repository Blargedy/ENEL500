package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.dji.sdk.sample.common.droneState.api.I_FlightControllerInitializer;
import com.dji.sdk.sample.common.integration.api.I_FlightController;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerUpdateSystemStateCallback;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.integration.src.DJISampleApplication;
import com.dji.sdk.sample.common.view.api.I_MissionControlsView;

import dji.sdk.base.DJIBaseProduct;

/**
 * Created by Julia on 2017-02-04.
 */

public class ProductConnectionPresenter
{
    private I_MissionControlsView missionControlsView_;
    private I_FlightControllerInitializer flightControllerInitializer_;

    private BroadcastReceiver receiver_;

    public ProductConnectionPresenter(
            Context context,
            I_MissionControlsView missionControlsView,
            I_FlightControllerInitializer flightControllerInitializer)
    {
        missionControlsView_ = missionControlsView;
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
            flightControllerInitializer_.initializeFlightController(null);
        }
        else
        {
        }
    }
}
