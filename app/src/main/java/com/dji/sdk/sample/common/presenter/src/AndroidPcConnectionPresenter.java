package com.dji.sdk.sample.common.presenter.src;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dji.sdk.sample.common.activity.FlightControlActivity;
import com.dji.sdk.sample.common.imageTransfer.src.AndroidPcConnectionTester;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;
import com.dji.sdk.sample.common.view.src.AndroidPcConnectionView;

/**
 * Created by Julia on 2017-03-26.
 */

public class AndroidPcConnectionPresenter implements View.OnClickListener
{
    private Context context_;
    private AndroidPcConnectionView view_;
    private AndroidPcConnectionTester connectionTester_;

    private String pcIpAddress_;

    public AndroidPcConnectionPresenter(
            Context context,
            AndroidPcConnectionView view,
            AndroidPcConnectionTester connectionTester)
    {
        context_ = context;
        view_ = view;
        connectionTester_ = connectionTester;

        setOnClickListeners();
    }

    private void setOnClickListeners()
    {
        view_.testConnectionButton().setOnClickListener(this);
        view_.continueButton().setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == view_.testConnectionButton().getId())
        {
            testConnection();
        }
        else if (v.getId() == view_.continueButton().getId())
        {
            launchFlightControlActivity();
        }
    }

    private void testConnection()
    {
        pcIpAddress_ = view_.pcIpAddress().getText().toString();
        connectionTester_.setPcIpAddress(pcIpAddress_);

        Thread thread = new Thread(connectionTester_);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (connectionTester_.isConnectionSuccessful())
        {
            view_.testConnectionButton().setVisibility(View.GONE);
            view_.continueButton().setVisibility(View.VISIBLE);
            view_.connectionStatus().setText("Connection successful!");
        }
        else
        {
            view_.connectionStatus().setText("Connection unsuccessful. Please try again");
        }
    }

    private void launchFlightControlActivity()
    {
        Intent reconstructNowIntent = new Intent(context_, FlightControlActivity.class);
        reconstructNowIntent.putExtra(IntentExtraKeys.IS_LIVE_MODE_ENABLED, new Boolean(true));
        reconstructNowIntent.putExtra(IntentExtraKeys.PC_IP_ADDRESS, pcIpAddress_);

        context_.startActivity(reconstructNowIntent);
    }
}
