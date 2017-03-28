package com.dji.sdk.sample.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.dji.sdk.sample.common.imageTransfer.src.AndroidPcConnectionTester;
import com.dji.sdk.sample.common.presenter.src.AndroidPcConnectionPresenter;
import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;
import com.dji.sdk.sample.common.view.src.AndroidPcConnectionView;

/**
 * Created by Julia on 2017-03-26.
 */

public class AndroidPcConnectionActivity extends Activity
{
    AndroidPcConnectionView connectionView_;

    ApplicationSettingsManager applicationSettingsManager_;
    AndroidPcConnectionTester connectionTester_;
    AndroidPcConnectionPresenter connectionPresenter_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        connectionView_ = new AndroidPcConnectionView(this);
        if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        applicationSettingsManager_ = new ApplicationSettingsManager(this);
        connectionTester_ = new AndroidPcConnectionTester();
        connectionPresenter_ = new AndroidPcConnectionPresenter(
                this,
                connectionView_,
                connectionTester_,
                applicationSettingsManager_);

        setContentView(connectionView_);
    }

    private boolean isLargeDevice(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return false;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return true;
            default:
                return false;
        }
    }
}