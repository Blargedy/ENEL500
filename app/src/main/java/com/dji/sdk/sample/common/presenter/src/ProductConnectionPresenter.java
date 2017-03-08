package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Button;
import android.widget.TextView;

import com.dji.sdk.sample.common.testClasses.MapViewTest;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.integration.src.DJISampleApplication;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.sdk.base.DJIBaseProduct;

/**
 * Created by Julia on 2017-02-04.
 */

public class ProductConnectionPresenter
{
    private TextView connectionStatusText_;
    private Button generateMissionButton_;
    private Button transferImagesButton_;
    private Button selectSurveyAreaButton_;

    private I_ApplicationContextManager contextManager_;
    private BroadcastReceiver receiver_;

    public ProductConnectionPresenter(
            MapViewTest view,
            I_ApplicationContextManager contextManager)
    {
        connectionStatusText_ = view.txt_console();
        generateMissionButton_ = view.generateMissionButton();
        transferImagesButton_ = view.transferImagesButton();
        selectSurveyAreaButton_ = view.btn_mainButton();

        contextManager_ = contextManager;

        registerConnectionChangedReceiver();
        updateProgressConnectionStatus();
    }

    private void registerConnectionChangedReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.DJI_AIRCRAFT_CONNECTION_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateProgressConnectionStatus();
            }
        };

        contextManager_.getApplicationContext().registerReceiver(receiver_, filter);
    }

    private void updateProgressConnectionStatus()
    {
        DJIBaseProduct product = DJISampleApplication.getProductInstance();
        if (product != null && product.isConnected())
        {
            connectionStatusText_.setText("Status: Product Connected");
            generateMissionButton_.setEnabled(true);
            transferImagesButton_.setEnabled(true);
            //selectSurveyAreaButton_.setEnabled(true);
        }
        else
        {
            connectionStatusText_.setText("Status: Product Not Connected");
            generateMissionButton_.setEnabled(false);
            transferImagesButton_.setEnabled(false);
            //selectSurveyAreaButton_.setEnabled(false);
        }
    }
}
