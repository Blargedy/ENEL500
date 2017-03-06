package com.dji.sdk.sample.common.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.container.MissionContainer;
import com.dji.sdk.sample.common.container.PresenterContainer;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.FlightControlView;
import com.dji.sdk.sample.common.view.MapView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.dji.sdk.sample.common.utility.IntentExtraKeys.*;

public class FlightControlActivity extends FragmentActivity
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;

    private FlightControlView flightControlView_;
    private MapView mapView_;

    private IntegrationLayerContainer integrationLayerContainer_;
    private ImageTransferContainer imageTransferContainer_;
    private MissionContainer missionContainer_;
    private PresenterContainer presenterContainer_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isLiveModeEnabled = getIntent().getBooleanExtra(IS_LIVE_MODE_ENABLED, false);
        String pcIpAddress = getIntent().getStringExtra(PC_IP_ADDRESS);

        permissionRequester_ = new UserPermissionRequester();
        permissionRequester_.requestPermissions(this);
        contextManager_ = new ApplicationContextManager(this);

        flightControlView_ = new FlightControlView(this);
        mapView_ = new MapView(this);

        integrationLayerContainer_ = new IntegrationLayerContainer();
        imageTransferContainer_ = new ImageTransferContainer(
                contextManager_,
                integrationLayerContainer_,
                pcIpAddress,
                isLiveModeEnabled);
        missionContainer_ = new MissionContainer(
                integrationLayerContainer_,
                imageTransferContainer_,
                contextManager_);
        presenterContainer_ = new PresenterContainer(
                mapView_,
                flightControlView_,
                missionContainer_,
                imageTransferContainer_,
                contextManager_,
                this);

        //Replace with
        //        setContentView(mapView_);
        setContentView(flightControlView_);
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleApiClient googleApiClient = presenterContainer_.mapPresenter().googleApiClient();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient.connect();
        AppIndex.AppIndexApi.start(
                googleApiClient, presenterContainer_.mapPresenter().getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        GoogleApiClient googleApiClient = presenterContainer_.mapPresenter().googleApiClient();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(
                googleApiClient, presenterContainer_.mapPresenter().getIndexApiAction());
        googleApiClient.disconnect();
    }
}
