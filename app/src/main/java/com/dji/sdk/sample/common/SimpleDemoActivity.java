package com.dji.sdk.sample.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dji.sdk.sample.common.mission.FlightController;
import com.dji.sdk.sample.common.presenter.SimpleDemoPresenter;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.SimpleDemoView;

public class SimpleDemoActivity extends AppCompatActivity
{
    private UserPermissionRequester permissionRequester_;
    private SimpleDemoView simpleDemoView_;
    private FlightController flightController_;
    private SimpleDemoPresenter simpleDemoPresenter_;

    protected BroadcastReceiver receiver_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequester_ = new UserPermissionRequester();
        simpleDemoView_ = new SimpleDemoView(this);
        flightController_ = new FlightController();
        simpleDemoPresenter_ = new SimpleDemoPresenter(simpleDemoView_, flightController_);

        permissionRequester_.requestPermissions(this);
        setContentView(simpleDemoView_);

        // TODO Extract this stuff into some kind of service
        IntentFilter filter = new IntentFilter();
        filter.addAction(DJISampleApplication.FLAG_CONNECTION_CHANGE);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                simpleDemoPresenter_.productConnectionChanged();
            }
        };

        registerReceiver(receiver_, filter);
    }
}
