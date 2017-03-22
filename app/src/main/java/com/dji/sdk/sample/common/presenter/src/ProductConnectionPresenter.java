package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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

    private BroadcastReceiver receiver_;

    public ProductConnectionPresenter(
            Context context,
            I_MissionControlsView missionControlsView)
    {
        missionControlsView_ = missionControlsView;

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
    }
}
