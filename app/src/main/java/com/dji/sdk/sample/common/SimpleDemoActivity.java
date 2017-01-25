package com.dji.sdk.sample.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dji.sdk.sample.common.mission.FlightController;
import com.dji.sdk.sample.common.presenter.ShootPhotoButtonListener;
import com.dji.sdk.sample.common.presenter.ShootPhotoPresenter;
import com.dji.sdk.sample.common.presenter.SimpleDemoPresenter;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.SimpleDemoView;

public class SimpleDemoActivity extends AppCompatActivity
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;
    private SimpleDemoView simpleDemoView_;
    private FlightController flightController_;
    private SimpleDemoPresenter simpleDemoPresenter_;

    private ShootPhotoPresenter shootPhotoPresenter_;
    private ShootPhotoButtonListener shootPhotoButtonListener_;

    protected BroadcastReceiver receiver_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequester_ = new UserPermissionRequester();
        contextManager_ = new ApplicationContextManager(this);
        simpleDemoView_ = new SimpleDemoView(this);
        flightController_ = new FlightController();
        simpleDemoPresenter_ = new SimpleDemoPresenter(simpleDemoView_, flightController_);
        shootPhotoPresenter_ = new ShootPhotoPresenter(contextManager_, simpleDemoView_);
        shootPhotoButtonListener_ = new ShootPhotoButtonListener(contextManager_, simpleDemoView_);

        permissionRequester_.requestPermissions(this);
        setContentView(simpleDemoView_);

        // TODO Extract this stuff into some kind of service
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.DJI_AIRCRAFT_CONNECTION_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                simpleDemoPresenter_.productConnectionChanged();
            }
        };

        registerReceiver(receiver_, filter);
    }


}
