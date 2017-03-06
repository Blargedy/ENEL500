package com.dji.sdk.sample.common.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.integration.src.DJISampleApplication;

import dji.sdk.base.DJIBaseProduct;

/**
 * Created by Julia on 2017-02-04.
 */

public class ProductConnectionPresenter
{
    private TextView connectionStatusText_;
    private ApplicationContextManager contextManager_;
    private BroadcastReceiver receiver_;

    public ProductConnectionPresenter(
            TextView connectionStatusText,
            ApplicationContextManager contextManager)
    {
        connectionStatusText_ = connectionStatusText;
        contextManager_ = contextManager;

        registerConnectionChangedReceiver();
    }

    private void registerConnectionChangedReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.DJI_AIRCRAFT_CONNECTION_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateText();
            }
        };

        contextManager_.getApplicationContext().registerReceiver(receiver_, filter);
    }

    private void updateText()
    {
        DJIBaseProduct product = DJISampleApplication.getProductInstance();
        if (product != null && product.isConnected())
        {
            connectionStatusText_.setText("Status: Connected");
        }
        else
        {
            connectionStatusText_.setText("Status: Not Connected");
        }
    }
}
