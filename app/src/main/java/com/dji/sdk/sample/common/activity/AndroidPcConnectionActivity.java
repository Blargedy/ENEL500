package com.dji.sdk.sample.common.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.dji.sdk.sample.common.imageTransfer.src.AndroidPcConnectionTester;
import com.dji.sdk.sample.common.presenter.src.AndroidPcConnectionPresenter;
import com.dji.sdk.sample.common.view.src.AndroidPcConnectionView;

/**
 * Created by Julia on 2017-03-26.
 */

public class AndroidPcConnectionActivity extends Activity
{
    AndroidPcConnectionView connectionView_;

    AndroidPcConnectionTester connectionTester_;
    AndroidPcConnectionPresenter connectionPresenter_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        connectionView_ = new AndroidPcConnectionView(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        connectionTester_ = new AndroidPcConnectionTester();
        connectionPresenter_ = new AndroidPcConnectionPresenter(
                this,
                connectionView_,
                connectionTester_);

        setContentView(connectionView_);
    }
}